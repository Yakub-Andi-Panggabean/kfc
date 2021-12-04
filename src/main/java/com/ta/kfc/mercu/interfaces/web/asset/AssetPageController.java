package com.ta.kfc.mercu.interfaces.web.asset;

import com.ta.kfc.mercu.context.FastContext;
import com.ta.kfc.mercu.dto.item.AddAssetDto;
import com.ta.kfc.mercu.infrastructure.db.orm.model.actor.UserDetail;
import com.ta.kfc.mercu.infrastructure.db.orm.model.asset.AssetStatus;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Unit;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrderStatus;
import com.ta.kfc.mercu.service.security.AuthorizationService;
import com.ta.kfc.mercu.service.transaction.RequestOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class AssetPageController extends AssetModule {

    private AuthorizationService authorizationService;
    private RequestOrderService requestOrderService;
    private FastContext context;

    @Autowired
    public AssetPageController(AuthorizationService authorizationService,
                               RequestOrderService requestOrderService,
                               FastContext context) {
        this.authorizationService = authorizationService;
        this.requestOrderService = requestOrderService;
        this.context = context;
    }

    @GetMapping({ASSET_VERIFICATION_PATH})
    public String getAssetVerificationPage(Model model) {

        UserDetail userDetail = context.getUser().get().getUserDetail();

        model.addAttribute("template", "asset_verification");
        model.addAttribute("orders", requestOrderService.findRequestOrderPerUser(userDetail)
                .stream()
                .filter(ro -> ro.getStatus() == RequestOrderStatus.SEND)
                .collect(Collectors.toList()));

        return "index";
    }

    @GetMapping({ASSET_ADJUSTMENT_PATH})
    public String getAssetAdjustmentPage(Model model) {

        model.addAttribute("template", "asset_adjustment");

        return "index";
    }

    @GetMapping({ASSET_VERIFICATION_PATH + "/{ro_id}"})
    public String getAssetVerificationModal(
            @PathVariable(value = "ro_id") Long roId,
            Model model) {

        model.addAttribute("order", requestOrderService.findRequestOrderById(roId).get());

        return "fragments/asset_verification/modal_item_verification";
    }
}
