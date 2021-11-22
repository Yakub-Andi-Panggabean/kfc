package com.ta.kfc.mercu.interfaces.web.page.order;

import com.ta.kfc.mercu.service.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PurchaseOrderController {

    public static final String ORDER_PATH = "/order";
    public static final String ORDER_PURCHASE_PATH = ORDER_PATH + "/purchase";

    private AuthorizationService authorizationService;

    @Autowired
    public PurchaseOrderController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @GetMapping({ORDER_PURCHASE_PATH})
    public String getOrderPurchasePage(Model model) {

        model.addAttribute("template", "order_purchase");
        return "index";
    }
}
