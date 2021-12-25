package com.ta.kfc.mercu.interfaces.web.asset;


import com.ta.kfc.mercu.context.FastContext;
import com.ta.kfc.mercu.infrastructure.db.orm.model.actor.UserDetail;
import com.ta.kfc.mercu.infrastructure.db.orm.model.asset.Asset;
import com.ta.kfc.mercu.infrastructure.db.orm.model.asset.AssetStatus;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Product;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Unit;
import com.ta.kfc.mercu.infrastructure.db.orm.model.notification.Notification;
import com.ta.kfc.mercu.infrastructure.db.orm.model.stock.*;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrder;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrderStatus;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.TransactionStatus;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.TransactionType;
import com.ta.kfc.mercu.service.asset.AssetService;
import com.ta.kfc.mercu.service.master.MasterService;
import com.ta.kfc.mercu.service.notification.NotificationService;
import com.ta.kfc.mercu.service.stock.StockOpnameService;
import com.ta.kfc.mercu.service.transaction.RequestOrderService;
import com.ta.kfc.mercu.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class AssetProcessorController extends AssetModule {

    public static final String VERIFICATION_NOTIFICATION = "send order with id %d has been verified by %s";

    private RequestOrderService requestOrderService;
    private TransactionService transactionService;
    private StockOpnameService stockOpnameService;
    private AssetService assetService;
    private MasterService masterService;
    private NotificationService notificationService;
    private FastContext context;

    @Autowired
    public AssetProcessorController(RequestOrderService requestOrderService,
                                    TransactionService transactionService,
                                    AssetService assetService,
                                    StockOpnameService stockOpnameService,
                                    MasterService masterService,
                                    NotificationService notificationService,
                                    FastContext context) {
        this.requestOrderService = requestOrderService;
        this.transactionService = transactionService;
        this.assetService = assetService;
        this.masterService = masterService;
        this.stockOpnameService = stockOpnameService;
        this.context = context;
        this.notificationService = notificationService;
    }

    @PostMapping({ASSET_VERIFICATION_PATH + "/{roId}"})
    public String verifySendStatus(@PathVariable("roId") Long id) {

        Optional<RequestOrder> ro = requestOrderService.findRequestOrderById(id);

        if (ro.isPresent()) {
            ro.get().getTransactions()
                    .stream()
                    .filter(trx -> trx.getTransactionType() == TransactionType.SEND_ITEM)
                    .findAny().ifPresent(trx -> {
                        trx.setStatus(TransactionStatus.COMPLETE);
                        transactionService.update(trx);
                    });

            ro.get().getAssets().stream().forEach(a -> {
                a.setAssetStatus(AssetStatus.IN_USED);
                assetService.update(a);
            });

            Optional<UserDetail> senderPic = ro.get().getTransactions()
                    .stream()
                    .filter(o -> o.getTransactionType() == TransactionType.SEND_ITEM)
                    .map(o -> o.getPic()).findAny();

            if (senderPic.isPresent()) {
                Notification notification = new Notification();
                notification.setCreatedDate(new Date());
                notification.setMessage(String.format(VERIFICATION_NOTIFICATION,
                        ro.get().getId(), context.getUser().get().getUserDetail().getFirstName()));
                notification.setUserDetail(senderPic.get());
                notification.setOrder(ro.get());

                notificationService.save(notification);

            }

            ro.get().setStatus(RequestOrderStatus.COMPLETED);
            requestOrderService.saveRequestOrder(ro.get());
        }

        return String.format("redirect:%s", ASSET_VERIFICATION_PATH);
    }


    @PostMapping({ASSET_ADJUSTMENT_PATH + "/{unitId}"})
    public String createStockOpname(@PathVariable("unitId") Long unitId) {

        Optional<Unit> unit = masterService.getUnit(unitId);

        if (!unit.isPresent()) {
            return String.format("redirect:%s", ASSET_ADJUSTMENT_PATH);
        }
        StockOpname stockOpname = new StockOpname();
        stockOpname.setStatus(SoStatus.NEW);
        stockOpname.setCreatedBy(context.getUser().map(u -> u.getUserDetail()).get());
        stockOpname.setUnit(unit.get());
        stockOpname.setCreatedDate(new Date());
        stockOpnameService.save(stockOpname);

        Map<Product, List<Asset>> results = unit.get()
                .getAssets()
                .stream()
                .collect(Collectors.groupingBy(Asset::getProduct, Collectors.toList()));

        List<StockOpnameDetail> stockOpnameDetails = new ArrayList<>();
        results.forEach((key, val) -> {
            StockOpnameDetail soDetail = new StockOpnameDetail();
            soDetail.setQty(Long.valueOf(val.size()));
            soDetail.setProduct(key);
            stockOpnameService.save(soDetail);

            List<StockOpnameAsset> assets = val.stream().map(ast -> {
                StockOpnameAsset asset = new StockOpnameAsset();
                asset.setAsset(ast);
                asset.setStockOpnameDetail(soDetail);
                asset.setStatus(SOAssetStatus.OK);
                return stockOpnameService.save(asset).get();
            }).collect(Collectors.toList());

            soDetail.setAssets(assets);
            soDetail.setStockOpname(stockOpname);
            stockOpnameDetails.add(stockOpnameService.save(soDetail).get());
        });

        stockOpname.setDetails(stockOpnameDetails);
        stockOpnameService.save(stockOpname);

        return String.format("redirect:%s?unitId=%d", ASSET_ADJUSTMENT_PATH, unit.get().getId());
    }


    @PostMapping({ASSET_DETAIL_ADJUSTMENT_PATH + "/{detailId}/{soAssetId}"})
    public String addStockOpnameAsset(@PathVariable("detailId") Long detailId,
                                      @PathVariable("soAssetId") Long soAssetId,
                                      @RequestParam("action") String action) {

        SOAssetStatus status = SOAssetStatus.NOT_OK;
        if (action.equals("ok")) {
            status = SOAssetStatus.NOT_OK;
        }

        Optional<StockOpnameDetail> stockOpname = stockOpnameService.findDetail(detailId);

        if (stockOpname.isPresent()) {

            Optional<StockOpnameAsset> asset = stockOpname.get()
                    .getAssets()
                    .stream()
                    .filter(ast -> ast.getId() == soAssetId).findAny();

            if (asset.isPresent()) {
                asset.get().setStatus(status);
                if (status == SOAssetStatus.NOT_OK) {
                    asset.get().getAsset().setAssetStatus(AssetStatus.NOT_FOUND);
                } else {
                    asset.get().getAsset().setAssetStatus(AssetStatus.IN_USED);
                }
                assetService.save(asset.get().getAsset());
                stockOpnameService.save(asset.get());
            }

        }
        return String.format("redirect:%s/%d/%d?so_id=%d", ASSET_ADJUSTMENT_PATH,
                stockOpname.get().getStockOpname().getUnit().getId(),
                stockOpname.get().getProduct().getId(),
                stockOpname.get().getStockOpname().getId());
    }

    @PostMapping({ASSET_DETAIL_ADJUSTMENT_PATH + "/complete"})
    public String completeStockOpnameDetail(@RequestParam("detailId") Long detailId) {

        Optional<StockOpnameDetail> stockOpnameDetail = stockOpnameService.findDetail(detailId);

        Long missingAssetCount = stockOpnameDetail.get()
                .getAssets()
                .stream()
                .filter(ast -> ast.getStatus() == SOAssetStatus.NOT_OK).count();

        Long qtyResult = stockOpnameDetail.get().getQty() - missingAssetCount;
        stockOpnameDetail.get().setQtyAdjustment(qtyResult);
        stockOpnameDetail.get().setComplete(true);
        stockOpnameService.save(stockOpnameDetail.get());

        return String.format("redirect:%s/%d/%d?so_id=%d", ASSET_ADJUSTMENT_PATH,
                stockOpnameDetail.get().getStockOpname().getUnit().getId(),
                stockOpnameDetail.get().getProduct().getId(),
                stockOpnameDetail.get().getStockOpname().getId());
    }


    @PostMapping({ASSET_ADJUSTMENT_PATH + "/complete"})
    public String completeStockOpname(@RequestParam("soId") Long soId) {
        Optional<StockOpname> stockOpname = stockOpnameService.find(soId);

        if (stockOpname.isPresent()) {
            stockOpname.get().setStatus(SoStatus.COMPLETED);
            stockOpname.get().setUpdatedDate(new Date());
            stockOpnameService.save(stockOpname.get());
        }
        return String.format("redirect:%s?unitId=%d", ASSET_ADJUSTMENT_PATH, stockOpname.get().getUnit().getId());
    }
}
