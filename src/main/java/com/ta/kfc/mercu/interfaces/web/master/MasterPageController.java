package com.ta.kfc.mercu.interfaces.web.master;

import com.ta.kfc.mercu.infrastructure.db.orm.model.master.*;
import com.ta.kfc.mercu.service.security.AuthorizationService;
import com.ta.kfc.mercu.service.master.MasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class MasterPageController extends MasterModule {

    private final AuthorizationService authorizationService;
    private final MasterService masterService;

    @Autowired
    public MasterPageController(AuthorizationService authorizationService,
                                MasterService masterService) {
        this.authorizationService = authorizationService;
        this.masterService = masterService;
    }

    @GetMapping({MASTER_PATH})
    public String getMasterPage(Model model) {
        return String.format("redirect:%s?", MASTER_PRODUCT_PATH);
    }

    @GetMapping({MASTER_BRAND_PATH})
    public String getMasterBrandPage(
            @RequestParam(value = "isUpdate", required = false) boolean isUpdate,
            @RequestParam(value = "id", required = false) Long id,
            Model model) {

        model.addAttribute("template", "master");
        model.addAttribute("master_template", "master_brand");
        model.addAttribute("brands", masterService.getAllBrands());
        if (isUpdate) {
            Optional<Brand> modelEntity = masterService.getBrand(id);
            if (modelEntity.isPresent()) {
                model.addAttribute("isUpdate", true);
                model.addAttribute("brand", modelEntity.get());
            }
        } else {
            model.addAttribute("brand", new Brand());
        }


        return "index";
    }

    @GetMapping({MASTER_PRODUCT_PATH})
    public String getMasterProductPage(@RequestParam(value = "isUpdate", required = false) boolean isUpdate,
                                       @RequestParam(value = "search", required = false) String search,
                                       @RequestParam(value = "id", required = false) Long id,
                                       Model model) {

        model.addAttribute("template", "master");
        model.addAttribute("master_template", "master_product");
        model.addAttribute("models", masterService.getAllModel());
        model.addAttribute("brands", masterService.getAllBrands());
        if (null != search && !search.isEmpty()) {
            model.addAttribute("products", masterService.getAllProducts().
                    stream().filter(p -> p.getName().contains(search)).collect(Collectors.toList()));
        } else {
            model.addAttribute("products", masterService.getAllProducts());
        }
        if (isUpdate) {
            Optional<Product> productEntity = masterService.getProduct(id);
            if (productEntity.isPresent()) {
                model.addAttribute("isUpdate", true);
                model.addAttribute("product", productEntity.get());
            }
        } else {
            model.addAttribute("product", new Product());
        }

        return "index";
    }


    @GetMapping({MASTER_MODEL_PATH})
    public String getMasterModelPage(
            @RequestParam(value = "isUpdate", required = false) boolean isUpdate,
            @RequestParam(value = "id", required = false) Long id,
            Model model) {

        model.addAttribute("template", "master");
        model.addAttribute("master_template", "master_model");
        model.addAttribute("models", masterService.getAllModel());
        if (isUpdate) {
            Optional<com.ta.kfc.mercu.infrastructure.db.orm.model.master.Model> modelEntity = masterService.getModel(id);
            if (modelEntity.isPresent()) {
                model.addAttribute("isUpdate", true);
                model.addAttribute("model", modelEntity.get());
            }
        } else {
            model.addAttribute("model", new com.ta.kfc.mercu.infrastructure.db.orm.model.master.Model());
        }

        return "index";
    }


    @GetMapping({MASTER_SUPPLIER_PATH})
    public String getMasterSupplierPage(@RequestParam(value = "isUpdate", required = false) boolean isUpdate,
                                        @RequestParam(value = "supplierId", required = false) Long supplierID,
                                        Model model) {

        model.addAttribute("template", "master");
        model.addAttribute("master_template", "master_supplier");

        if (supplierID == null) {
            model.addAttribute("supplier", new Supplier());
        } else {
            model.addAttribute("supplier", masterService.findSupplier(supplierID).get());
        }
        model.addAttribute("isUpdate", isUpdate);

        model.addAttribute("suppliers", masterService.findAllSuppliers());

        return "index";
    }

    @GetMapping({MASTER_USER_PATH})
    public String getMasterUserPage(Model model) {

        model.addAttribute("template", "master");
        model.addAttribute("master_template", "master_user");

        return "index";
    }

    @GetMapping({MASTER_UNIT_PATH})
    public String getMasterOutletPage(@RequestParam(value = "isUpdate", required = false) boolean isUpdate,
                                      @RequestParam(value = "id", required = false) Long id,
                                      Model model) {

        model.addAttribute("template", "master");
        model.addAttribute("master_template", "master_unit");

        model.addAttribute("units", masterService.getAllUnit());

        if (isUpdate) {
            Optional<Unit> unitEntity = masterService.getUnit(id);
            if (unitEntity.isPresent()) {
                model.addAttribute("isUpdate", true);
                model.addAttribute("unit", unitEntity.get());
            }
        } else {
            model.addAttribute("unit", new Unit());
        }


        return "index";
    }

    @GetMapping({MASTER_DEPARTMENT_PATH})
    public String getMasterDepartmentPage(@RequestParam(value = "isUpdate", required = false) boolean isUpdate,
                                          @RequestParam(value = "id", required = false) Long id,
                                          Model model) {

        model.addAttribute("template", "master");
        model.addAttribute("master_template", "master_department");
        model.addAttribute("departments", masterService.getAllDepartments());

        if (isUpdate) {
            Optional<Department> departmentEntity = masterService.getDepartment(id);
            if (departmentEntity.isPresent()) {
                model.addAttribute("isUpdate", true);
                model.addAttribute("department", departmentEntity.get());
            }
        } else {
            model.addAttribute("department", new Department());
        }

        return "index";
    }

}
