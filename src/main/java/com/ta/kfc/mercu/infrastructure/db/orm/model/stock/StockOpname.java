package com.ta.kfc.mercu.infrastructure.db.orm.model.stock;

import com.ta.kfc.mercu.infrastructure.db.orm.model.actor.UserDetail;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Unit;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "stock_opname")
public class StockOpname {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Unit unit;
    @OneToOne
    private UserDetail createdBy;
    private Date createdDate;
    private Date updatedDate;
    @OneToMany(mappedBy = "stockOpname")
    private List<StockOpnameDetail> details;

    @Enumerated(EnumType.STRING)
    private SoStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public UserDetail getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserDetail createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public List<StockOpnameDetail> getDetails() {
        return details;
    }

    public void setDetails(List<StockOpnameDetail> details) {
        this.details = details;
    }

    public SoStatus getStatus() {
        return status;
    }

    public void setStatus(SoStatus status) {
        this.status = status;
    }
}
