package com.ta.kfc.mercu.infrastructure.db.orm.model.notification;

import com.ta.kfc.mercu.infrastructure.db.orm.model.actor.UserDetail;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrder;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private boolean alreadyRead;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDetail userDetail;
    private Date createdDate;
    private Date updatedDate;
    @ManyToOne
    private RequestOrder order;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isAlreadyRead() {
        return alreadyRead;
    }

    public void setAlreadyRead(boolean read) {
        this.alreadyRead = read;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
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

    public RequestOrder getOrder() {
        return order;
    }

    public void setOrder(RequestOrder orders) {
        this.order = orders;
    }
}
