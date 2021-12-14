package com.ta.kfc.mercu.dto.item;

import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrder;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.RequestOrderStatus;
import com.ta.kfc.mercu.infrastructure.db.orm.model.transaction.TransactionStatus;

public class SubmitTransferOrder {
    private RequestOrder requestOrder;
    private String note;
    private RequestOrderStatus status;

    public RequestOrder getRequestOrder() {
        return requestOrder;
    }

    public void setRequestOrder(RequestOrder requestOrder) {
        this.requestOrder = requestOrder;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public RequestOrderStatus getStatus() {
        return status;
    }

    public void setStatus(RequestOrderStatus status) {
        this.status = status;
    }
}
