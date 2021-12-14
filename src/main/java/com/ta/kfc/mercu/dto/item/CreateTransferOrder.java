package com.ta.kfc.mercu.dto.item;

import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Unit;

public class CreateTransferOrder {
    private Unit from;
    private Unit to;

    public Unit getFrom() {
        return from;
    }

    public void setFrom(Unit from) {
        this.from = from;
    }

    public Unit getTo() {
        return to;
    }

    public void setTo(Unit to) {
        this.to = to;
    }
}
