package com.ta.kfc.mercu.infrastructure.db.orm.model.transaction;

import com.ta.kfc.mercu.infrastructure.db.orm.model.actor.UserDetail;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Product;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Unit;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "request_order")
public class RequestOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private UserDetail requester;

    @ManyToOne
    @JoinColumn(name = "approver_id", nullable = false)
    private UserDetail approver;

    @Enumerated(EnumType.STRING)
    private RequestOrderType type;

    @Enumerated(EnumType.STRING)
    private RequestOrderStatus status;

    @ManyToOne
    @JoinColumn(name = "from_unit", nullable = false)
    private Unit from;

    @ManyToOne
    @JoinColumn(name = "to_unit", nullable = false)
    private Unit to;

    @OneToMany
    private List<Product> products;

    @OneToMany(mappedBy = "order")
    private List<Transaction> transactions;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDetail getRequester() {
        return requester;
    }

    public void setRequester(UserDetail requester) {
        this.requester = requester;
    }

    public UserDetail getApprover() {
        return approver;
    }

    public void setApprover(UserDetail approver) {
        this.approver = approver;
    }

    public RequestOrderType getType() {
        return type;
    }

    public void setType(RequestOrderType type) {
        this.type = type;
    }

    public RequestOrderStatus getStatus() {
        return status;
    }

    public void setStatus(RequestOrderStatus status) {
        this.status = status;
    }

    public Unit getFrom() {
        return from;
    }

    public void setFrom(Unit unit) {
        this.from = unit;
    }

    public Unit getTo() {
        return to;
    }

    public void setTo(Unit to) {
        this.to = to;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
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

}
