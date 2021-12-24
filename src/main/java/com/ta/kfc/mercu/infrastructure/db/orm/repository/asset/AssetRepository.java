package com.ta.kfc.mercu.infrastructure.db.orm.repository.asset;

import com.ta.kfc.mercu.infrastructure.db.orm.model.asset.Asset;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AssetRepository extends CrudRepository<Asset, Long> {
    List<Asset> findAll();

    Optional<Asset> findByCode(String code);
}
