package com.ta.kfc.mercu.interfaces.web.report;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.ta.kfc.mercu.context.FastContext;
import com.ta.kfc.mercu.dto.report.ProductParameter;
import com.ta.kfc.mercu.dto.report.UnitParameter;
import com.ta.kfc.mercu.infrastructure.db.orm.model.actor.UserDetail;
import com.ta.kfc.mercu.infrastructure.db.orm.model.asset.Asset;
import com.ta.kfc.mercu.infrastructure.db.orm.model.asset.ItemReceipt;
import com.ta.kfc.mercu.infrastructure.db.orm.model.auth.User;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Product;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Unit;
import com.ta.kfc.mercu.infrastructure.db.orm.model.stock.StockOpname;
import com.ta.kfc.mercu.infrastructure.db.orm.model.stock.StockOpnameAsset;
import com.ta.kfc.mercu.infrastructure.db.orm.model.stock.StockOpnameDetail;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrder;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrderType;
import com.ta.kfc.mercu.interfaces.web.item.ItemModule;
import com.ta.kfc.mercu.interfaces.web.order.OrderModule;
import com.ta.kfc.mercu.service.asset.AssetService;
import com.ta.kfc.mercu.service.master.MasterService;
import com.ta.kfc.mercu.service.security.AuthenticationService;
import com.ta.kfc.mercu.service.stock.StockOpnameService;
import com.ta.kfc.mercu.service.transaction.RequestOrderService;
import com.ta.kfc.mercu.shared.CollectionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class ReportController {

    private static final String REPORT_PATH = "/report";

    private RequestOrderService requestOrderService;
    private AssetService assetService;
    private ServletContext servletContext;
    private CollectionHelper collectionHelper;
    private SpringTemplateEngine templateEngine;
    private AuthenticationService authenticationService;
    private MasterService masterService;
    private FastContext fastContext;
    private StockOpnameService stockOpnameService;


    @Autowired
    public ReportController(RequestOrderService requestOrderService,
                            ServletContext servletContext,
                            FastContext fastContext,
                            AssetService assetService,
                            MasterService masterService,
                            AuthenticationService authenticationService,
                            SpringTemplateEngine templateEngine,
                            StockOpnameService stockOpnameService,
                            CollectionHelper collectionHelper) {

        this.requestOrderService = requestOrderService;
        this.authenticationService = authenticationService;
        this.servletContext = servletContext;
        this.collectionHelper = collectionHelper;
        this.templateEngine = templateEngine;
        this.assetService = assetService;
        this.masterService = masterService;
        this.fastContext = fastContext;
        this.stockOpnameService = stockOpnameService;
    }

    @GetMapping({REPORT_PATH})
    public String getReportPage(Model model) {

        model.addAttribute("template", "report");
        model.addAttribute("units", masterService.getAllUnit());
        model.addAttribute("products", masterService.getAllProducts());
        model.addAttribute("unitParam", new UnitParameter());
        model.addAttribute("productParam", new ProductParameter());


        return "index";
    }

    @GetMapping(OrderModule.REQUEST_ORDER_PATH + "/pdf/{orderId}")
    public ResponseEntity<?> getRequestOrderPdfPage(@PathVariable("orderId") Long orderId,
                                                    HttpServletRequest request, HttpServletResponse response) {

        Optional<RequestOrder> requestOrder = requestOrderService.findRequestOrderById(orderId);

        WebContext context = new WebContext(request, response, servletContext);
        context.setVariable("report_template", "order_request_detail");
        context.setVariable("order", requestOrder.get());
        context.setVariable("products", collectionHelper.
                groupProductCount(requestOrder.get().
                        getProducts()));

        String html = templateEngine.process("report", context);

        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort());

        ByteArrayOutputStream target = new ByteArrayOutputStream();

        HtmlConverter.convertToPdf(html, target, converterProperties);

        byte[] bytes = target.toByteArray();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);

    }

    @GetMapping(ItemModule.ITEM_RECEIPT_PATH + "/pdf/{receiptId}")
    public ResponseEntity<?> getItemReceiptDetailPdfPage(@PathVariable("receiptId") Long receiptId,
                                                         HttpServletRequest request,
                                                         HttpServletResponse response) {

        Optional<ItemReceipt> itemReceipt = assetService.findItemReceiptById(receiptId);

        WebContext context = new WebContext(request, response, servletContext);
        context.setVariable("report_template", "item_receipt_detail");
        context.setVariable("receipt", itemReceipt.get());

        String html = templateEngine.process("report", context);

        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort());

        ByteArrayOutputStream target = new ByteArrayOutputStream();

        HtmlConverter.convertToPdf(html, target, converterProperties);

        byte[] bytes = target.toByteArray();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);
    }

    @GetMapping(REPORT_PATH + "/asset/unit")
    public ResponseEntity<?> getAssetCountPerunitPdfPage(HttpServletRequest request,
                                                         HttpServletResponse response) {

        List<Asset> assets = assetService.getAllAsset();

        Map<Unit, Long> assetCountPerunit = assets.
                stream().collect(Collectors.groupingBy(Asset::getUnit, Collectors.counting()));

        WebContext context = new WebContext(request, response, servletContext);
        context.setVariable("report_template", "asset_per_unit");
        context.setVariable("assetCountPerUnit", assetCountPerunit);

        String html = templateEngine.process("report", context);

        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort());

        ByteArrayOutputStream target = new ByteArrayOutputStream();

        HtmlConverter.convertToPdf(html, target, converterProperties);

        byte[] bytes = target.toByteArray();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);
    }


    @GetMapping(REPORT_PATH + "/asset/unit/list")
    public ResponseEntity<?> getAssetListPerunitPdfPage(UnitParameter unitParameter,
                                                        HttpServletRequest request,
                                                        HttpServletResponse response) throws UnsupportedEncodingException, DocumentException {

        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Font font1 = new Font(Font.FontFamily.HELVETICA, 80, Font.BOLD
                | Font.UNDERLINE);

        Paragraph paragraph1 = new Paragraph();
        paragraph1.add("Asset Report Unit");
        paragraph1.setAlignment(Element.ALIGN_CENTER);
        paragraph1.setFont(font1);

        Paragraph paragraph2 = new Paragraph();
        paragraph2.add(unitParameter.getUnit().getUnitName());
        paragraph2.setAlignment(Element.ALIGN_CENTER);
        paragraph2.setFont(font1);

        Paragraph paragraph3 = new Paragraph();
        paragraph3.add("Total Asset :" + unitParameter.getUnit().getAssets().size());
        paragraph3.setAlignment(Element.ALIGN_CENTER);
        paragraph3.setFont(font1);


        PdfPTable table = new PdfPTable(4);

        Font headFont = FontFactory.getFont(FontFactory.HELVETICA, 17, BaseColor.WHITE);

        PdfPCell hcell;
        hcell = new PdfPCell(new Phrase("Asset Code", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setBackgroundColor(BaseColor.RED);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Product", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setBackgroundColor(BaseColor.RED);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Brand", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setBackgroundColor(BaseColor.RED);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Model", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setBackgroundColor(BaseColor.RED);
        table.addCell(hcell);

        for (Asset a : unitParameter.getUnit().getAssets()) {
            PdfPCell cell;

            cell = new PdfPCell(new Phrase(a.getCode()));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);


            cell = new PdfPCell(new Phrase(a.getProduct().getName()));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);


            cell = new PdfPCell(new Phrase(a.getProduct().getBrand().getName()));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(a.getProduct().getModel().getName()));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        com.itextpdf.text.pdf.PdfWriter.getInstance(document, out);
        document.open();
        document.add(paragraph1);
        document.add(paragraph2);
        document.add(paragraph3);
        document.add(new Paragraph(" "));
        document.addTitle("Unit :".concat(unitParameter.getUnit().getUnitName()));
        document.add(table);
        document.close();


        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(out.toByteArray());
    }


    @GetMapping(REPORT_PATH + "/asset/product/list")
    public ResponseEntity<?> getAssetListPerproductPdfPage(ProductParameter productParameter,
                                                           HttpServletRequest request,
                                                           HttpServletResponse response) throws UnsupportedEncodingException, DocumentException {

        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Font font1 = new Font(Font.FontFamily.HELVETICA, 80, Font.BOLD
                | Font.UNDERLINE);

        List<Asset> assets = assetService.getAllAsset().stream()
                .filter(a -> a.getProduct().getId() == productParameter.getProduct().getId())
                .collect(Collectors.toList());

        Paragraph paragraph1 = new Paragraph();
        paragraph1.add("Asset Report Product");
        paragraph1.setAlignment(Element.ALIGN_CENTER);
        paragraph1.setFont(font1);

        Paragraph paragraph2 = new Paragraph();
        paragraph2.add(productParameter.getProduct().getName());
        paragraph2.setAlignment(Element.ALIGN_CENTER);
        paragraph2.setFont(font1);

        Paragraph paragraph3 = new Paragraph();
        paragraph3.add("Total Asset :" + assets.size());
        paragraph3.setAlignment(Element.ALIGN_CENTER);
        paragraph3.setFont(font1);


        PdfPTable table = new PdfPTable(5);

        Font headFont = FontFactory.getFont(FontFactory.HELVETICA, 17, BaseColor.WHITE);

        PdfPCell hcell;
        hcell = new PdfPCell(new Phrase("Asset Code", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setBackgroundColor(BaseColor.RED);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Product", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setBackgroundColor(BaseColor.RED);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Brand", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setBackgroundColor(BaseColor.RED);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Model", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setBackgroundColor(BaseColor.RED);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Location", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setBackgroundColor(BaseColor.RED);
        table.addCell(hcell);

        for (Asset a : assets) {
            PdfPCell cell;

            cell = new PdfPCell(new Phrase(a.getCode()));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);


            cell = new PdfPCell(new Phrase(a.getProduct().getName()));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);


            cell = new PdfPCell(new Phrase(a.getProduct().getBrand().getName()));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(a.getProduct().getModel().getName()));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(a.getUnit().getUnitName()));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        com.itextpdf.text.pdf.PdfWriter.getInstance(document, out);
        document.open();
        document.add(paragraph1);
        document.add(paragraph2);
        document.add(paragraph3);
        document.add(new Paragraph(" "));
        document.addTitle("Product :".concat(productParameter.getProduct().getName()));
        document.add(table);
        document.close();


        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(out.toByteArray());
    }


    @GetMapping(REPORT_PATH + "/user/list")
    public ResponseEntity<?> getAssetListPerproductPdfPage() throws UnsupportedEncodingException, DocumentException {

        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        List<User> users = authenticationService.findAll();

        Font font1 = new Font(Font.FontFamily.HELVETICA, 80, Font.BOLD
                | Font.UNDERLINE);

        Paragraph paragraph1 = new Paragraph();
        paragraph1.add("User Report");
        paragraph1.setAlignment(Element.ALIGN_CENTER);
        paragraph1.setFont(font1);


        PdfPTable table = new PdfPTable(6);

        Font headFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.WHITE);

        PdfPCell hcell;
        hcell = new PdfPCell(new Phrase("Employee-id", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setBackgroundColor(BaseColor.RED);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Firstname", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setBackgroundColor(BaseColor.RED);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Lastname", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setBackgroundColor(BaseColor.RED);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Department", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setBackgroundColor(BaseColor.RED);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Unit", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setBackgroundColor(BaseColor.RED);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Role", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setBackgroundColor(BaseColor.RED);
        table.addCell(hcell);

        Font rowFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);


        for (User u : users) {
            PdfPCell cell;

            cell = new PdfPCell(new Phrase(u.getUserDetail().getCode(), rowFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);


            cell = new PdfPCell(new Phrase(u.getUserDetail().getFirstName(), rowFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);


            cell = new PdfPCell(new Phrase(u.getUserDetail().getLastName()));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(u.getUserDetail().getDepartment().getName(), rowFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(u.getUserDetail().getUnit().getUnitName(), rowFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(u.getRoles().toString(), rowFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        com.itextpdf.text.pdf.PdfWriter.getInstance(document, out);
        document.open();
        document.add(paragraph1);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(table);
        document.close();


        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(out.toByteArray());
    }


    @GetMapping(REPORT_PATH + "/product/list")
    public ResponseEntity<?> getProductListPdfPage() throws DocumentException {

        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        List<Product> products = masterService.getAllProducts();

        Font font1 = new Font(Font.FontFamily.HELVETICA, 80, Font.BOLD
                | Font.UNDERLINE);

        Paragraph paragraph1 = new Paragraph();
        paragraph1.add("Product Report");
        paragraph1.setAlignment(Element.ALIGN_CENTER);
        paragraph1.setFont(font1);


        PdfPTable table = new PdfPTable(4);

        Font headFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.WHITE);

        PdfPCell hcell;
        hcell = new PdfPCell(new Phrase("Name", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setBackgroundColor(BaseColor.RED);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Brand", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setBackgroundColor(BaseColor.RED);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Model", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setBackgroundColor(BaseColor.RED);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Type", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setBackgroundColor(BaseColor.RED);
        table.addCell(hcell);

        Font rowFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);


        for (Product u : products) {
            PdfPCell cell;

            cell = new PdfPCell(new Phrase(u.getName(), rowFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);


            cell = new PdfPCell(new Phrase(u.getBrand().getName(), rowFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);


            cell = new PdfPCell(new Phrase(u.getModel().getName(), rowFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(u.getProductType().toString(), rowFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        com.itextpdf.text.pdf.PdfWriter.getInstance(document, out);
        document.open();
        document.add(paragraph1);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(table);
        document.close();


        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(out.toByteArray());
    }


    @GetMapping(REPORT_PATH + "/ro/list")
    public ResponseEntity<?> getRequestOrderPdfPage() throws DocumentException {

        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        List<RequestOrder> ros = requestOrderService.findAllRequestOrders()
                .stream()
                .filter(o -> o.getType() == RequestOrderType.REQUEST_ORDER)
                .collect(Collectors.toList());

        Font font1 = new Font(Font.FontFamily.HELVETICA, 80, Font.BOLD
                | Font.UNDERLINE);

        Paragraph paragraph1 = new Paragraph();
        paragraph1.add("Request Order Report");
        paragraph1.setAlignment(Element.ALIGN_CENTER);
        paragraph1.setFont(font1);


        PdfPTable table = new PdfPTable(7);

        Font headFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.WHITE);

        PdfPCell hcell;
        hcell = new PdfPCell(new Phrase("RO ID", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setBackgroundColor(BaseColor.RED);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Description", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setBackgroundColor(BaseColor.RED);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Department", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setBackgroundColor(BaseColor.RED);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Unit", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setBackgroundColor(BaseColor.RED);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Requested By", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setBackgroundColor(BaseColor.RED);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Approved By", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setBackgroundColor(BaseColor.RED);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Status", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setBackgroundColor(BaseColor.RED);
        table.addCell(hcell);

        Font rowFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);


        for (RequestOrder ro : ros) {
            PdfPCell cell;

            cell = new PdfPCell(new Phrase("RO-" + ro.getId().toString(), rowFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);


            cell = new PdfPCell(new Phrase(ro.getDescription(), rowFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);


            cell = new PdfPCell(new Phrase(ro.getRequester().getDepartment().getName(), rowFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(ro.getFrom().getUnitName(), rowFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(ro.getRequester().getFirstName(), rowFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            String approver = "";
            if (ro.getApprover() != null) {
                approver = ro.getApprover().getFirstName();
            }
            cell = new PdfPCell(new Phrase(approver, rowFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(ro.getStatus().toString(), rowFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        com.itextpdf.text.pdf.PdfWriter.getInstance(document, out);
        document.open();
        document.add(paragraph1);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(table);
        document.close();


        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(out.toByteArray());
    }


    @GetMapping(REPORT_PATH + "/to/list")
    public ResponseEntity<?> getTransferOrderPdfPage() throws DocumentException {

        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        List<RequestOrder> ros = requestOrderService.findAllRequestOrders()
                .stream()
                .filter(o -> o.getType() == RequestOrderType.TRANSFER_ORDER)
                .collect(Collectors.toList());

        Font font1 = new Font(Font.FontFamily.HELVETICA, 80, Font.BOLD
                | Font.UNDERLINE);

        Paragraph paragraph1 = new Paragraph();
        paragraph1.add("Transfer Order Report");
        paragraph1.setAlignment(Element.ALIGN_CENTER);
        paragraph1.setFont(font1);

        PdfPTable table = new PdfPTable(7);

        Font headFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.WHITE);

        PdfPCell hcell;
        hcell = new PdfPCell(new Phrase("Transfer ID", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setBackgroundColor(BaseColor.RED);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Description", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setBackgroundColor(BaseColor.RED);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("From", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setBackgroundColor(BaseColor.RED);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("To", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setBackgroundColor(BaseColor.RED);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Requested By", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setBackgroundColor(BaseColor.RED);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Approved By", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setBackgroundColor(BaseColor.RED);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Status", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setBackgroundColor(BaseColor.RED);
        table.addCell(hcell);

        Font rowFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);

        for (RequestOrder ro : ros) {
            PdfPCell cell;

            cell = new PdfPCell(new Phrase("TO-" + ro.getId().toString(), rowFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);


            cell = new PdfPCell(new Phrase(ro.getDescription(), rowFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);


            cell = new PdfPCell(new Phrase(ro.getFrom().getUnitName(), rowFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(ro.getTo().getUnitName(), rowFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(ro.getRequester().getFirstName(), rowFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            String approver = "";
            if (ro.getApprover() != null) {
                approver = ro.getApprover().getFirstName();
            }
            cell = new PdfPCell(new Phrase(approver, rowFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(ro.getStatus().toString(), rowFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        com.itextpdf.text.pdf.PdfWriter.getInstance(document, out);
        document.open();
        document.add(paragraph1);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(table);
        document.close();


        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(out.toByteArray());
    }


    @GetMapping(REPORT_PATH + "/so/product/{soId}/{productId}")
    public ResponseEntity<?> getSoDetailPdfPage(@PathVariable("soId") Long soId,
                                                @PathVariable("productId") Long productId) throws DocumentException {

        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Optional<StockOpname> so = stockOpnameService.find(soId);

        Optional<StockOpnameDetail> soDetail = so.map(StockOpname::getDetails)
                .orElse(Collections.emptyList())
                .stream()
                .filter(d -> d.getProduct().getId() == productId)
                .findAny();

        Font font1 = new Font(Font.FontFamily.HELVETICA, 80, Font.BOLD
                | Font.UNDERLINE);

        Paragraph title = new Paragraph();
        title.add("Stock Opname Detail");
        title.setAlignment(Element.ALIGN_CENTER);
        title.setFont(font1);

        PdfPTable table = new PdfPTable(2);

        Font headFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

        PdfPCell hcell;
        hcell = new PdfPCell(new Phrase("Unit Name :" + so.get().getUnit().getUnitName(), headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        hcell.setBorder(Rectangle.NO_BORDER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Brand :" + soDetail.get().getProduct().getBrand(), headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        hcell.setBorder(Rectangle.NO_BORDER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Adjusted Qty :" + soDetail.get().getQtyAdjustment(), headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        hcell.setBorder(Rectangle.NO_BORDER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Product :" + soDetail.get().getProduct().getName(), headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        hcell.setBorder(Rectangle.NO_BORDER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Book Qty :" + soDetail.get().getQty(), headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        hcell.setBorder(Rectangle.NO_BORDER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Model :" + soDetail.get().getProduct().getModel(), headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        hcell.setBorder(Rectangle.NO_BORDER);
        table.addCell(hcell);

        PdfPTable tableAsset = new PdfPTable(2);

        PdfPCell hcellDetail;
        hcellDetail = new PdfPCell(new Phrase("Asset Code", headFont));
        hcellDetail.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableAsset.addCell(hcellDetail);

        hcellDetail = new PdfPCell(new Phrase("Status", headFont));
        hcellDetail.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableAsset.addCell(hcellDetail);

        Font rowFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);

        for (StockOpnameAsset as : soDetail.get().getAssets()) {
            PdfPCell cell;

            cell = new PdfPCell(new Phrase(as.getAsset().getCode(), rowFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAsset.addCell(cell);


            cell = new PdfPCell(new Phrase(as.getStatus().toString(), rowFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAsset.addCell(cell);
        }

        Paragraph checkedBy = new Paragraph();
        checkedBy.add("Checked By " + so.get().getCreatedBy().getFirstName());
        checkedBy.setAlignment(Element.ALIGN_CENTER);
        checkedBy.setFont(rowFont);

        com.itextpdf.text.pdf.PdfWriter.getInstance(document, out);
        document.open();
        document.add(title);
        document.add(new Paragraph(" "));
        document.add(table);
        document.add(new Paragraph(" "));
        document.add(tableAsset);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(checkedBy);
        document.close();


        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(out.toByteArray());
    }


    @GetMapping(REPORT_PATH + "/so/{soId}")
    public ResponseEntity<?> getSoDetailPdfPage(@PathVariable("soId") Long soId) throws DocumentException {

        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Optional<StockOpname> so = stockOpnameService.find(soId);

        Font font1 = new Font(Font.FontFamily.HELVETICA, 80, Font.BOLD
                | Font.UNDERLINE);

        Paragraph title = new Paragraph();
        title.add("Stock Opname");
        title.setAlignment(Element.ALIGN_CENTER);
        title.setFont(font1);

        Paragraph unit = new Paragraph();
        unit.add(so.get().getUnit().getUnitName());
        unit.setAlignment(Element.ALIGN_CENTER);
        unit.setFont(font1);

        Paragraph date = new Paragraph();
        date.add(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(so.get().getCreatedDate()));
        date.setAlignment(Element.ALIGN_CENTER);
        date.setFont(font1);

        Font headFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

        PdfPTable tableAsset = new PdfPTable(5);

        PdfPCell hcellDetail;
        hcellDetail = new PdfPCell(new Phrase("Asset Code", headFont));
        hcellDetail.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableAsset.addCell(hcellDetail);

        hcellDetail = new PdfPCell(new Phrase("Product", headFont));
        hcellDetail.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableAsset.addCell(hcellDetail);

        hcellDetail = new PdfPCell(new Phrase("Brand", headFont));
        hcellDetail.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableAsset.addCell(hcellDetail);

        hcellDetail = new PdfPCell(new Phrase("Model", headFont));
        hcellDetail.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableAsset.addCell(hcellDetail);

        hcellDetail = new PdfPCell(new Phrase("Status", headFont));
        hcellDetail.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableAsset.addCell(hcellDetail);

        Font rowFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);

        List<StockOpnameAsset> soAssets = so.get().getDetails().stream().map(s -> s.getAssets())
                .flatMap(List::stream)
                .collect(Collectors.toList());

        for (StockOpnameAsset as : soAssets) {
            PdfPCell cell;

            cell = new PdfPCell(new Phrase(as.getAsset().getCode(), rowFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAsset.addCell(cell);

            cell = new PdfPCell(new Phrase(as.getAsset().getProduct().getName(), rowFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAsset.addCell(cell);

            cell = new PdfPCell(new Phrase(as.getAsset().getProduct().getBrand().getName(), rowFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAsset.addCell(cell);

            cell = new PdfPCell(new Phrase(as.getAsset().getProduct().getModel().getName(), rowFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAsset.addCell(cell);

            cell = new PdfPCell(new Phrase(as.getStatus().toString(), rowFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAsset.addCell(cell);
        }

        Paragraph checkedBy = new Paragraph();
        checkedBy.add("Checked By " + so.get().getCreatedBy().getFirstName());
        checkedBy.setAlignment(Element.ALIGN_CENTER);
        checkedBy.setFont(rowFont);

        com.itextpdf.text.pdf.PdfWriter.getInstance(document, out);
        document.open();
        document.add(title);
        document.add(new Paragraph(" "));
        document.add(unit);
        document.add(new Paragraph(" "));
        document.add(date);
        document.add(new Paragraph(" "));
        document.add(tableAsset);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(checkedBy);
        document.close();


        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(out.toByteArray());
    }


    @GetMapping(REPORT_PATH + "/to/{toId}")
    public ResponseEntity<?> getToDetailPdfPage(@PathVariable("toId") Long toId) throws DocumentException {

        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Optional<RequestOrder> to = requestOrderService.findRequestOrderById(toId);

        Font font1 = new Font(Font.FontFamily.HELVETICA, 80, Font.BOLD
                | Font.UNDERLINE);

        Paragraph title = new Paragraph();
        title.add("Transfer Request Detail");
        title.setAlignment(Element.ALIGN_CENTER);
        title.setFont(font1);

        PdfPTable table = new PdfPTable(2);

        Font headFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

        PdfPCell hcell;
        hcell = new PdfPCell(new Phrase("Transfer ID : TF-" + to.get().getId(), headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        hcell.setBorder(Rectangle.NO_BORDER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("From : " + to.get().getFrom().getUnitName(), headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        hcell.setBorder(Rectangle.NO_BORDER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Requested By :" + to.get().getRequester().getFirstName(), headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        hcell.setBorder(Rectangle.NO_BORDER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("To     : " + to.get().getTo().getUnitName(), headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        hcell.setBorder(Rectangle.NO_BORDER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Created At: " + new SimpleDateFormat("YYYY-MM-DD HH:mm:ss").format(to.get().getCreatedDate()), headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        hcell.setBorder(Rectangle.NO_BORDER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        hcell.setBorder(Rectangle.NO_BORDER);
        table.addCell(hcell);

        PdfPTable tableAsset = new PdfPTable(4);

        PdfPCell hcellDetail;
        hcellDetail = new PdfPCell(new Phrase("Asset Code", headFont));
        hcellDetail.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableAsset.addCell(hcellDetail);

        hcellDetail = new PdfPCell(new Phrase("Product", headFont));
        hcellDetail.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableAsset.addCell(hcellDetail);

        hcellDetail = new PdfPCell(new Phrase("Brand", headFont));
        hcellDetail.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableAsset.addCell(hcellDetail);

        hcellDetail = new PdfPCell(new Phrase("Model", headFont));
        hcellDetail.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableAsset.addCell(hcellDetail);

        Font rowFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);

        for (Asset as : to.get().getAssets()) {
            PdfPCell cell;

            cell = new PdfPCell(new Phrase(as.getCode(), rowFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAsset.addCell(cell);

            cell = new PdfPCell(new Phrase(as.getProduct().getName(), rowFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAsset.addCell(cell);

            cell = new PdfPCell(new Phrase(as.getProduct().getBrand().getName(), rowFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAsset.addCell(cell);

            cell = new PdfPCell(new Phrase(as.getProduct().getModel().getName(), rowFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAsset.addCell(cell);
        }


        com.itextpdf.text.pdf.PdfWriter.getInstance(document, out);
        document.open();
        document.add(title);
        document.add(new Paragraph(" "));
        document.add(table);
        document.add(new Paragraph(" "));
        document.add(tableAsset);
        document.close();


        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(out.toByteArray());
    }


}
