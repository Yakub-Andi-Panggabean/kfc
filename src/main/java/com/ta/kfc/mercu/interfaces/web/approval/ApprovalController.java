package com.ta.kfc.mercu.interfaces.web.approval;

import com.ta.kfc.mercu.context.FastContext;
import com.ta.kfc.mercu.infrastructure.db.orm.model.actor.Position;
import com.ta.kfc.mercu.infrastructure.db.orm.model.auth.User;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrder;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrderStatus;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrderType;
import com.ta.kfc.mercu.interfaces.web.approval.ApprovalModule;
import com.ta.kfc.mercu.service.transaction.RequestOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ApprovalController extends ApprovalModule {

    private final FastContext fastContext;
    private final RequestOrderService requestOrderService;

    @Autowired
    public ApprovalController(FastContext fastContext, RequestOrderService requestOrderService) {
        this.fastContext = fastContext;
        this.requestOrderService = requestOrderService;
    }


    @GetMapping({APPROVAL_PATH})
    public String getApprovalPage(Model model) {

        model.addAttribute("template", "approval");

        User user = fastContext.getUser().get();

        Position position = user.getUserDetail().getPosition();
        boolean isRoVisible = false;
        boolean isToVisible = false;

        model.addAttribute("requests", requestOrderService.findAllRequestOrders()
                .stream()
                .filter(ro -> ro.getType() == RequestOrderType.REQUEST_ORDER)
                .filter(ro -> ro.getStatus().equals(RequestOrderStatus.WAITING_APPROVAL) ||
                        ro.getStatus().equals(RequestOrderStatus.WAITING_SEND_APPROVAL))
                .collect(Collectors.toList()));

        model.addAttribute("transferRequests", requestOrderService.findAllRequestOrders()
                .stream()
                .filter(ro -> ro.getType() == RequestOrderType.TRANSFER_ORDER)
                .filter(ro ->
                        ro.getStatus().equals(RequestOrderStatus.WAITING_TRANSFER_APPROVAL))
                .collect(Collectors.toList()));

        switch (position) {
            case HEAD:
                isRoVisible = true;
                isToVisible = true;
                break;
            case MANAGER:
                isRoVisible = true;
                break;
            case ASSET_MANAGER:
                isToVisible = true;
                break;
            case ROOT:
                isRoVisible = true;
                isToVisible = true;
                break;
        }

        model.addAttribute("isRoVisible", isRoVisible);
        model.addAttribute("isToVisible", isToVisible);

        return "index";
    }

}
