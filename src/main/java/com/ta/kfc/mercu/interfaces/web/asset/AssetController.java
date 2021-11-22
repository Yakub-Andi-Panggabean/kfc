package com.ta.kfc.mercu.interfaces.web.page.asset;

import com.ta.kfc.mercu.service.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AssetController {

    public static final String ASSET_PATH = "/asset";
    public static final String ASSET_VERIFICATION_PATH = ASSET_PATH + "/verification";
    public static final String ASSET_ADJUSTMENT_PATH = ASSET_PATH + "/adjustment";


    private AuthorizationService authorizationService;

    @Autowired
    public AssetController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @GetMapping({ASSET_VERIFICATION_PATH})
    public String getAssetVerificationPage(Model model) {

        model.addAttribute("template", "asset_verification");

        return "index";
    }

    @GetMapping({ASSET_ADJUSTMENT_PATH})
    public String getAssetAdjustmentPage(Model model) {

        model.addAttribute("template", "asset_adjustment");

        return "index";
    }
}
