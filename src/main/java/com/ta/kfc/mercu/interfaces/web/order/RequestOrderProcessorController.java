package com.ta.kfc.mercu.interfaces.web.order;

import com.ta.kfc.mercu.context.FastContext;
import com.ta.kfc.mercu.infrastructure.db.orm.model.auth.User;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Brand;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Product;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.*;
import com.ta.kfc.mercu.interfaces.web.approval.ApprovalModule;
import com.ta.kfc.mercu.service.master.MasterService;
import com.ta.kfc.mercu.service.security.AuthorizationService;
import com.ta.kfc.mercu.service.transaction.RequestOrderService;
import com.ta.kfc.mercu.service.transaction.TransactionService;
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
    private TransactionService transactionService;
    private FastContext context;

    @Autowired
    public RequestOrderProcessorController(FastContext context,
                                           RequestOrderService requestOrderService,
                                           TransactionService transactionService,
                                           MasterService masterService) {
        this.context = context;
        this.requestOrderService = requestOrderService;
        this.transactionService = transactionService;
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

        String page = REQUEST_ORDER_PATH;

        if (requestOrder.isPresent()) {
            requestOrder.get().setUpdatedDate(new Date());
            requestOrder.get().setApprover(user.getUserDetail());
            switch (status) {
                case "approval":
                    requestOrder.get().setStatus(RequestOrderStatus.WAITING_APPROVAL);
                    break;
                case "cancel":
                    requestOrder.get().setStatus(RequestOrderStatus.CANCELED);
                    break;
                case "approve":
                    if (requestOrder.get().getStatus()
                            .equals(RequestOrderStatus.WAITING_APPROVAL)) {

                        requestOrder.get().setStatus(RequestOrderStatus.APPROVED);

                    } else if (requestOrder.get().getStatus()
                            .equals(RequestOrderStatus.WAITING_SEND_APPROVAL)) {

                        requestOrder.get().setStatus(RequestOrderStatus.SEND_APPROVED);

                        Transaction transaction = new Transaction();
                        transaction.setCreatedDate(new Date());
                        transaction.setUpdatedDate(new Date());
                        transaction.setTransactionType(TransactionType.SEND_APPROVAL);
                        transaction.setStatus(TransactionStatus.COMPLETE);
                        transaction.setOrder(requestOrder.get());
                        transaction.setPic(context.getUser().get().getUserDetail());
                        transaction.setNote("");
                        requestOrder.get().getTransactions().add(transaction);
                        requestOrder.get().getTransactions().stream().
                                filter(t -> t.getTransactionType().equals(TransactionType.REQ_SEND_APPROVAL))
                                .findAny().ifPresent(t -> t.setStatus(TransactionStatus.COMPLETE));

                        transactionService.save(transaction);
                    }
                    page = ApprovalModule.APPROVAL_PATH;
                    break;
                case "reject":
                    requestOrder.get().setStatus(RequestOrderStatus.REJECTED);
                    page = ApprovalModule.APPROVAL_PATH;
                    break;
            }

            requestOrderService.saveRequestOrder(requestOrder.get());
        }
        return String.format("redirect:%s?", page);
    }

}
