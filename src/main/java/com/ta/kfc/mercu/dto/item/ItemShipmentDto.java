package com.ta.kfc.mercu.dto.item;

import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrder;

public class ItemShipmentDto {
    private RequestOrder ro;
    private String note;

    public RequestOrder getRo() {
        return ro;
    }

    public void setRo(RequestOrder ro) {
        this.ro = ro;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
