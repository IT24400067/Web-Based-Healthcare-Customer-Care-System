package com.example.healthcare_customercare.config;

import com.example.healthcare_customercare.observer.EmailNotificationObserver;
import com.example.healthcare_customercare.observer.LoggingNotificationObserver;
import com.example.healthcare_customercare.observer.NotificationObserver;
import com.example.healthcare_customercare.observer.WebSocketNotificationObserver;
import com.example.healthcare_customercare.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.util.List;

/**
 * Configuration class to register all notification observers
 */
@Configuration
public class NotificationObserverConfig {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private List<NotificationObserver> notificationObservers;

    @PostConstruct
    public void registerObservers() {
        // Register all available observers
        for (NotificationObserver observer : notificationObservers) {
            notificationService.registerObserver(observer);
            System.out.println("Registered notification observer: " + observer.getClass().getSimpleName());
        }
    }
}
