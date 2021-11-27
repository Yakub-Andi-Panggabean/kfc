package com.ta.kfc.mercu.interfaces.web.order;

import com.ta.kfc.mercu.infrastructure.db.orm.model.auth.User;
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

import java.util.Collections;
import java.util.Optional;

@Controller
public class RequestOrderPageController extends OrderModule {

    private AuthorizationService authorizationService;
    private MasterService masterService;
    private RequestOrderService requestOrderService;

    @Autowired
    public RequestOrderPageController(AuthorizationService authorizationService,
                                      MasterService masterService, RequestOrderService requestOrderService) {
        this.authorizationService = authorizationService;
        this.masterService = masterService;
        this.requestOrderService = requestOrderService;
    }

    @GetMapping(REQUEST_ORDER_PATH)
    public String getRequestOrderPage(@RequestParam(value = "isUpdate", required = false) boolean isUpdate,
                                      @RequestParam(value = "search", required = false) String search,
                                      @RequestParam(value = "id", required = false) Long id,
                                      Model model) {

        model.addAttribute("template", "order_request");

        User user = (User) model.getAttribute("user");

        Optional<RequestOrder> inProgressRequestOrder = requestOrderService.findInProgressRequestOrder(user.getUserDetail());

        if (inProgressRequestOrder.isPresent()) {
            model.addAttribute("request_order", inProgressRequestOrder);
            model.addAttribute("requested_products", inProgressRequestOrder.get().getProducts());
        } else {
            final RequestOrder requestOrder = new RequestOrder();
            requestOrder.setRequester(user.getUserDetail());
            requestOrder.setTo(user.getUserDetail().getUnit());
            requestOrder.setFrom(user.getUserDetail().getUnit());
            requestOrder.setStatus(RequestOrderStatus.NEW);
            requestOrder.setType(RequestOrderType.REQUEST_ORDER);
            model.addAttribute("request_order", requestOrder);
            model.addAttribute("requested_products", Collections.emptyList());
        }

        model.addAttribute("products", masterService.getAllProducts());

        return "index";
    }

}