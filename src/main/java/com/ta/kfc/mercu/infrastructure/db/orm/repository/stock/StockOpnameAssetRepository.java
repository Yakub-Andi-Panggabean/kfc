package com.ta.kfc.mercu.infrastructure.db.orm.repository.stock;

import com.ta.kfc.mercu.infrastructure.db.orm.model.stock.StockOpnameAsset;
import org.springframework.data.repository.CrudRepository;

public interface StockOpnameAssetRepository extends CrudRepository<StockOpnameAsset, Long> {
}
