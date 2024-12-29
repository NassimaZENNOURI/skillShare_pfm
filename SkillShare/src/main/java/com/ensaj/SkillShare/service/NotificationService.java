package com.ensaj.SkillShare.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ensaj.SkillShare.model.Notification;
import com.ensaj.SkillShare.repository.NotificationRepository;

import jakarta.transaction.Transactional;

@Service
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    public List<Notification> getUnreadNotifications(int idUser) {
        return notificationRepository.findUnreadNotificationsByUserId(idUser);
    }

    @Transactional
    public void markNotificationAsRead(int idNotif) {
        notificationRepository.markAsRead(idNotif);
    }
}