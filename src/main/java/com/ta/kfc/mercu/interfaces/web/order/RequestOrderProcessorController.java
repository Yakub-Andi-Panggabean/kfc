package com.ta.kfc.mercu.interfaces.web.order;

import com.ta.kfc.mercu.context.FastContext;
import com.ta.kfc.mercu.infrastructure.db.orm.model.auth.User;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Brand;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Product;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrder;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrderStatus;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrderType;
import com.ta.kfc.mercu.service.master.MasterService;
import com.ta.kfc.mercu.service.security.AuthorizationService;
import com.ta.kfc.mercu.service.transaction.RequestOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.Optional;

@Controller
public class RequestOrderProcessorController extends OrderModule {

    private RequestOrderService requestOrderService;
    private MasterService masterService;
    private FastContext context;

    @Autowired
    public RequestOrderProcessorController(FastContext context,
                                           RequestOrderService requestOrderService,
                                           MasterService masterService) {
        this.context = context;
        this.requestOrderService = requestOrderService;
        this.masterService = masterService;
    }

    @PostMapping(REQUEST_ORDER_PATH)
    public String createRequestOrder(Model model,
                                     RequestOrder requestOrder,
                                     RedirectAttributes attrModel) {

        User user = context.getUser().get();

        requestOrder.setCreatedDate(new Date());
        requestOrder.setUpdatedDate(new Date());
        requestOrder.setRequester(user.getUserDetail());
        requestOrder.setTo(user.getUserDetail().getUnit());
        requestOrder.setFrom(user.getUserDetail().getUnit());
        requestOrder.setStatus(RequestOrderStatus.NEW);
        requestOrder.setType(RequestOrderType.REQUEST_ORDER);
        requestOrderService.saveRequestOrder(requestOrder);

        return String.format("redirect:%s?", REQUEST_ORDER_PATH);
    }

    @GetMapping({REQUEST_ORDER_PATH_PRODUCT + "/{action}" + "/{product_id}"})
    public String addRequestOrderItem(@PathVariable("product_id") Long productId,
                                      @PathVariable("action") String action) {

        User user = context.getUser().get();

        Optional<RequestOrder> requestOrder = requestOrderService.findInProgressRequestOrder(user.getUserDetail());
        if (requestOrder.isPresent()) {
            masterService.getProduct(productId).ifPresent(p -> {
                if (action.equals("add")) {
                    requestOrder.get().getProducts().add(p);
                } else {
                    requestOrder.get().getProducts().remove(p);
                }
                requestOrderService.saveRequestOrder(requestOrder.get());
            });
        }

        return String.format("redirect:%s?", REQUEST_ORDER_PATH);
    }

    @GetMapping(REQUEST_ORDER_PATH + "/{status}/{ro_id}")
    public String updateRequestOrderStatus(@PathVariable("ro_id") Long roId,
                                           @PathVariable("status") String status) {

        User user = context.getUser().get();

        Optional<RequestOrder> requestOrder = requestOrderService.findRequestOrderById(roId);

        requestOrder.ifPresent(r -> {
            r.setUpdatedDate(new Date());
            switch (status) {
                case "approval":
                    r.setStatus(RequestOrderStatus.WAITING_APPROVAL);
                    break;
                case "cancel":
                    r.setStatus(RequestOrderStatus.CANCELED);
                    break;
            }
            requestOrderService.saveRequestOrder(r);
        });


        return String.format("redirect:%s?", REQUEST_ORDER_PATH);
    }

}
