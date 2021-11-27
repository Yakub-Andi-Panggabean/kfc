package com.ta.kfc.mercu.service.master;

import com.ta.kfc.mercu.infrastructure.db.orm.model.master.*;

import java.util.List;
import java.util.Optional;

public interface MasterService {

    Optional<Model> saveModel(Model model);

    List<Model> getAllModel();

    Optional<Model> getModel(Long id);

    Optional<Model> updateModel(Model model);

    Optional<Brand> saveBrand(Brand brand);

    List<Brand> getAllBrands();

    Optional<Brand> getBrand(Long id);

    Optional<Brand> updateBrand(Brand brand);

    List<Product> getAllProducts();

    Optional<Product> saveProduct(Product product);

    Optional<Product> getProduct(Long id);

    Optional<Product> updateProduct(Product product);

    Optional<Unit> saveUnit(Unit unit);

    Optional<Unit> updateUnit(Unit unit);

    List<Unit> getAllUnit();

    Optional<Unit> getUnit(Long id);

    Optional<Department> saveDepartment(Department department);

    Optional<Department> updateDepartment(Department department);

    List<Department> getAllDepartments();

    Optional<Department> getDepartment(Long id);


}
