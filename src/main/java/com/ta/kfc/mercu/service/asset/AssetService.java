package com.ta.kfc.mercu.service.asset;

import com.ta.kfc.mercu.infrastructure.db.orm.model.actor.UserDetail;
import com.ta.kfc.mercu.infrastructure.db.orm.model.asset.Asset;
import com.ta.kfc.mercu.infrastructure.db.orm.model.asset.ItemReceipt;

import java.util.List;
import java.util.Optional;

public interface AssetService {
    Optional<Asset> getAsset(Long id);

    Optional<Asset> save(Asset asset);

    Optional<Asset> update(Asset asset);

    void delete(Asset asset);

    Optional<Asset> findById(Long id);

    Optional<Asset> findByCode(String code);

    List<Asset> getAllAsset();

    List<ItemReceipt> getAllItemReceipt();

    List<ItemReceipt> getItemReceiptByUser(UserDetail userDetail);

    Optional<ItemReceipt> saveItemReceipt(ItemReceipt itemReceipt);

    Optional<ItemReceipt> updateItemReceipt(ItemReceipt itemReceipt);

    Optional<ItemReceipt> findItemReceiptById(Long id);
}
