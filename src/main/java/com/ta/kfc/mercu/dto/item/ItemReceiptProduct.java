package com.ta.kfc.mercu.dto.item;

import com.ta.kfc.mercu.infrastructure.db.orm.model.asset.ItemReceipt;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Product;

public class ItemReceiptProduct {
    private Long itemReceipt;
    private Long product;
    private Long qty;

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

    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }
}
