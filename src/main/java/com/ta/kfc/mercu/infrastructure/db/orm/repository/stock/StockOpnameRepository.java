package com.ta.kfc.mercu.infrastructure.db.orm.repository.stock;

import com.ta.kfc.mercu.infrastructure.db.orm.model.actor.UserDetail;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Unit;
import com.ta.kfc.mercu.infrastructure.db.orm.model.stock.SoStatus;
import com.ta.kfc.mercu.infrastructure.db.orm.model.stock.StockOpname;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockOpnameRepository extends CrudRepository<StockOpname, Long> {

    Optional<StockOpname> findFirstByUnitAndCreatedByAndStatusOrderByCreatedDateDesc(
            Unit unit,
            UserDetail createdBy,
            SoStatus status);

    List<StockOpname> findByUnit(Unit unit);

}
