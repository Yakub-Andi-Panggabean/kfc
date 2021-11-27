package com.ta.kfc.mercu.interfaces.web.order;

import com.ta.kfc.mercu.infrastructure.db.orm.model.auth.User;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrder;
import com.ta.kfc.mercu.service.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PurchaseOrderController extends OrderModule {

    private AuthorizationService authorizationService;

    @Autowired
    public PurchaseOrderController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @GetMapping({ORDER_PURCHASE_PATH})
    public String getOrderPurchasePage(@RequestParam(value = "isUpdate", required = false) boolean isUpdate,
                                       @RequestParam(value = "search", required = false) String search,
                                       @RequestParam(value = "id", required = false) Long id,
                                       Model model) {

        User user = (User) model.getAttribute("user");

        model.addAttribute("template", "order_purchase");

        return "index";
    }
}
