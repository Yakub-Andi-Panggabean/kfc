package com.ta.kfc.mercu.interfaces.web.master;

import com.ta.kfc.mercu.service.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MasterController {

    public static final String MASTER_PATH = "/master";
    public static final String MASTER_BRAND_PATH = MASTER_PATH + "/brand";
    public static final String MASTER_PRODUCT_PATH = MASTER_PATH + "/product";
    public static final String MASTER_MODEL_PATH = MASTER_PATH + "/model";
    public static final String MASTER_SUPPLIER_PATH = MASTER_PATH + "/supplier";
    public static final String MASTER_USER_PATH = MASTER_PATH + "/user";





    private AuthorizationService authorizationService;

    @Autowired
    public MasterController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
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
    public String getMasterModelPage(Model model) {

        model.addAttribute("template", "master");
        model.addAttribute("master_template", "master_model");

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

}
