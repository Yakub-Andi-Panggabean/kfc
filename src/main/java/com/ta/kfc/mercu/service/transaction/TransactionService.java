package com.ta.kfc.mercu.service.transaction;

import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrder;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.Transaction;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.TransactionStatus;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.TransactionType;

import java.util.List;
import java.util.Optional;

public interface TransactionService {
    List<Transaction> findByRo(RequestOrder requestOrder);

    List<Transaction> findByStatus(TransactionStatus transactionStatus);

    List<Transaction> findByType(TransactionType transactionType);


    Optional<Transaction> save(Transaction transaction);

    Optional<Transaction> update(Transaction transaction);
}
