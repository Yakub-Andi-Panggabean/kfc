package com.ta.kfc.mercu.interfaces.web.tagging;

import com.ta.kfc.mercu.infrastructure.db.orm.model.asset.Asset;
import com.ta.kfc.mercu.service.asset.AssetService;
import com.ta.kfc.mercu.service.security.AuthorizationService;
import com.ta.kfc.mercu.shared.QRHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;

@Controller
public class TaggingPrintController {

    public static final String TAGGING_PATH = "/tagging";
    public static final String TAGGING_PRINT_PATH = TAGGING_PATH + "/print";

    private AuthorizationService authorizationService;
    private AssetService assetService;

    @Autowired
    public TaggingPrintController(AuthorizationService authorizationService,
                                  AssetService assetService) {
        this.authorizationService = authorizationService;
        this.assetService = assetService;
    }

    @GetMapping({TAGGING_PRINT_PATH})
    public String getPrintTaggingPage(Model model) {

        List<Asset> assets = assetService.getAllAsset();

        model.addAttribute("template", "tagging_print");
        model.addAttribute("assets", assets);

        int columnNumber = 4;
        model.addAttribute("columnNumber", columnNumber);
        int rowNumber = assets.size() % columnNumber > 0 ?
                (assets.size() / columnNumber) + 1 : (assets.size() / columnNumber);
        model.addAttribute("rowNumber", rowNumber);
        return "index";
    }

    @GetMapping(value = "/qrcode/{id}")
    public void qrcode(@PathVariable("id") String id,
                       HttpServletResponse response) throws Exception {

        String assetID = assetService.findByCode(id)
                .map(a -> a.getCode()
                        .concat(":")
                        .concat(a.getUnit().getUnitName())
                        .concat(":")
                        .concat(a.getProduct().getName())
                ).orElse(id);

        response.setContentType("image/png");
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(QRHelper.getQRCodeImage(assetID, 200, 200));
        outputStream.flush();
        outputStream.close();
    }

    @GetMapping(value = "/download/qrcode/{id}")
    public ResponseEntity<?> qrcodePDF(@PathVariable("id") String id,
                                       HttpServletResponse response) throws Exception {

        String assetID = assetService.findByCode(id)
                .map(a -> a.getCode()
                        .concat(":")
                        .concat(a.getUnit().getUnitName())
                        .concat(":")
                        .concat(a.getProduct().getName())
                ).orElse(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s.png", id))
                .contentType(MediaType.IMAGE_PNG).body(QRHelper.getQRCodeImage(assetID, 400, 400));

    }

}
