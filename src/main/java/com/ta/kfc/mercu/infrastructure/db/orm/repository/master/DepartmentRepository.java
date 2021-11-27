package com.ta.kfc.mercu.infrastructure.db.orm.repository.master;

import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Department;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DepartmentRepository extends CrudRepository<Department, Long> {
    List<Department> findAll();
}
