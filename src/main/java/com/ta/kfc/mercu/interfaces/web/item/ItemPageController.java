package com.ta.kfc.mercu.interfaces.web.item;

import com.ta.kfc.mercu.context.FastContext;
import com.ta.kfc.mercu.dto.item.ItemShipmentDto;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Unit;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrder;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrderStatus;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrderType;
import com.ta.kfc.mercu.service.master.MasterService;
import com.ta.kfc.mercu.service.security.AuthorizationService;
import com.ta.kfc.mercu.service.transaction.RequestOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.stream.Collectors;

@Controller
public class ItemPageController extends ItemModule {

    private AuthorizationService authorizationService;
    private RequestOrderService requestOrderService;
    private MasterService masterService;
    private FastContext context;

    @Autowired
    public ItemPageController(FastContext context, AuthorizationService authorizationService,
                              RequestOrderService requestOrderService, MasterService masterService) {
        this.authorizationService = authorizationService;
        this.requestOrderService = requestOrderService;
        this.masterService = masterService;
        this.context = context;
    }


    @GetMapping({ITEM_RECEIPT_PATH})
    public String getItemReceiptPage(Model model) {

        model.addAttribute("template", "item_receipt");
        return "index";
    }


    @GetMapping({ITEM_SHIPMENT_PATH})
    public String getItemShipmentPage(@RequestParam(value = "ro_id", required = false) Long roId,
                                      Model model) {

        ItemShipmentDto itemShipmentDto = new ItemShipmentDto();
        model.addAttribute("template", "item_shipment");
        model.addAttribute("units", masterService.getAllUnit());
        model.addAttribute("orders", requestOrderService.findAllRequestOrders()
                .stream()
                .filter(ro -> ro.getType().equals(RequestOrderType.REQUEST_ORDER))
                .filter(ro -> ro.getStatus().equals(RequestOrderStatus.APPROVED))
                .collect(Collectors.toList()));
        if (roId != null) {
            itemShipmentDto.setRo(requestOrderService.findRequestOrderById(roId).get());
            model.addAttribute("itemShipmentDto", itemShipmentDto);
            model.addAttribute("roSelected", true);
        } else {
            RequestOrder mockRo = new RequestOrder();
            Unit mockUnit = new Unit();
            mockUnit.setUnitName("");
            mockUnit.setCity("");
            mockUnit.setZipCode("");
            mockRo.setFrom(mockUnit);
            itemShipmentDto.setRo(mockRo);
            model.addAttribute("itemShipmentDto", itemShipmentDto);
            model.addAttribute("roSelected", false);
        }

        return "index";
    }


    @GetMapping({ITEM_TRANSFER_PATH})
    public String getItemTransferPage(Model model) {

        model.addAttribute("template", "item_transfer");
        return "index";
    }

}
