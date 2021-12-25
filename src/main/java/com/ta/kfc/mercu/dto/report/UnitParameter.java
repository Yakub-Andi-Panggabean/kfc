package com.ta.kfc.mercu.dto.report;

import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Unit;

public class UnitParameter {
    private Unit unit;

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}
