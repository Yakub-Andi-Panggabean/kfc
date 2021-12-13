package com.ta.kfc.mercu.dto.item;

import com.ta.kfc.mercu.infrastructure.db.orm.model.asset.ItemReceipt;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Product;

public class ItemReceiptProduct {
    private Long itemReceipt;
    private Long product;

    public Long getItemReceipt() {
        return itemReceipt;
    }

    public void setItemReceipt(Long itemReceipt) {
        this.itemReceipt = itemReceipt;
    }

    public Long getProduct() {
        return product;
    }

    public void setProduct(Long product) {
        this.product = product;
    }
}
