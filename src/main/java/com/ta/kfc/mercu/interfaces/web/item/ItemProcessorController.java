package com.ta.kfc.mercu.interfaces.web.item;

import com.ta.kfc.mercu.context.FastContext;
import com.ta.kfc.mercu.dto.item.AddItemReceipt;
import com.ta.kfc.mercu.dto.item.CreateTransferOrder;
import com.ta.kfc.mercu.dto.item.ItemReceiptProduct;
import com.ta.kfc.mercu.dto.item.ItemShipmentDto;
import com.ta.kfc.mercu.infrastructure.db.orm.model.asset.Asset;
import com.ta.kfc.mercu.infrastructure.db.orm.model.asset.AssetStatus;
import com.ta.kfc.mercu.infrastructure.db.orm.model.asset.ItemReceipt;
import com.ta.kfc.mercu.infrastructure.db.orm.model.asset.ItemReceiptStatus;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Product;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.*;
import com.ta.kfc.mercu.interfaces.web.order.OrderModule;
import com.ta.kfc.mercu.service.asset.AssetService;
import com.ta.kfc.mercu.service.master.MasterService;
import com.ta.kfc.mercu.service.transaction.RequestOrderService;
import com.ta.kfc.mercu.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class ItemProcessorController extends ItemModule {

    private FastContext context;
    private TransactionService transactionService;
    private RequestOrderService requestOrderService;
    private AssetService assetService;
    private MasterService masterService;

    @Autowired
    public ItemProcessorController(FastContext context,
                                   TransactionService transactionService,
                                   MasterService masterService,
                                   RequestOrderService requestOrderService,
                                   AssetService assetService) {
        this.context = context;
        this.transactionService = transactionService;
        this.requestOrderService = requestOrderService;
        this.assetService = assetService;
        this.masterService = masterService;
    }

    @PostMapping({ITEM_SHIPMENT_PATH})
    public String sendRo(ItemShipmentDto itemShipmentDto) {

        Transaction transaction = new Transaction();
        transaction.setCreatedDate(new Date());
        transaction.setUpdatedDate(new Date());
        transaction.setStatus(TransactionStatus.IN_PROGRESS);
        transaction.setOrder(itemShipmentDto.getRo());
        transaction.setPic(context.getUser().get().getUserDetail());
        transaction.setNote(itemShipmentDto.getNote());

        if (itemShipmentDto.getRo().getStatus() == RequestOrderStatus.APPROVED) {
            itemShipmentDto.getRo().setStatus(RequestOrderStatus.WAITING_SEND_APPROVAL);
            transaction.setTransactionType(TransactionType.REQ_SEND_APPROVAL);
            itemShipmentDto.getRo().getAssets().stream().forEach(asset -> {
                asset.setAssetStatus(AssetStatus.LOCKED);
                asset.setUpdatedDate(new Date());
                assetService.update(asset);
            });
        } else {
            itemShipmentDto.getRo().setStatus(RequestOrderStatus.SEND);
            transaction.setTransactionType(TransactionType.SEND_ITEM);
            itemShipmentDto.getRo().getAssets().stream().forEach(asset -> {
                asset.setAssetStatus(AssetStatus.SEND);
                asset.setUpdatedDate(new Date());
                asset.setUnit(itemShipmentDto.getRo().getFrom());
                assetService.update(asset);
            });
        }

        if (itemShipmentDto.getRo().getTransactions() != null) {
            itemShipmentDto.getRo().getTransactions().add(transaction);
        } else {
            itemShipmentDto.getRo().setTransactions(Arrays.asList(transaction));
        }

        transactionService.save(transaction);
        requestOrderService.updateRequestOrder(itemShipmentDto.getRo());
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


    @PostMapping({ITEM_RECEIPT_PATH})
    public String addItemReceipt(
            AddItemReceipt addItemReceipt, Model model) {

        ItemReceipt itemReceipt = new ItemReceipt();
        itemReceipt.setReceiver(context.getUser().get().getUserDetail());
        itemReceipt.setPurchaseOrder(addItemReceipt.getPoNumber());
        itemReceipt.setRequestOrder(addItemReceipt.getRequestOrder());
        itemReceipt.setStatus(ItemReceiptStatus.NEW);
        itemReceipt.setSupplier(addItemReceipt.getSupplier());
        itemReceipt.setLocation(addItemReceipt.getUnit());

        assetService.saveItemReceipt(itemReceipt);

        return String.format("redirect:%s", ITEM_RECEIPT_PATH);
    }

    @PostMapping({ITEM_RECEIPT_PATH + "/add"})
    public String addItemReceiptAsset(
            ItemReceiptProduct itemReceiptProduct, Model model) {

        Optional<ItemReceipt> itemReceipt = assetService.findItemReceiptById(itemReceiptProduct.getItemReceipt());
        if (itemReceipt.isPresent()) {

            Optional<Product> product = masterService.getProduct(itemReceiptProduct.getProduct());

            Asset asset = new Asset();
            asset.setUpdatedDate(new Date());
            asset.setCreatedDate(new Date());
            asset.setUnit(itemReceipt.get().getRequestOrder().getTo());
            asset.setProduct(product.get());
            asset.setAssetStatus(AssetStatus.AVAILABLE);
            asset.setCode(UUID.randomUUID().toString());
            Optional<Asset> savedAsset = assetService.save(asset);
            itemReceipt.get().getAssets().add(savedAsset.get());

            assetService.updateItemReceipt(itemReceipt.get());
        }

        return String.format("redirect:%s", ITEM_RECEIPT_PATH);
    }


    @PostMapping({ITEM_RECEIPT_PATH + "/remove"})
    public String addItemReceiptAsset(
            @RequestParam(name = "receiptId")
                    Long receiptId, @RequestParam(name = "assetId") Long assetId) {

        Optional<ItemReceipt> itemReceipt = assetService.findItemReceiptById(receiptId);
        if (itemReceipt.isPresent()) {

            Optional<Asset> asset = assetService.findById(assetId);

            if (asset.isPresent()) {
                itemReceipt.get().getAssets().removeIf(as -> as.getId() == asset.get().getId());
                assetService.updateItemReceipt(itemReceipt.get());
                assetService.delete(asset.get());
            }

        }

        return String.format("redirect:%s", ITEM_RECEIPT_PATH);
    }

    @PostMapping({ITEM_RECEIPT_PATH + "/{state}/{id}"})
    public String addItemReceiptAsset(@PathVariable(name = "state") String state,
                                      @PathVariable(name = "id") Long receiptId) {

        Optional<ItemReceipt> itemReceipt = assetService.findItemReceiptById(receiptId);

        String page = ITEM_RECEIPT_PATH;
        if (itemReceipt.isPresent()) {

            switch (state) {
                case "done":
                    itemReceipt.get().setStatus(ItemReceiptStatus.VERIFIED);
                    break;
                case "complete":
                    itemReceipt.get().setStatus(ItemReceiptStatus.COMPLETED);
                    page = OrderModule.ORDER_PURCHASE_PATH;
                    break;
            }

            assetService.updateItemReceipt(itemReceipt.get());
        }

        return String.format("redirect:%s", page);
    }


    @PostMapping({ITEM_TRANSFER_PATH})
    public String createTransferOrder(CreateTransferOrder req) {

        RequestOrder order = new RequestOrder();
        order.setFrom(req.getFrom());
        order.setTo(req.getTo());
        order.setStatus(RequestOrderStatus.NEW);
        order.setRequester(context.getUser().get().getUserDetail());
        order.setType(RequestOrderType.TRANSFER_ORDER);
        order.setUpdatedDate(new Date());
        order.setCreatedDate(new Date());

        requestOrderService.saveRequestOrder(order);

        return String.format("redirect:%s", ITEM_TRANSFER_PATH);
    }

    @PostMapping({ITEM_TRANSFER_PATH + "/{action}/{toId}/{assetId}"})
    public String addTransferOrderItem(@PathVariable("action") String action,
                                       @PathVariable("toId") Long transferOrderId,
                                       @PathVariable("assetId") Long assetId) {

        Optional<RequestOrder> order = requestOrderService.findRequestOrderById(transferOrderId);

        if (order.isPresent()) {

            Optional<Asset> asset = assetService.findById(assetId);

            if (asset.isPresent()) {
                switch (action) {
                    case "add":
                        order.get().getAssets().add(asset.get());
                        break;
                    case "remove":
                        order.get().getAssets().removeIf(a -> asset.get().getId() == a.getId());
                        break;
                }
                requestOrderService.saveRequestOrder(order.get());
            }
        }

        return String.format("redirect:%s", ITEM_TRANSFER_PATH);
    }


}
