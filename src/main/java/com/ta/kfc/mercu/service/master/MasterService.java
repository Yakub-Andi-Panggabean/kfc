package com.ta.kfc.mercu.service.master;

import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Brand;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Model;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Product;

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


}
