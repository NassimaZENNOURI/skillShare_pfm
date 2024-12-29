package com.ensaj.SkillShare.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ensaj.SkillShare.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer>{

    @Query("SELECT n FROM Notification n WHERE n.statut = false AND n.destinataire.idUser = :idUser")
    List<Notification> findUnreadNotificationsByUserId(@Param("idUser") int idUser);

    @Modifying
    @Query("UPDATE Notification n SET n.statut = true WHERE n.idNotif = :idNotif")
    void markAsRead(@Param("idNotif") int idNotif);

}