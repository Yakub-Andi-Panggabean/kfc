package com.ta.kfc.mercu.service.master.bean;

import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Brand;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Model;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Product;
import com.ta.kfc.mercu.infrastructure.db.orm.repository.master.BrandRepository;
import com.ta.kfc.mercu.infrastructure.db.orm.repository.master.ModelRepository;
import com.ta.kfc.mercu.infrastructure.db.orm.repository.master.ProductRepository;
import com.ta.kfc.mercu.service.master.MasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultMasterService implements MasterService {

    private ModelRepository modelRepository;
    private BrandRepository brandRepository;
    private ProductRepository productRepository;

    @Autowired
    public DefaultMasterService(ModelRepository modelRepository, BrandRepository brandRepository, ProductRepository productRepository) {
        this.modelRepository = modelRepository;
        this.brandRepository = brandRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Optional<Model> saveModel(Model model) {
        return Optional.of(modelRepository.save(model));
    }

    @Override
    public List<Model> getAllModel() {
        return modelRepository.findAll();
    }

    @Override
    public Optional<Model> getModel(Long id) {
        return modelRepository.findById(id);
    }

    @Override
    public Optional<Model> updateModel(Model model) {

        if (model.getId() == null) {
            throw new RuntimeException("bad parameter, null model id");
        }

        return Optional.of(modelRepository.save(model));
    }

    @Override
    public Optional<Brand> saveBrand(Brand brand) {
        return Optional.of(brandRepository.save(brand));
    }

    @Override
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    @Override
    public Optional<Brand> getBrand(Long id) {
        return brandRepository.findById(id);
    }

    @Override
    public Optional<Brand> updateBrand(Brand brand) {
        return Optional.of(brandRepository.save(brand));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> saveProduct(Product product) {
        return Optional.of(productRepository.save(product));
    }

    @Override
    public Optional<Product> getProduct(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Optional<Product> updateProduct(Product product) {
        return Optional.of(productRepository.save(product));
    }


}
