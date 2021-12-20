package com.ta.kfc.mercu.interfaces.web.order;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.layout.Document;
import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;
import com.ta.kfc.mercu.context.FastContext;
import com.ta.kfc.mercu.infrastructure.db.orm.model.auth.User;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Product;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrder;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrderStatus;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrderType;
import com.ta.kfc.mercu.service.master.MasterService;
import com.ta.kfc.mercu.service.security.AuthorizationService;
import com.ta.kfc.mercu.service.transaction.RequestOrderService;
import com.ta.kfc.mercu.shared.CollectionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class RequestOrderPageController extends OrderModule {

    private AuthorizationService authorizationService;
    private MasterService masterService;
    private RequestOrderService requestOrderService;
    private FastContext context;
    private ServletContext servletContext;
    private SpringTemplateEngine templateEngine;
    private CollectionHelper collectionHelper;

    @Autowired
    public RequestOrderPageController(FastContext context, AuthorizationService authorizationService,
                                      ServletContext servletContext,
                                      SpringTemplateEngine templateEngine,
                                      MasterService masterService, RequestOrderService requestOrderService,
                                      CollectionHelper collectionHelper) {
        this.authorizationService = authorizationService;
        this.masterService = masterService;
        this.requestOrderService = requestOrderService;
        this.context = context;
        this.servletContext = servletContext;
        this.templateEngine = templateEngine;
        this.collectionHelper = collectionHelper;
    }

    @GetMapping(REQUEST_ORDER_PATH)
    public String getRequestOrderPage(@RequestParam(value = "isUpdate", required = false) boolean isUpdate,
                                      @RequestParam(value = "search", required = false) String search,
                                      @RequestParam(value = "id", required = false) Long id,
                                      Model model) {

        model.addAttribute("template", "order_request");

        User user = context.getUser().get();

        Optional<RequestOrder> inProgressRequestOrder = requestOrderService.findInProgressRequestOrder(user.getUserDetail());

        model.addAttribute("orders", requestOrderService.findRequestOrderPerUser(user.getUserDetail())
                .stream()
                .filter(ro -> ro.getType() == RequestOrderType.REQUEST_ORDER)
                .filter(ro -> !ro.getStatus().equals(RequestOrderStatus.NEW)).collect(Collectors.toList()));

        model.addAttribute("requestOrderExist", false);
        if (inProgressRequestOrder.isPresent()) {
            Map<Product, Long> countedMap = inProgressRequestOrder.get().getProducts().stream()
                    .collect(Collectors.groupingBy(p -> p, Collectors.counting()));
            model.addAttribute("request_order", inProgressRequestOrder.get());
            model.addAttribute("requested_products", countedMap);
            model.addAttribute("requestOrderExist", true);
        } else {
            final RequestOrder requestOrder = new RequestOrder();
            model.addAttribute("request_order", requestOrder);
            model.addAttribute("requested_products", Collections.emptyMap());
        }

        model.addAttribute("products", masterService.getAllProducts());

        return "index";
    }


    @GetMapping(REQUEST_ORDER_PATH + "/detail/{orderId}")
    public String getRequestOrderDetailPage(@PathVariable("orderId") Long orderId, Model model) {

        model.addAttribute("template", "order_request_detail");

        Optional<RequestOrder> requestOrder = requestOrderService.findRequestOrderById(orderId);
        if (requestOrder.isPresent()) {
            model.addAttribute("order", requestOrder.get());
            model.addAttribute("products", collectionHelper.
                    groupProductCount(requestOrder.get().
                            getProducts()));
        }

        return "report";
    }

    @GetMapping(REQUEST_ORDER_PATH + "/pdf/{orderId}")
    public ResponseEntity<?> getRequestOrderPdfPage(@PathVariable("orderId") Long orderId,
                                                    HttpServletRequest request, HttpServletResponse response) {

        Optional<RequestOrder> requestOrder = requestOrderService.findRequestOrderById(orderId);

        WebContext context = new WebContext(request, response, servletContext);
        context.setVariable("template", "order_request_detail");
        context.setVariable("order", requestOrder.get());
        context.setVariable("products", collectionHelper.
                groupProductCount(requestOrder.get().
                        getProducts()));

        String html = templateEngine.process("report", context);

        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri("http://localhost:8080");

        ByteArrayOutputStream target = new ByteArrayOutputStream();

        HtmlConverter.convertToPdf(html, target, converterProperties);

        byte[] bytes = target.toByteArray();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);

    }

}
