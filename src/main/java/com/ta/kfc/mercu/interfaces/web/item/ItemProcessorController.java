package com.ta.kfc.mercu.interfaces.web.item;

import com.ta.kfc.mercu.context.FastContext;
import com.ta.kfc.mercu.dto.item.ItemShipmentDto;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.Transaction;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.TransactionStatus;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.TransactionType;
import com.ta.kfc.mercu.service.transaction.RequestOrderService;
import com.ta.kfc.mercu.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

@Controller
public class ItemProcessorController extends ItemModule {

    private FastContext context;
    private TransactionService transactionService;

    @Autowired
    public ItemProcessorController(FastContext context, TransactionService transactionService) {
        this.context = context;
        this.transactionService = transactionService;
    }

    @PostMapping({ITEM_SHIPMENT_PATH})
    public String getItemShipmentPage(ItemShipmentDto itemShipmentDto) {

        Transaction transaction = new Transaction();
        transaction.setCreatedDate(new Date());
        transaction.setUpdatedDate(new Date());
        transaction.setTransactionType(TransactionType.SEND_ITEM);
        transaction.setStatus(TransactionStatus.IN_PROGRESS);
        transaction.setOrder(itemShipmentDto.getRo());
        transaction.setPic(context.getUser().get().getUserDetail());
        transaction.setNote(itemShipmentDto.getNote());


        if (itemShipmentDto.getRo().getTransactions() != null) {
            itemShipmentDto.getRo().getTransactions().add(transaction);
        } else {
            itemShipmentDto.getRo().setTransactions(Arrays.asList(transaction));
        }

        transactionService.save(transaction);

        return String.format("redirect:%s", ITEM_SHIPMENT_PATH);
    }


}
