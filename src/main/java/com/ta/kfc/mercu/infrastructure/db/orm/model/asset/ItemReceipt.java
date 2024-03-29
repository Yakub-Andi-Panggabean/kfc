package com.ta.kfc.mercu.infrastructure.db.orm.model.asset;

import com.ta.kfc.mercu.infrastructure.db.orm.model.actor.UserDetail;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Supplier;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Unit;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrder;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "item_receipt")
public class ItemReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @OneToOne
    @JoinColumn(name = "request_order_id")
    private RequestOrder requestOrder;
    private String purchaseOrder;
    @OneToOne
    private UserDetail receiver;
    @OneToMany
    private List<Asset> assets;
    @Enumerated(EnumType.STRING)
    private ItemReceiptStatus status;
    @OneToOne
    private Unit location;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "received_date")
    private Date receivedDate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(String purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }

    public UserDetail getReceiver() {
        return receiver;
    }

    public void setReceiver(UserDetail receiver) {
        this.receiver = receiver;
    }

    public ItemReceiptStatus getStatus() {
        return status;
    }

    public void setStatus(ItemReceiptStatus status) {
        this.status = status;
    }

    public Unit getLocation() {
        return location;
    }

    public void setLocation(Unit location) {
        this.location = location;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }
}
