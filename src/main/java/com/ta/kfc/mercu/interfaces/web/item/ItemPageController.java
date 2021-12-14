package com.ta.kfc.mercu.interfaces.web.item;

import com.ta.kfc.mercu.context.FastContext;
import com.ta.kfc.mercu.dto.item.*;
import com.ta.kfc.mercu.infrastructure.db.orm.model.asset.Asset;
import com.ta.kfc.mercu.infrastructure.db.orm.model.asset.AssetStatus;
import com.ta.kfc.mercu.infrastructure.db.orm.model.asset.ItemReceipt;
import com.ta.kfc.mercu.infrastructure.db.orm.model.asset.ItemReceiptStatus;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Product;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Supplier;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Unit;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.UnitType;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.*;
import com.ta.kfc.mercu.service.asset.AssetService;
import com.ta.kfc.mercu.service.master.MasterService;
import com.ta.kfc.mercu.service.security.AuthorizationService;
import com.ta.kfc.mercu.service.transaction.RequestOrderService;
import com.ta.kfc.mercu.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ItemPageController extends ItemModule {

    private AuthorizationService authorizationService;
    private RequestOrderService requestOrderService;
    private MasterService masterService;
    private FastContext context;
    private TransactionService transactionService;
    private AssetService assetService;

    @Autowired
    public ItemPageController(FastContext context,
                              AssetService assetService,
                              AuthorizationService authorizationService,
                              RequestOrderService requestOrderService,
                              MasterService masterService,
                              TransactionService transactionService) {
        this.authorizationService = authorizationService;
        this.requestOrderService = requestOrderService;
        this.masterService = masterService;
        this.transactionService = transactionService;
        this.assetService = assetService;
        this.context = context;
    }


    @GetMapping({ITEM_RECEIPT_PATH})
    public String getItemReceiptPage(@RequestParam(value = "isSupplierSelected", required = false) boolean isSupplierSelected,
                                     @RequestParam(value = "supplierId", required = false) Long supplierId,
                                     @RequestParam(value = "isRoSelected", required = false) boolean isRoSelected,
                                     @RequestParam(value = "roId", required = false) Long roId,
                                     Model model) {

        model.addAttribute("template", "item_receipt");

        model.addAttribute("suppliers", masterService.findAllSuppliers());
        model.addAttribute("orders", requestOrderService.findAllRequestOrders()
                .stream().filter(o -> o.getStatus() == RequestOrderStatus.APPROVED)
                .collect(Collectors.toList()));
        model.addAttribute("units", masterService.getAllUnit().stream()
                .filter(unit -> unit.getUnitType() == UnitType.WAREHOUSE)
                .collect(Collectors.toList()));


        List<ItemReceipt> existingItemReceipt =
                assetService.getItemReceiptByUser(context.getUser().get().getUserDetail());

        Optional<ItemReceipt> itemReceipt = existingItemReceipt
                .stream()
                .filter(item -> item.getStatus() == ItemReceiptStatus.NEW || item.getStatus() == ItemReceiptStatus.PROCESSING)
                .findAny();

        model.addAttribute("isReceiptExist", itemReceipt.isPresent());
        model.addAttribute("itemReceiptProductModel", new ItemReceiptProduct());

        if (itemReceipt.isPresent()) {
            model.addAttribute("existingItemReceipt", itemReceipt.get());
            model.addAttribute("products", itemReceipt.get().getSupplier().getProducts());
            model.addAttribute("isItemContainAsset", itemReceipt.get().getAssets().size() > 0);
        } else {
            model.addAttribute("products", Collections.emptyList());
        }

        model.addAttribute("isSupplierSelected", isSupplierSelected);
        model.addAttribute("isRoSelected", isRoSelected);
        model.addAttribute("roId", roId);
        model.addAttribute("supplierId", supplierId);

        AddItemReceipt addItemReceipt = new AddItemReceipt();

        if (isSupplierSelected) {

            Optional<Supplier> supplier = masterService.findSupplier(supplierId);
            if (supplier.isPresent()) {
                addItemReceipt.setSupplier(supplier.get());
                model.addAttribute("supplier", supplier.get());

                if (isRoSelected) {
                    Optional<RequestOrder> requestOrder = requestOrderService.findRequestOrderById(roId);
                    if (requestOrder.isPresent()) {
                        addItemReceipt.setRequestOrder(requestOrder.get());
                        model.addAttribute("ro", requestOrder.get());
                    }
                }
            }
        }

        model.addAttribute("addItemReceiptModel", addItemReceipt);


        return "index";
    }


    @GetMapping({ITEM_SHIPMENT_PATH})
    public String getItemShipmentPage(@RequestParam(value = "ro_id", required = false) Long roId,
                                      Model model) {

        ItemShipmentDto itemShipmentDto = new ItemShipmentDto();
        model.addAttribute("template", "item_shipment");

        List<Transaction> transactions = transactionService.findByType(TransactionType.SEND_ITEM);
        transactions.addAll(transactionService.findByType(TransactionType.REQ_SEND_APPROVAL));
        transactions.addAll(transactionService.findByType(TransactionType.SEND_APPROVAL));
        model.addAttribute("transactions", transactions);

        model.addAttribute("units", masterService.getAllUnit().stream()
                .filter(u -> u.getUnitType().equals(UnitType.WAREHOUSE))
                .collect(Collectors.toList()));
        model.addAttribute("orders", requestOrderService.findAllRequestOrders()
                .stream()
                .filter(ro -> ro.getType().equals(RequestOrderType.REQUEST_ORDER))
                .filter(ro -> ro.getStatus().equals(RequestOrderStatus.APPROVED)
                        || ro.getStatus().equals(RequestOrderStatus.SEND_APPROVED))
                .collect(Collectors.toList()));
        if (roId != null) {
            model.addAttribute("ro_id", roId);
            RequestOrder requestOrder = requestOrderService.findRequestOrderById(roId).get();
            model.addAttribute("isRoEligible", requestOrder.getProducts()
                    .size() > 0
                    && requestOrder
                    .getProducts().size() == requestOrder.getAssets().size());
            List<Asset> availableAssets = requestOrder.getAssets();
            itemShipmentDto.setRo(requestOrder);
            model.addAttribute("isRoApproved", requestOrder.getStatus() == RequestOrderStatus.SEND_APPROVED);
            model.addAttribute("itemShipmentDto", itemShipmentDto);
            model.addAttribute("roSelected", true);
            model.addAttribute("roItems", requestOrder.getProducts()
                    .stream()
                    .map(p -> {
                        Optional<Asset> matchAsset = availableAssets
                                .stream()
                                .filter(asset -> asset.getProduct().getId() == p.getId())
                                .findAny();

                        if (matchAsset.isPresent()) {
                            matchAsset.get().setAssetStatus(AssetStatus.LOCKED);
                            availableAssets.remove(matchAsset.get());
                            return new RoProductAsset(p, matchAsset.get());
                        }

                        return new RoProductAsset(p, null);
                    }).collect(Collectors.toList()));


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
            model.addAttribute("roItems", Collections.emptyList());
            model.addAttribute("isRoEligible", false);
        }

        return "index";
    }


    @GetMapping({ITEM_TRANSFER_PATH})
    public String getItemTransferPage(Model model) {

        model.addAttribute("template", "item_transfer");
        model.addAttribute("units", masterService.getAllUnit());
        model.addAttribute("inProgressOrders", requestOrderService
                .findRequestOrderPerUser(context.getUser().get().getUserDetail())
                .stream()
                .filter(o -> o.getType() == RequestOrderType.TRANSFER_ORDER)
                .filter(o -> o.getStatus() != RequestOrderStatus.NEW)
                .collect(Collectors.toList()));

        Optional<RequestOrder> order = requestOrderService
                .findRequestOrderPerUser(context.getUser().get().getUserDetail())
                .stream()
                .filter(o -> o.getType() == RequestOrderType.TRANSFER_ORDER)
                .filter(o -> o.getStatus() == RequestOrderStatus.NEW)
                .findAny();

        boolean isAbleToSubmit = false;

        CreateTransferOrder createTransferOrder = new CreateTransferOrder();
        SubmitTransferOrder submitTransferOrder = new SubmitTransferOrder();
        model.addAttribute("isOrderExist", order.isPresent());
        if (order.isPresent()) {
            model.addAttribute("existingOrder", order.get());
            model.addAttribute("assets", order.get()
                    .getFrom()
                    .getAssets()
                    .stream().filter(a -> a.getAssetStatus() == AssetStatus.AVAILABLE || a.getAssetStatus() == AssetStatus.IN_USED)
                    .filter(a -> !order.get().getAssets().contains(a))
                    .collect(Collectors.toList()));
            createTransferOrder.setTo(order.get().getTo());
            createTransferOrder.setFrom(order.get().getFrom());
            submitTransferOrder.setStatus(RequestOrderStatus.WAITING_APPROVAL);
            submitTransferOrder.setRequestOrder(order.get());
            isAbleToSubmit = order.get().getAssets().size() > 0;
        }
        model.addAttribute("createTransferModel", createTransferOrder);
        model.addAttribute("submitTransferModel", submitTransferOrder);
        model.addAttribute("isAbleToSubmit", isAbleToSubmit);

        return "index";
    }


    @GetMapping({ITEM_SHIPMENT_PATH + "/{ro_id}"})
    public String getItemShipmentModal(
            @PathVariable(value = "ro_id") Long roId,
            @RequestParam(value = "product_id", required = false) Long productId,
            @RequestParam(value = "unit_id", required = false) Long unitId,
            Model model) {

        Optional<Unit> unit = masterService.getUnit(unitId);

        model.addAttribute("ro_id", roId);
        model.addAttribute("addAssetDto", new AddAssetDto());

        if (unit.isPresent()) {

            model.addAttribute("assets", unit.get().getAssets()
                    .stream()
                    .filter(asset -> asset.getProduct().getId() == productId)
                    .filter(asset -> asset.getAssetStatus().equals(AssetStatus.AVAILABLE))
                    .collect(Collectors.toList()));
        }

        return "fragments/item_shipment/modal_asset";
    }

    @GetMapping({"/modal/item/transfer/{transferId}"})
    public String getItemTransferModal(@PathVariable("transferId") Long transferID, Model model) {

        Optional<RequestOrder> ro = requestOrderService.findRequestOrderById(transferID);

        if (ro.isPresent()) {
            model.addAttribute("transactions", ro.get().getTransactions());
        } else {
            model.addAttribute("transactions", Collections.emptyList());
        }

        return "fragments/item_transfer/modal_transfer_detail";
    }

}
