package com.ta.kfc.mercu.interfaces.web.order;

import com.ta.kfc.mercu.infrastructure.db.orm.model.auth.User;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Brand;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrder;
import com.ta.kfc.mercu.service.master.MasterService;
import com.ta.kfc.mercu.service.security.AuthorizationService;
import com.ta.kfc.mercu.service.transaction.RequestOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

@Controller
public class RequestOrderProcessorController extends OrderModule {

    private AuthorizationService authorizationService;
    private MasterService masterService;
    private RequestOrderService requestOrderService;

    @Autowired
    public RequestOrderProcessorController(AuthorizationService authorizationService,
                                           MasterService masterService, RequestOrderService requestOrderService) {
        this.authorizationService = authorizationService;
        this.masterService = masterService;
        this.requestOrderService = requestOrderService;
    }

    @PostMapping(REQUEST_ORDER_PATH)
    public String createRequestOrder(Model model,
                                     RequestOrder requestOrder,
                                     RedirectAttributes attr) {

        requestOrder.setCreatedDate(new Date());
        requestOrder.setUpdatedDate(new Date());


        return String.format("redirect:%s?", REQUEST_ORDER_PATH);
    }

}
