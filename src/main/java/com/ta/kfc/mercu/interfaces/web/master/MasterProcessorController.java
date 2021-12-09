package com.ta.kfc.mercu.interfaces.web.master;

import com.ta.kfc.mercu.dto.master.ProductSupplierMapping;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.*;
import com.ta.kfc.mercu.service.master.MasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.Optional;

@Controller
public class MasterProcessorController extends MasterModule {

    private MasterService masterService;

    @Autowired
    public MasterProcessorController(MasterService masterService) {
        this.masterService = masterService;
    }

    @PostMapping({MASTER_MODEL_PATH})
    public String addOrUpdateModel(Model modelUi,
                                   com.ta.kfc.mercu.infrastructure.db.orm.model.master.Model model,
                                   RedirectAttributes attr) {
        try {
            model.setUpdatedDate(new Date());
            if (model.getId() != null) {
                masterService.updateModel(model);
            } else {
                model.setCreatedDate(new Date());
                masterService.saveModel(model);
            }
        } catch (Exception exception) {
            attr.addAttribute("error", exception.getMessage());
        }
        return String.format("redirect:%s?", MASTER_MODEL_PATH);
    }

    @GetMapping({MASTER_MODEL_STATUS_PATH})
    public String updateModelStatus(Model modelUi,
                                    @PathVariable("id") Long id,
                                    RedirectAttributes attr) {
        try {
            Optional<com.ta.kfc.mercu.infrastructure.db.orm.model.master.Model> model = masterService.getModel(id);
            if (!model.isPresent()) {
                throw new RuntimeException("model not found");
            }
            model.get().setUpdatedDate(new Date());
            model.get().setEnable(!model.get().isEnable());
            masterService.updateModel(model.get());
        } catch (Exception exception) {
            attr.addAttribute("error", exception.getMessage());
        }
        return String.format("redirect:%s?", MASTER_MODEL_PATH);
    }


    @PostMapping({MASTER_BRAND_PATH})
    public String addOrUpdateBrand(Model modelUi,
                                   Brand brand,
                                   RedirectAttributes attr) {
        try {
            brand.setUpdatedDate(new Date());
            if (brand.getId() != null) {
                masterService.updateBrand(brand);
            } else {
                brand.setCreatedDate(new Date());
                masterService.saveBrand(brand);
            }
        } catch (Exception exception) {
            attr.addAttribute("error", exception.getMessage());
        }
        return String.format("redirect:%s?", MASTER_BRAND_PATH);
    }

    @GetMapping({MASTER_BRAND_STATUS_PATH})
    public String updateBrandStatus(Model modelUi,
                                    @PathVariable("id") Long id,
                                    RedirectAttributes attr) {
        try {
            Optional<Brand> brand = masterService.getBrand(id);
            if (!brand.isPresent()) {
                throw new RuntimeException("brand not found");
            }
            brand.get().setUpdatedDate(new Date());
            brand.get().setEnable(!brand.get().isEnable());
            masterService.updateBrand(brand.get());
        } catch (Exception exception) {
            attr.addAttribute("error", exception.getMessage());
        }
        return String.format("redirect:%s?", MASTER_BRAND_PATH);
    }


    @PostMapping({MASTER_PRODUCT_PATH})
    public String addOrUpdateProduct(Model modelUi,
                                     Product product,
                                     RedirectAttributes attr) {
        try {
            product.setUpdatedDate(new Date());
            if (product.getId() != null) {
                masterService.updateProduct(product);
            } else {
                product.setCreatedDate(new Date());
                masterService.saveProduct(product);
            }
        } catch (Exception exception) {
            attr.addAttribute("error", exception.getMessage());
        }
        return String.format("redirect:%s?", MASTER_PRODUCT_PATH);
    }

    @GetMapping({MASTER_PRODUCT_STATUS_PATH})
    public String updateProductStatus(Model modelUi,
                                      @PathVariable("id") Long id,
                                      RedirectAttributes attr) {
        try {
            Optional<Product> product = masterService.getProduct(id);
            if (!product.isPresent()) {
                throw new RuntimeException("product not found");
            }
            product.get().setUpdatedDate(new Date());
            product.get().setEnable(!product.get().isEnable());
            masterService.updateProduct(product.get());
        } catch (Exception exception) {
            attr.addAttribute("error", exception.getMessage());
        }
        return String.format("redirect:%s?", MASTER_PRODUCT_PATH);
    }

    @PostMapping({MASTER_UNIT_PATH})
    public String addOrUpdateUnit(Model modelUi,
                                  Unit unit,
                                  RedirectAttributes attr) {
        try {
            unit.setUpdatedDate(new Date());
            if (unit.getId() != null) {
                masterService.updateUnit(unit);
            } else {
                unit.setCreatedDate(new Date());
                masterService.saveUnit(unit);
            }
        } catch (Exception exception) {
            attr.addAttribute("error", exception.getMessage());
        }
        return String.format("redirect:%s?", MASTER_UNIT_PATH);
    }

    @GetMapping({MASTER_UNIT_STATUS_PATH})
    public String updateUnitStatus(Model modelUi,
                                   @PathVariable("id") Long id,
                                   RedirectAttributes attr) {
        try {
            Optional<Unit> unit = masterService.getUnit(id);
            if (!unit.isPresent()) {
                throw new RuntimeException("unit not found");
            }
            unit.get().setUpdatedDate(new Date());
            unit.get().setEnable(!unit.get().isEnable());
            masterService.updateUnit(unit.get());
        } catch (Exception exception) {
            attr.addAttribute("error", exception.getMessage());
        }
        return String.format("redirect:%s?", MASTER_UNIT_PATH);
    }

    @PostMapping({MASTER_DEPARTMENT_PATH})
    public String addOrUpdateDepartment(Model modelUi,
                                        Department department,
                                        RedirectAttributes attr) {
        try {
            department.setUpdatedDate(new Date());
            if (department.getId() != null) {
                masterService.updateDepartment(department);
            } else {
                department.setCreatedDate(new Date());
                masterService.saveDepartment(department);
            }
        } catch (Exception exception) {
            attr.addAttribute("error", exception.getMessage());
        }
        return String.format("redirect:%s?", MASTER_DEPARTMENT_PATH);
    }


    @GetMapping({MASTER_DEPARTMENT_STATUS_PATH})
    public String updateDepartmentStatus(Model modelUi,
                                         @PathVariable("id") Long id,
                                         RedirectAttributes attr) {
        try {
            Optional<Department> department = masterService.getDepartment(id);
            if (!department.isPresent()) {
                throw new RuntimeException("unit not found");
            }
            department.get().setUpdatedDate(new Date());
            department.get().setEnable(!department.get().isEnable());
            masterService.updateDepartment(department.get());
        } catch (Exception exception) {
            attr.addAttribute("error", exception.getMessage());
        }
        return String.format("redirect:%s?", MASTER_DEPARTMENT_PATH);
    }


    @PostMapping({MASTER_SUPPLIER_PATH})
    public String addNewSupllier(Model modelUi,
                                 Supplier supplier) {

        if (supplier.getId() == null) {
            supplier.setCreatedDate(new Date());
            supplier.setUpdatedDate(new Date());
            masterService.saveSupplier(supplier);
        } else {
            supplier.setUpdatedDate(new Date());
            masterService.updateSupplier(supplier);
        }

        return String.format("redirect:%s?", MASTER_SUPPLIER_PATH);

    }

    @GetMapping({MASTER_SUPPLIER_PATH + "/{supplierId}"})
    public String addNewSupllier(Model modelUi,
                                 @PathVariable("supplierId") Long supplierId) {

        Optional<Supplier> supplier = masterService.findSupplier(supplierId);

        if (supplier.isPresent()) {
            if (supplier.get().isEnable()) {
                supplier.get().setEnable(false);
            } else {
                supplier.get().setEnable(true);
            }
            masterService.updateSupplier(supplier.get());
        }

        return String.format("redirect:%s", MASTER_SUPPLIER_PATH);
    }


    @PostMapping({MASTER_MAPPING_PRODUCT_PATH})
    public String addProductSupllierMapping(Model modelUi,
                                            ProductSupplierMapping req) {

        req.getProduct().getSuppliers().add(req.getSupplier());
        masterService.updateProduct(req.getProduct());

        return String.format("redirect:%s?isProductSelected=true&productId=%d", MASTER_MAPPING_PRODUCT_PATH, req.getProduct().getId());

    }

}
