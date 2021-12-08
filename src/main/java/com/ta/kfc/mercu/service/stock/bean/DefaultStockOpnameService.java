package com.ta.kfc.mercu.service.stock.bean;

import com.ta.kfc.mercu.infrastructure.db.orm.model.actor.UserDetail;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Unit;
import com.ta.kfc.mercu.infrastructure.db.orm.model.stock.SoStatus;
import com.ta.kfc.mercu.infrastructure.db.orm.model.stock.StockOpname;
import com.ta.kfc.mercu.infrastructure.db.orm.model.stock.StockOpnameAsset;
import com.ta.kfc.mercu.infrastructure.db.orm.model.stock.StockOpnameDetail;
import com.ta.kfc.mercu.infrastructure.db.orm.repository.stock.StockOpnameAssetRepository;
import com.ta.kfc.mercu.infrastructure.db.orm.repository.stock.StockOpnameDetailRepository;
import com.ta.kfc.mercu.infrastructure.db.orm.repository.stock.StockOpnameRepository;
import com.ta.kfc.mercu.service.stock.StockOpnameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultStockOpnameService implements StockOpnameService {

    private final StockOpnameRepository repository;
    private final StockOpnameDetailRepository stockOpnameDetailRepository;
    private final StockOpnameAssetRepository stockOpnameAssetRepository;

    @Autowired
    public DefaultStockOpnameService(StockOpnameRepository repository,
                                     StockOpnameDetailRepository stockOpnameDetailRepository,
                                     StockOpnameAssetRepository stockOpnameAssetRepository) {
        this.repository = repository;
        this.stockOpnameDetailRepository = stockOpnameDetailRepository;
        this.stockOpnameAssetRepository = stockOpnameAssetRepository;
    }

    @Override
    public Optional<StockOpname> findLatestStockOpname(Unit unit, UserDetail userDetail) {
        Optional<StockOpname> inProgress = repository.
                findFirstByUnitAndCreatedByAndStatusOrderByCreatedDateDesc(unit, userDetail, SoStatus.IN_PROGRESS);

        if (inProgress.isPresent()) {
            return inProgress;
        }

        return repository.findFirstByUnitAndCreatedByAndStatusOrderByCreatedDateDesc(unit, userDetail, SoStatus.NEW);
    }

    @Override
    public List<StockOpname> findByUnit(Unit unit) {
        return repository.findByUnit(unit);
    }

    @Override
    public Optional<StockOpname> save(StockOpname stockOpname) {
        return Optional.of(repository.save(stockOpname));
    }

    @Override
    public Optional<StockOpname> update(StockOpname stockOpname) {
        return Optional.of(repository.save(stockOpname));
    }

    @Override
    public Optional<StockOpname> find(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<StockOpnameDetail> save(StockOpnameDetail stockOpnameDetail) {
        return Optional.of(stockOpnameDetailRepository.save(stockOpnameDetail));
    }

    @Override
    public Optional<StockOpnameAsset> save(StockOpnameAsset stockOpnameAsset) {
        return Optional.of(stockOpnameAssetRepository.save(stockOpnameAsset));
    }

    @Override
    public Optional<StockOpnameDetail> findDetail(Long id) {
        return stockOpnameDetailRepository.findById(id);
    }


}
