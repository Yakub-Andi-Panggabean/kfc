package com.ta.kfc.mercu.infrastructure.db.orm.repository.master;

import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Unit;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UnitRepository extends CrudRepository<Unit, Long> {
    List<Unit> findAll();
}
