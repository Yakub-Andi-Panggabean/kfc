package com.ta.kfc.mercu.interfaces.web.item;

import com.ta.kfc.mercu.service.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ItemController {

    public static final String ITEM_PATH = "/item";
    public static final String ITEM_RECEIPT_PATH = ITEM_PATH + "/receipt";
    public static final String ITEM_SHIPMENT_PATH = ITEM_PATH + "/shipment";
    public static final String ITEM_TRANSFER_PATH = ITEM_PATH + "/transfer";

    private AuthorizationService authorizationService;

    @Autowired
    public ItemController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @GetMapping({ITEM_RECEIPT_PATH})
    public String getItemReceiptPage(Model model) {

        model.addAttribute("template", "item_receipt");
        return "index";
    }


    @GetMapping({ITEM_SHIPMENT_PATH})
    public String getItemShipmentPage(Model model) {

        model.addAttribute("template", "item_shipment");
        return "index";
    }


    @GetMapping({ITEM_TRANSFER_PATH})
    public String getItemTransferPage(Model model) {

        model.addAttribute("template", "item_transfer");
        return "index";
    }

}
