package com.ta.kfc.mercu.dto.report;

import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Product;

public class ProductParameter {
    private Product product;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
