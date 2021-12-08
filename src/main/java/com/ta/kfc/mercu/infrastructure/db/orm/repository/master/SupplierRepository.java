package com.ta.kfc.mercu.infrastructure.db.orm.repository.master;

import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Supplier;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SupplierRepository extends CrudRepository<Supplier, Long> {

    List<Supplier> findAll();

}
