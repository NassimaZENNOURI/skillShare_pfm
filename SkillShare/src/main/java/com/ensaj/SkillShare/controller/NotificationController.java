package com.ensaj.SkillShare.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ensaj.SkillShare.model.Notification;
import com.ensaj.SkillShare.service.NotificationService;
import com.ensaj.SkillShare.util.JwtUtil;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@RequestHeader("Authorization") String token) {
        // Supprimer "Bearer " du token si présent
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // Extraire l'idUser du token
        int idUser = jwtUtil.extractUserId(token);
        List<Notification> unreadNotifications = notificationService.getUnreadNotifications(idUser);
        return ResponseEntity.ok(unreadNotifications);
    }

    @PutMapping("/read/{id}")
    public ResponseEntity<String> markNotificationAsRead(@PathVariable("id") int idNotif) {
        notificationService.markNotificationAsRead(idNotif);
        return ResponseEntity.ok("Notification mis à jour avec succès.");
    }

}