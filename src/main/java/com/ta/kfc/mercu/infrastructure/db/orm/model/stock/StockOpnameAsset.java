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
    private AssetStatus assetStatus;
}

