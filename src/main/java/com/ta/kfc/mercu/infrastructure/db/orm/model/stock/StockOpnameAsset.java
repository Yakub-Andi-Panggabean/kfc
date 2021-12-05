package com.ta.kfc.mercu.infrastructure.db.orm.model.stock;

import com.ta.kfc.mercu.infrastructure.db.orm.model.asset.Asset;
import com.ta.kfc.mercu.infrastructure.db.orm.model.asset.AssetStatus;

import javax.persistence.*;

@Entity
@Table(name = "stock_opname_detail_asset")
public class StockOpnameAsset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "so_detail")
    private StockOpnameDetail stockOpnameDetail;
    @OneToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;

    @Enumerated(EnumType.STRING)
    private SoStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StockOpnameDetail getStockOpnameDetail() {
        return stockOpnameDetail;
    }

    public void setStockOpnameDetail(StockOpnameDetail stockOpnameDetail) {
        this.stockOpnameDetail = stockOpnameDetail;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public SoStatus getStatus() {
        return status;
    }

    public void setStatus(SoStatus status) {
        this.status = status;
    }
}

