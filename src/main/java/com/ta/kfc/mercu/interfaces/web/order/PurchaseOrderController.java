package com.ta.kfc.mercu.interfaces.web.order;

import com.ta.kfc.mercu.context.FastContext;
import com.ta.kfc.mercu.infrastructure.db.orm.model.asset.ItemReceiptStatus;
import com.ta.kfc.mercu.infrastructure.db.orm.model.auth.User;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Supplier;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrder;
import com.ta.kfc.mercu.service.asset.AssetService;
import com.ta.kfc.mercu.service.master.MasterService;
import com.ta.kfc.mercu.service.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class PurchaseOrderController extends OrderModule {

    private FastContext context;
    private MasterService masterService;
    private AssetService assetService;


    @Autowired
    public PurchaseOrderController(FastContext context, MasterService masterService, AssetService assetService) {
        this.context = context;
        this.masterService = masterService;
        this.assetService = assetService;
    }


    @GetMapping({ORDER_PURCHASE_PATH})
    public String getOrderPurchasePage(@RequestParam(value = "isSupplierSelected", required = false) boolean isProductSelected,
                                       @RequestParam(value = "supplierId", required = false) Long supplierID,
                                       Model model) {

        model.addAttribute("template", "order_purchase");
        model.addAttribute("receipts", assetService.getAllItemReceipt()
                .stream()
                .filter(r -> r.getStatus() == ItemReceiptStatus.VERIFIED).collect(Collectors.toList()));

        model.addAttribute("completedReceipts", assetService.getAllItemReceipt()
                .stream()
                .filter(r -> r.getStatus() == ItemReceiptStatus.COMPLETED)
                .collect(Collectors.toList()));

        return "index";
    }


    @GetMapping({ORDER_PURCHASE_PATH + "/{receiptId}"})
    public String getOrderPurchaseModal(@PathVariable("receiptId") Long receiptId, Model model) {

        model.addAttribute("receipt", assetService.findItemReceiptById(receiptId).get());

        return "fragments/order_purchase/modal_order_purchase";
    }

}
