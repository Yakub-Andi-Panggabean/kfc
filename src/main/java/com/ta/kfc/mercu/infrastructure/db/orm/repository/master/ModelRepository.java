package com.ta.kfc.mercu.infrastructure.db.orm.repository.master;

import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Model;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelRepository extends CrudRepository<Model, Long> {
    List<Model> findAll();
}
