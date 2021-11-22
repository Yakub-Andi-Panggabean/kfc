package com.ta.kfc.mercu.interfaces.web.master;

import com.ta.kfc.mercu.service.security.AuthorizationService;
import com.ta.kfc.mercu.service.security.MasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

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

        model.addAttribute("template", "master");
        model.addAttribute("master_template", "master_product");

        return "index";
    }

    @GetMapping({MASTER_BRAND_PATH})
    public String getMasterBrandPage(Model model) {

        model.addAttribute("template", "master");
        model.addAttribute("master_template", "master_brand");

        return "index";
    }

    @GetMapping({MASTER_PRODUCT_PATH})
    public String getMasterProductPage(Model model) {

        model.addAttribute("template", "master");
        model.addAttribute("master_template", "master_product");

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
    public String getMasterSupplierPage(Model model) {

        model.addAttribute("template", "master");
        model.addAttribute("master_template", "master_supplier");

        return "index";
    }

    @GetMapping({MASTER_USER_PATH})
    public String getMasterUserPage(Model model) {

        model.addAttribute("template", "master");
        model.addAttribute("master_template", "master_user");

        return "index";
    }

    @GetMapping({MASTER_OUTLET_PATH})
    public String getMasterOutletPage(Model model) {

        model.addAttribute("template", "master");
        model.addAttribute("master_template", "master_outlet");

        return "index";
    }

    @GetMapping({MASTER_DEPARTMENT_PATH})
    public String getMasterDepartmentPage(Model model) {

        model.addAttribute("template", "master");
        model.addAttribute("master_template", "master_department");

        return "index";
    }

}
