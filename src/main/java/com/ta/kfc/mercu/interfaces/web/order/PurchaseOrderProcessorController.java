package com.ta.kfc.mercu.interfaces.web.order;

import com.ta.kfc.mercu.service.transaction.PurchaseOrderService;
import org.springframework.stereotype.Controller;

@Controller
public class PurchaseOrderProcessorController extends OrderModule{

    private PurchaseOrderService purchaseOrderService;


}
