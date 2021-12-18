package com.ta.kfc.mercu.service.notification;

import com.ta.kfc.mercu.infrastructure.db.orm.model.actor.UserDetail;
import com.ta.kfc.mercu.infrastructure.db.orm.model.auth.User;
import com.ta.kfc.mercu.infrastructure.db.orm.model.notification.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationService {
    Optional<Notification> save(Notification notification);

    Optional<Notification> findById(Long id);

    List<Notification> findByUserDetail(User user);

    Optional<Notification> read(Notification notification);
}
