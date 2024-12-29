/*package com.ensaj.SkillShare.service;

import com.ensaj.SkillShare.model.*;
import com.ensaj.SkillShare.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import com.ensaj.SkillShare.util.JwtUtil;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ServiceRepository serviceProposeRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public Reservation reserverService(int idServicePropose, int idCreateur) throws Exception {
        // Étape 1 : Récupérer le service proposé
        Optional<ServicePropose> serviceOpt = serviceProposeRepository.findById(idServicePropose);
        if (serviceOpt.isEmpty()) {
            throw new Exception("Le service proposé avec l'ID " + idServicePropose + " n'existe pas.");
        }
        ServicePropose service = serviceOpt.get();

        // Étape 2 : Récupérer le créateur (utilisateur qui réserve)
        Optional<Utilisateur> createurOpt = utilisateurRepository.findById(idCreateur);
        if (createurOpt.isEmpty()) {
            throw new Exception("Le créateur avec l'ID " + idCreateur + " n'existe pas.");
        }
        Utilisateur createur = createurOpt.get();

        // Étape 3 : Récupérer le destinataire (propriétaire du service)
        Utilisateur destinataire = service.getUtilisateur();
        if (destinataire == null) {
            throw new Exception("Aucun destinataire associé au service proposé.");
        }

        // Étape 4 : Créer la réservation
        Reservation reservation = new Reservation();
        reservation.setService(service);
        reservation.setCreateur(createur);
        reservation.setDestinataire(destinataire);
        reservation.setDateHeure(LocalDateTime.now()); // La date et l'heure actuelles
        reservation.setStatut(false); // Statut initial de la réservation (non confirmée)
        
        // Étape 5 : Sauvegarder la réservation
        Reservation savedReservation = reservationRepository.save(reservation);

        // Étape 6 : Créer une notification pour le destinataire
        Notification notification = new Notification();
        notification.setContenue("Vous avez une nouvelle réservation pour le service : " + service.getNomService());
        notification.setDateEnvoie(LocalDateTime.now());
        notification.setStatut(false); // Statut de la notification (non lue)
        notification.setCreateur(createur);
        notification.setDestinataire(destinataire);

        // Sauvegarder la notification
        notificationRepository.save(notification);

        return savedReservation;
    }

    public List<Reservation> getReservationsForUser(String token) throws Exception {
        // Vérifier que le token commence par "Bearer "
        if (token.startsWith("Bearer ")) {
            token = token.substring(7); // Retirer le préfixe "Bearer "
        } else {
            throw new IllegalArgumentException("Le token doit commencer par 'Bearer '");
        }

        // Extraire l'ID de l'utilisateur depuis le token
        int idUser = jwtUtil.extractUserId(token);

        // Récupérer les réservations pour l'utilisateur
        return reservationRepository.findByDestinataire_IdUser(idUser); // Assurez-vous que cette méthode existe dans le repository
    }

}
*/


package com.ensaj.SkillShare.service;

import com.ensaj.SkillShare.model.*;
import com.ensaj.SkillShare.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import com.ensaj.SkillShare.util.JwtUtil;

import jakarta.transaction.Transactional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ServiceRepository serviceProposeRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public Reservation reserverService(int idServicePropose, int idCreateur, LocalDateTime dateHeure) throws Exception {
        // Étape 1 : Récupérer le service proposé
        Optional<ServicePropose> serviceOpt = serviceProposeRepository.findById(idServicePropose);
        if (serviceOpt.isEmpty()) {
            throw new Exception("Le service proposé avec l'ID " + idServicePropose + " n'existe pas.");
        }
        ServicePropose service = serviceOpt.get();

        // Étape 2 : Récupérer le créateur (utilisateur qui réserve)
        Optional<Utilisateur> createurOpt = utilisateurRepository.findById(idCreateur);
        if (createurOpt.isEmpty()) {
            throw new Exception("Le créateur avec l'ID " + idCreateur + " n'existe pas.");
        }
        Utilisateur createur = createurOpt.get();

        // Étape 3 : Récupérer le destinataire (propriétaire du service)
        Utilisateur destinataire = service.getUtilisateur();
        if (destinataire == null) {
            throw new Exception("Aucun destinataire associé au service proposé.");
        }

        // Étape 4 : Créer la réservation
        Reservation reservation = new Reservation();
        reservation.setService(service);
        reservation.setCreateur(createur);
        reservation.setDestinataire(destinataire);
        reservation.setDateHeure(dateHeure); // La date et l'heure actuelles
        reservation.setStatut("0");
        //reservation.setStatut(false);// Statut initial de la réservation (non confirmée)

        // Étape 5 : Sauvegarder la réservation
        Reservation savedReservation = reservationRepository.save(reservation);

        // Étape 6 : Créer une notification pour le destinataire
        Notification notification = new Notification();
        //notification.setContenue("Vous avez une nouvelle réservation pour le service : " + service.getNomService() + "dans le "+ reservation.getDateHeure() +"par "+ createur.getNom()+" "+createur.getPrenom());
        notification.setContenue("You have a new reservation for the service: " + service.getNomService() + " on " + reservation.getDateHeure() + " by " + createur.getNom() + " " + createur.getPrenom() + ".");
        notification.setDateEnvoie(LocalDateTime.now());
        notification.setStatut(false); // Statut de la notification (non lue)
        notification.setCreateur(createur);
        notification.setDestinataire(destinataire);

        // Sauvegarder la notification
        notificationRepository.save(notification);

        return savedReservation;
    }

    public boolean hasUserReservationForService(int userId, int serviceId) {
        return reservationRepository.existsReservationByUserAndService(userId, serviceId);
    }

    @Transactional
    public void accepterReservation(int idReser) {
        // Récupérer la réservation depuis la base de données
        Reservation reservation = reservationRepository.findById(idReser)
                .orElseThrow(() -> new IllegalArgumentException("Réservation non trouvée"));

        Notification notification = new Notification();
        notification.setContenue("Votre réservation pour le service : " + reservation.getService().getNomService() + " est accepté");
        notification.setDateEnvoie(LocalDateTime.now());
        notification.setStatut(false); // Statut de la notification (non lue)
        notification.setCreateur(reservation.getDestinataire());
        notification.setDestinataire(reservation.getCreateur());
        // Sauvegarder la notification
        notificationRepository.save(notification);

        reservationRepository.accepter(idReser);
    }

    // Méthode pour refuser une réservation (mettre à jour le statut)
    @Transactional
    public void refuserReservation(int idReser) {
        // Récupérer la réservation depuis la base de données
        Reservation reservation = reservationRepository.findById(idReser)
                .orElseThrow(() -> new IllegalArgumentException("Réservation non trouvée"));

        Notification notification = new Notification();
        notification.setContenue("Votre réservation pour le service : " + reservation.getService().getNomService() +" est refusé");
        notification.setDateEnvoie(LocalDateTime.now());
        notification.setStatut(false); // Statut de la notification (non lue)
        notification.setCreateur(reservation.getDestinataire());
        notification.setDestinataire(reservation.getCreateur());
        // Sauvegarder la notification
        notificationRepository.save(notification);

        reservationRepository.refuser(idReser);
    }

    public List<Map<String, Object>> getReservationsForUser(String token) throws Exception {
        // Vérifier que le token commence par "Bearer "
        if (token.startsWith("Bearer ")) {
            token = token.substring(7); // Retirer le préfixe "Bearer "
        } else {
            throw new IllegalArgumentException("Le token doit commencer par 'Bearer '");
        }

        // Extraire l'ID de l'utilisateur depuis le token
        int idUser = jwtUtil.extractUserId(token);

        List<Object[]> results = reservationRepository.findReservationNonLu(idUser);
        List<Map<String, Object>> reservations = new ArrayList<>();

        for (Object[] row : results) {
            reservations.add(Map.of(
                    "idreser", row[0],
                    "date", row[1],
                    "nomService", row[2],
                    "nom", row[3],
                    "prenom", row[4],
                    "image", row[5]
            ));
        }

        return reservations;
    }

}