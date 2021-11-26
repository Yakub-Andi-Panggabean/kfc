package com.ta.kfc.mercu.infrastructure.db.orm.repository.master;

import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Brand;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BrandRepository extends CrudRepository<Brand, Long> {
    List<Brand> findAll();
}
