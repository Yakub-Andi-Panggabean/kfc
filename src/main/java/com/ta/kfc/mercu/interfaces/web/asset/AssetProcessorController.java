package com.ta.kfc.mercu.interfaces.web.asset;


import com.ta.kfc.mercu.context.FastContext;
import com.ta.kfc.mercu.infrastructure.db.orm.model.asset.AssetStatus;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrder;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrderStatus;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.TransactionStatus;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.TransactionType;
import com.ta.kfc.mercu.service.asset.AssetService;
import com.ta.kfc.mercu.service.transaction.RequestOrderService;
import com.ta.kfc.mercu.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class AssetProcessorController extends AssetModule {

    private RequestOrderService requestOrderService;
    private TransactionService transactionService;
    private AssetService assetService;
    private FastContext context;

    @Autowired
    public AssetProcessorController(RequestOrderService requestOrderService,
                                    TransactionService transactionService,
                                    AssetService assetService,
                                    FastContext context) {
        this.requestOrderService = requestOrderService;
        this.transactionService = transactionService;
        this.assetService = assetService;
        this.context = context;
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

            ro.get().setStatus(RequestOrderStatus.COMPLETED);
            requestOrderService.saveRequestOrder(ro.get());
        }

        return String.format("redirect:%s", ASSET_VERIFICATION_PATH);
    }

}
