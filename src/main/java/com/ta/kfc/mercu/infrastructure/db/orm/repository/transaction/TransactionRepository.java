package com.ta.kfc.mercu.infrastructure.db.orm.repository.transaction;

import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrder;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.Transaction;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.TransactionStatus;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.TransactionType;
import org.springframework.data.repository.CrudRepository;
;
import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    List<Transaction> findByOrder(RequestOrder requestOrder);

    List<Transaction> findByStatus(TransactionStatus transactionStatus);

    List<Transaction> findByTransactionType(TransactionType type);

}
