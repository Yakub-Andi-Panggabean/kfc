package com.ta.kfc.mercu.service.transaction.bean;

import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrder;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.Transaction;
import com.ta.kfc.mercu.infrastructure.db.orm.repository.transaction.RequestOrderRepository;
import com.ta.kfc.mercu.infrastructure.db.orm.repository.transaction.TransactionRepository;
import com.ta.kfc.mercu.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultTransactionService implements TransactionService {

    private TransactionRepository transactionRepository;
    private RequestOrderRepository requestOrderRepository;

    @Autowired
    public DefaultTransactionService(TransactionRepository transactionRepository, RequestOrderRepository requestOrderRepository) {
        this.transactionRepository = transactionRepository;
        this.requestOrderRepository = requestOrderRepository;
    }

    @Override
    public List<Transaction> findByRo(RequestOrder requestOrder) {
        return transactionRepository.findByOrder(requestOrder);
    }

    @Override
    public Optional<Transaction> save(Transaction transaction) {
        return Optional.of(transactionRepository.save(transaction));
    }

    @Override
    public Optional<Transaction> update(Transaction transaction) {
        if (transaction.getId() == null) {
            throw new RuntimeException("failed to update trx due to null id");
        }
        return Optional.of(transactionRepository.save(transaction));
    }
}
