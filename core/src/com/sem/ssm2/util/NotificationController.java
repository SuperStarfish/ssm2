package com.sem.ssm2.util;

/**
 * Controls the notifications.
 */
public interface NotificationController {
    /**
     * Schedules the notification to be displayed.
     *
     * @param time Time to schedule the notification.
     */
    void scheduleNotification(final Long time);
}