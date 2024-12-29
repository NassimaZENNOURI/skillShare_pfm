package com.ensaj.SkillShare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.ensaj.SkillShare.model.*;

import jakarta.transaction.Transactional;

import java.util.*;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer>{

 // Trouver les réservations reçues par un utilisateur (destinataire)
 //List<Reservation> findByDestinataire_IdUser(int idDestinataire);

 @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
         "FROM Reservation r " +
         "WHERE r.createur.idUser = :idUser AND r.service.idService = :idService AND r.statut = '0'")
 boolean existsReservationByUserAndService(@Param("idUser") int idUser, @Param("idService") int idService);



 @Modifying
 @Transactional
 @Query("UPDATE Reservation r SET r.statut = '1' WHERE r.idReser = :idReser")
 void accepter(@Param("idReser") int idReser);

 @Modifying
 @Transactional
 @Query("UPDATE Reservation r SET r.statut = '2' WHERE r.idReser = :idReser")
 void refuser(@Param("idReser") int idReser);



 @Query("SELECT r.idReser, r.dateHeure, sp.nomService, u.nom, u.prenom, u.image " +
         "FROM Reservation r " +
         "JOIN r.service sp " +
         "JOIN r.createur u " +
         "WHERE r.destinataire.idUser = :idUser AND r.statut = '0'")
 List<Object[]> findReservationNonLu(@Param("idUser") int idUser);



}