package com.ta.kfc.mercu.interfaces.web.order;

import com.ta.kfc.mercu.context.FastContext;
import com.ta.kfc.mercu.infrastructure.db.orm.model.auth.User;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Supplier;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrder;
import com.ta.kfc.mercu.service.master.MasterService;
import com.ta.kfc.mercu.service.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

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
    public String getOrderPurchasePage(@RequestParam(value = "isSupplierSelected", required = false) boolean isProductSelected,
                                       @RequestParam(value = "supplierId", required = false) Long supplierID,
                                       Model model) {

        model.addAttribute("template", "order_purchase");
        model.addAttribute("suppliers", masterService.findAllSuppliers());
        model.addAttribute("isSupplierSelected", isProductSelected);

        if (isProductSelected) {
            Optional<Supplier> supplier = masterService.findSupplier(supplierID);
            if (supplier.isPresent()) {
                model.addAttribute("selectedSupplier", supplier.get());
            }
        }

        return "index";
    }
}
