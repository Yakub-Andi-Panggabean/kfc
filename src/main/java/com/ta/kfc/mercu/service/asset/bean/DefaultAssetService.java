package com.ta.kfc.mercu.service.asset.bean;

import com.ta.kfc.mercu.infrastructure.db.orm.model.actor.UserDetail;
import com.ta.kfc.mercu.infrastructure.db.orm.model.asset.Asset;
import com.ta.kfc.mercu.infrastructure.db.orm.model.asset.ItemReceipt;
import com.ta.kfc.mercu.infrastructure.db.orm.repository.asset.AssetRepository;
import com.ta.kfc.mercu.infrastructure.db.orm.repository.asset.ItemReceiptRepository;
import com.ta.kfc.mercu.service.asset.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultAssetService implements AssetService {

    private AssetRepository assetRepository;
    private ItemReceiptRepository itemReceiptRepository;

    @Autowired
    public DefaultAssetService(AssetRepository assetRepository, ItemReceiptRepository itemReceiptRepository) {
        this.assetRepository = assetRepository;
        this.itemReceiptRepository = itemReceiptRepository;
    }

    @Override
    public Optional<Asset> getAsset(Long id) {
        return assetRepository.findById(id);
    }

    @Override
    public Optional<Asset> save(Asset asset) {
        return Optional.of(assetRepository.save(asset));
    }

    @Override
    public Optional<Asset> update(Asset asset) {

        if (asset == null) {
            throw new RuntimeException("empty asset id");
        }

        return Optional.of(assetRepository.save(asset));
    }

    @Override
    public List<ItemReceipt> getAllItemReceipt() {
        return null;
    }

    @Override
    public List<ItemReceipt> getItemReceiptByUser(UserDetail userDetail) {
        return null;
    }

    @Override
    public Optional<ItemReceipt> saveItemReceipt(ItemReceipt itemReceipt) {
        return Optional.empty();
    }

    @Override
    public Optional<ItemReceipt> updateItemReceipt(ItemReceipt itemReceipt) {
        return Optional.empty();
    }


}
