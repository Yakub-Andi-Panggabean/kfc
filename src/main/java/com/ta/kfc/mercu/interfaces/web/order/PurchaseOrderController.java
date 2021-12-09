package com.ta.kfc.mercu.interfaces.web.order;

import com.ta.kfc.mercu.context.FastContext;
import com.ta.kfc.mercu.infrastructure.db.orm.model.auth.User;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrder;
import com.ta.kfc.mercu.service.master.MasterService;
import com.ta.kfc.mercu.service.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PurchaseOrderController extends OrderModule {

    private FastContext context;
    private MasterService masterService;


    @Autowired
    public PurchaseOrderController(FastContext context, MasterService masterService) {
        this.context = context;
        this.masterService = masterService;
    }


    @GetMapping({ORDER_PURCHASE_PATH})
    public String getOrderPurchasePage(@RequestParam(value = "isUpdate", required = false) boolean isUpdate,
                                       @RequestParam(value = "isProductSelected", required = false) boolean isProductSelected,
                                       @RequestParam(value = "search", required = false) String search,
                                       @RequestParam(value = "productId", required = false) Long productID,
                                       Model model) {


        model.addAttribute("template", "order_purchase");
        model.addAttribute("suppliers", masterService.findAllSuppliers());
        model.addAttribute("isProductSelected", isProductSelected);
        if (productID != null) {
            model.addAttribute("selectedProduct", masterService.getProduct(productID).get());
        }

        return "index";
    }
}
