package com.ta.kfc.mercu.dto.item;

import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Supplier;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Unit;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrder;

public class AddItemReceipt {
    private Supplier supplier;
    private RequestOrder requestOrder;
    private String poNumber;
    private Unit unit;

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public RequestOrder getRequestOrder() {
        return requestOrder;
    }

    public void setRequestOrder(RequestOrder requestOrder) {
        this.requestOrder = requestOrder;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}
