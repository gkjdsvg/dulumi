package com.example.dulumi.Repository;
import com.example.dulumi.domain.Notification;
import com.example.dulumi.sse.TestNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<Notification> findById(long id);

    long id(Long id);
}
