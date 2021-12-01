package com.ta.kfc.mercu.service.transaction;

import com.ta.kfc.mercu.infrastructure.db.orm.model.actor.UserDetail;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrder;

import java.util.List;
import java.util.Optional;

public interface RequestOrderService {
    Optional<RequestOrder> findInProgressRequestOrder(UserDetail requester);

    Optional<RequestOrder> findRequestOrderById(Long id);

    Optional<RequestOrder> saveRequestOrder(RequestOrder requestOrder);

    Optional<RequestOrder> updateRequestOrder(RequestOrder requestOrder);

    List<RequestOrder> findRequestOrderPerUser(UserDetail userDetail);

    List<RequestOrder> findAllRequestOrders();

}
