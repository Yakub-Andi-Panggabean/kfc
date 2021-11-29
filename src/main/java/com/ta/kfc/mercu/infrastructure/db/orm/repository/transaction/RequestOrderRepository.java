package com.ta.kfc.mercu.infrastructure.db.orm.repository.transaction;

import com.ta.kfc.mercu.infrastructure.db.orm.model.actor.UserDetail;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrder;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrderStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RequestOrderRepository extends CrudRepository<RequestOrder, Long> {
    Optional<RequestOrder> findByRequesterAndStatus(UserDetail requester, RequestOrderStatus requestOrderStatus);

    List<RequestOrder> findByRequester(UserDetail requester);

}
