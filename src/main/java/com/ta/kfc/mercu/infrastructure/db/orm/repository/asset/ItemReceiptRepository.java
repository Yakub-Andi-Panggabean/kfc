package com.ta.kfc.mercu.infrastructure.db.orm.repository.asset;

import com.ta.kfc.mercu.infrastructure.db.orm.model.actor.UserDetail;
import com.ta.kfc.mercu.infrastructure.db.orm.model.asset.ItemReceipt;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ItemReceiptRepository extends CrudRepository<ItemReceipt, Long> {

    List<ItemReceipt> findAll();

    List<ItemReceipt> findAllByReceiver(UserDetail userDetail);

}
