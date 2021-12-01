package com.ta.kfc.mercu.service.transaction;

import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrder;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionService {
    List<Transaction> findByRo(RequestOrder requestOrder);

    Optional<Transaction> save(Transaction transaction);

    Optional<Transaction> update(Transaction transaction);
}
