package com.ta.kfc.mercu.service.transaction;

import com.ta.kfc.mercu.infrastructure.db.orm.model.actor.UserDetail;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.PurchaseOrder;

import java.util.List;
import java.util.Optional;

public interface PurchaseOrderService {

    Optional<PurchaseOrder> createNewPO(UserDetail userDetail);

    Optional<PurchaseOrder> updatePO(PurchaseOrder purchaseOrder);

    List<PurchaseOrder> getByUser(UserDetail userDetail);

    List<PurchaseOrder> getAll();

}
