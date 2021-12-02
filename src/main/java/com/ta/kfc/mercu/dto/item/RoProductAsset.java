package com.ta.kfc.mercu.dto.item;

import com.ta.kfc.mercu.infrastructure.db.orm.model.asset.Asset;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Product;

public class RoProductAsset {

    private Product product;
    private Asset asset;

    public RoProductAsset(Product product, Asset asset) {
        this.product = product;
        this.asset = asset;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }
}
