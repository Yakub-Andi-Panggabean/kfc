package com.ta.kfc.mercu.service.security;

import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Model;

import java.util.List;
import java.util.Optional;

public interface MasterService {

    Optional<Model> saveModel(Model model);

    List<Model> getAllModel();

    Optional<Model> getModel(Long id);

    Optional<Model> updateModel(Model model);

}
