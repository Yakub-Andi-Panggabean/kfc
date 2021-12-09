package com.ta.kfc.mercu.dto.master;

import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Product;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Supplier;

public class ProductSupplierMapping {
    private Product product;
    private Supplier supplier;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}
