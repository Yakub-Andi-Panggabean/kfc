package com.ta.kfc.mercu.interfaces.web.master;

import com.ta.kfc.mercu.service.security.MasterService;
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

}
