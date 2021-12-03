package com.ta.kfc.mercu.infrastructure.db.orm.model.transaction;

public enum RequestOrderStatus {
    NEW, WAITING_APPROVAL, APPROVED, WAITING_SEND_APPROVAL,
    SEND_APPROVED, SEND,
    REJECTED, CANCELED, IN_PROGRESS, COMPLETED
}
