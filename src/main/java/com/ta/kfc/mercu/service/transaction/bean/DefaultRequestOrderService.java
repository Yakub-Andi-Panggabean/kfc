package com.ta.kfc.mercu.service.transaction.bean;

import com.ta.kfc.mercu.infrastructure.db.orm.model.actor.UserDetail;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrder;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrderStatus;
import com.ta.kfc.mercu.infrastructure.db.orm.repository.transaction.RequestOrderRepository;
import com.ta.kfc.mercu.service.transaction.RequestOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultRequestOrderService implements RequestOrderService {

    private final RequestOrderRepository requestOrderRepository;

    @Autowired
    public DefaultRequestOrderService(RequestOrderRepository requestOrderRepository) {
        this.requestOrderRepository = requestOrderRepository;
    }

    @Override
    public Optional<RequestOrder> findInProgressRequestOrder(UserDetail requester) {
        return requestOrderRepository.findByRequesterAndStatus(requester, RequestOrderStatus.NEW);
    }

    @Override
    public Optional<RequestOrder> findRequestOrderById(Long id) {
        return requestOrderRepository.findById(id);
    }

    @Override
    public Optional<RequestOrder> saveRequestOrder(RequestOrder requestOrder) {
        return Optional.of(requestOrderRepository.save(requestOrder));
    }

    @Override
    public Optional<RequestOrder> updateRequestOrder(RequestOrder requestOrder) {
        if (requestOrder.getId() == null) {
            throw new RuntimeException("request order is not exist");
        }
        return Optional.of(requestOrderRepository.save(requestOrder));
    }

    @Override
    public List<RequestOrder> findRequestOrderPerUser(UserDetail userDetail) {
        return requestOrderRepository.findByRequester(userDetail);
    }
}
