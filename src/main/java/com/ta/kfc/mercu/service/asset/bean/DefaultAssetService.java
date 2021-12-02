package com.ta.kfc.mercu.service.asset.bean;

import com.ta.kfc.mercu.infrastructure.db.orm.model.asset.Asset;
import com.ta.kfc.mercu.infrastructure.db.orm.repository.asset.AssetRepository;
import com.ta.kfc.mercu.service.asset.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DefaultAssetService implements AssetService {

    private AssetRepository assetRepository;

    @Autowired
    public DefaultAssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
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
}
