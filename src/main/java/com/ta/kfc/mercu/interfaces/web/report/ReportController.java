package com.ta.kfc.mercu.interfaces.web.report;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrder;
import com.ta.kfc.mercu.interfaces.web.order.OrderModule;
import com.ta.kfc.mercu.service.transaction.RequestOrderService;
import com.ta.kfc.mercu.shared.CollectionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.Optional;

@Controller
public class ReportController {

    private RequestOrderService requestOrderService;
    private ServletContext servletContext;
    private CollectionHelper collectionHelper;
    private SpringTemplateEngine templateEngine;


    @Autowired
    public ReportController(RequestOrderService requestOrderService,
                            ServletContext servletContext,
                            SpringTemplateEngine templateEngine,
                            CollectionHelper collectionHelper) {

        this.requestOrderService = requestOrderService;
        this.servletContext = servletContext;
        this.collectionHelper = collectionHelper;
        this.templateEngine = templateEngine;
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

}
