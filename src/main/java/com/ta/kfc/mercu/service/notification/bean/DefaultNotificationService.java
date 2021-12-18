package com.ta.kfc.mercu.service.notification.bean;

import com.ta.kfc.mercu.infrastructure.db.orm.model.auth.User;
import com.ta.kfc.mercu.infrastructure.db.orm.model.notification.Notification;
import com.ta.kfc.mercu.infrastructure.db.orm.repository.notification.NotificationRepository;
import com.ta.kfc.mercu.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultNotificationService implements NotificationService {

    private NotificationRepository notificationRepository;

    @Autowired
    public DefaultNotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Optional<Notification> save(Notification notification) {
        return Optional.of(notificationRepository.save(notification));
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return notificationRepository.findById(id);
    }

    @Override
    public List<Notification> findByUserDetail(User user) {
        return notificationRepository.findByUserDetail(user.getUserDetail());
    }

    @Override
    public Optional<Notification> read(Notification notification) {
        notification.setAlreadyRead(true);
        return Optional.of(notificationRepository.save(notification));
    }
}
