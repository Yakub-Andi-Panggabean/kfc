package com.ta.kfc.mercu.interfaces.web.order;

import com.ta.kfc.mercu.service.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RequestOrderController {

    public static final String ORDER_PATH = "/order";
    public static final String REQUEST_ORDER_PATH = ORDER_PATH + "/request";

    private AuthorizationService authorizationService;

    @Autowired
    public RequestOrderController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @GetMapping(REQUEST_ORDER_PATH)
    public String getRequestOrderPage(Model model){

        model.addAttribute("template", "order_request");

        return "index";
    }

}
