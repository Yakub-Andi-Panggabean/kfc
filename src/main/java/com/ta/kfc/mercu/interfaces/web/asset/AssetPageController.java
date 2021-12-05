package com.ta.kfc.mercu.interfaces.web.asset;

import com.ta.kfc.mercu.context.FastContext;
import com.ta.kfc.mercu.dto.item.AddAssetDto;
import com.ta.kfc.mercu.infrastructure.db.orm.model.actor.UserDetail;
import com.ta.kfc.mercu.infrastructure.db.orm.model.asset.Asset;
import com.ta.kfc.mercu.infrastructure.db.orm.model.asset.AssetStatus;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Product;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Unit;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrderStatus;
import com.ta.kfc.mercu.service.asset.AssetService;
import com.ta.kfc.mercu.service.master.MasterService;
import com.ta.kfc.mercu.service.security.AuthorizationService;
import com.ta.kfc.mercu.service.transaction.RequestOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class AssetPageController extends AssetModule {

    private AuthorizationService authorizationService;
    private RequestOrderService requestOrderService;
    private MasterService masterService;
    private FastContext context;

    @Autowired
    public AssetPageController(AuthorizationService authorizationService,
                               RequestOrderService requestOrderService,
                               MasterService masterService,
                               FastContext context) {
        this.authorizationService = authorizationService;
        this.requestOrderService = requestOrderService;
        this.masterService = masterService;
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
    public String getAssetAdjustmentPage(@RequestParam(value = "unitId", required = false) Long unitId, Model model) {

        model.addAttribute("isUnitSelected", unitId != null);
        model.addAttribute("template", "asset_adjustment");
        model.addAttribute("units", masterService.getAllUnit()
                .stream().filter(Unit::isEnable)
                .collect(Collectors.toList()));

        if (unitId != null) {
            Optional<Unit> unit = masterService.getUnit(unitId);
            Map<Product, List<Asset>> results = unit.map(u -> u.getAssets()
                            .stream()
                            .collect(Collectors.groupingBy(Asset::getProduct, Collectors.toList())))
                    .get();
            model.addAttribute("unit_id", unitId);
            model.addAttribute("unit_products", results);
        }

        return "index";
    }

    @GetMapping({ASSET_ADJUSTMENT_PATH + "/{unitId}/{productId}"})
    public String getAssetAdjustmentDetailPage(@PathVariable(value = "unitId") Long unitId,
                                               @PathVariable(value = "productId") Long productId,
                                               Model model) {

        model.addAttribute("template", "asset_adjustment_detail");
        Optional<Unit> unit = masterService.getUnit(unitId);
        if (unit.isPresent()) {
            model.addAttribute("unit", unit.get());
            model.addAttribute("product", unit.map(u -> u.getAssets()
                            .stream()
                            .findAny())
                    .map(asset -> asset.get())
                    .map(asset -> asset.getProduct()).get());
            model.addAttribute("assets", unit.get()
                    .getAssets()
                    .stream()
                    .filter(asset -> asset.getProduct().getId() == productId)
                    .collect(Collectors.toList()));
        } else {
            model.addAttribute("assets", Collections.emptyList());
            model.addAttribute("unit", null);
        }

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
