package com.ta.kfc.mercu.service.security.bean;

import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Model;
import com.ta.kfc.mercu.infrastructure.db.orm.repository.master.ModelRepository;
import com.ta.kfc.mercu.service.security.MasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultMasterService implements MasterService {

    private ModelRepository modelRepository;

    @Autowired
    public DefaultMasterService(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
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
}
