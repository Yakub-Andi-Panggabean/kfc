package com.ta.kfc.mercu.interfaces.web.item;

import com.ta.kfc.mercu.context.FastContext;
import com.ta.kfc.mercu.dto.item.ItemShipmentDto;
import com.ta.kfc.mercu.infrastructure.db.orm.model.asset.Asset;
import com.ta.kfc.mercu.infrastructure.db.orm.model.asset.AssetStatus;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.*;
import com.ta.kfc.mercu.service.asset.AssetService;
import com.ta.kfc.mercu.service.transaction.RequestOrderService;
import com.ta.kfc.mercu.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

@Controller
public class ItemProcessorController extends ItemModule {

    private FastContext context;
    private TransactionService transactionService;
    private RequestOrderService requestOrderService;
    private AssetService assetService;

    @Autowired
    public ItemProcessorController(FastContext context,
                                   TransactionService transactionService,
                                   RequestOrderService requestOrderService,
                                   AssetService assetService) {
        this.context = context;
        this.transactionService = transactionService;
        this.requestOrderService = requestOrderService;
        this.assetService = assetService;
    }

    @PostMapping({ITEM_SHIPMENT_PATH})
    public String sendRo(ItemShipmentDto itemShipmentDto) {

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

        itemShipmentDto.getRo().getAssets().stream().forEach(asset -> {
            asset.setAssetStatus(AssetStatus.SEND);
            asset.setUpdatedDate(new Date());
            asset.setUnit(itemShipmentDto.getRo().getFrom());
            assetService.update(asset);
        });

        itemShipmentDto.getRo().setStatus(RequestOrderStatus.IN_PROGRESS);
        requestOrderService.updateRequestOrder(itemShipmentDto.getRo());
        transactionService.save(transaction);

        return String.format("redirect:%s", ITEM_SHIPMENT_PATH);
    }

    @PostMapping(ITEM_SHIPMENT_PATH + "/asset/{action}/{ro_id}/{asset_id}")
    public String updateRequestOrderAsset(
            @PathVariable("action") String action,
            @PathVariable("ro_id") Long roId,
            @PathVariable("asset_id") Long assetId) {

        Optional<RequestOrder> requestOrder = requestOrderService.findRequestOrderById(roId);
        Optional<Asset> asset = assetService.getAsset(assetId);

        if (requestOrder.isPresent() && asset.isPresent()) {
            switch (action) {
                case "add":
                    asset.get().setAssetStatus(AssetStatus.LOCKED);
                    requestOrder.get().getAssets().add(assetService.update(asset.get()).get());
                    requestOrderService.saveRequestOrder(requestOrder.get());
                    break;
                case "remove":
                    asset.get().setAssetStatus(AssetStatus.AVAILABLE);
                    requestOrder.get().getAssets().remove(assetService.update(asset.get()).get());
                    requestOrderService.updateRequestOrder(requestOrder.get());
                    break;
            }
        }
        return String.format("redirect:%s?ro_id=%d", ITEM_SHIPMENT_PATH, roId);
    }


}
