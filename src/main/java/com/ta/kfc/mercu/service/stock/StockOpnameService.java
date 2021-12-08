package com.ta.kfc.mercu.service.stock;

import com.ta.kfc.mercu.infrastructure.db.orm.model.actor.UserDetail;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Unit;
import com.ta.kfc.mercu.infrastructure.db.orm.model.stock.StockOpname;
import com.ta.kfc.mercu.infrastructure.db.orm.model.stock.StockOpnameAsset;
import com.ta.kfc.mercu.infrastructure.db.orm.model.stock.StockOpnameDetail;

import java.util.List;
import java.util.Optional;

public interface StockOpnameService {

    Optional<StockOpname> findLatestStockOpname(Unit unit, UserDetail userDetail);

    List<StockOpname> findByUnit(Unit unit);

    Optional<StockOpname> save(StockOpname stockOpname);

    Optional<StockOpname> update(StockOpname stockOpname);

    Optional<StockOpname> find(Long id);

    Optional<StockOpnameDetail> save(StockOpnameDetail stockOpnameDetail);

    Optional<StockOpnameAsset> save(StockOpnameAsset stockOpnameAsset);

    Optional<StockOpnameDetail> findDetail(Long id);

}
