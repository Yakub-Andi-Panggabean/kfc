package com.ta.kfc.mercu.service.asset;

import com.ta.kfc.mercu.infrastructure.db.orm.model.asset.Asset;

import java.util.Optional;

public interface AssetService {
    Optional<Asset> getAsset(Long id);

    Optional<Asset> save(Asset asset);

    Optional<Asset> update(Asset asset);
}
