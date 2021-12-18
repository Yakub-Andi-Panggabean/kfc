package com.ta.kfc.mercu.infrastructure.db.orm.repository.notification;

import com.ta.kfc.mercu.infrastructure.db.orm.model.actor.UserDetail;
import com.ta.kfc.mercu.infrastructure.db.orm.model.notification.Notification;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NotificationRepository extends CrudRepository<Notification, Long> {
    List<Notification> findByUserDetail(UserDetail userDetail);
}
