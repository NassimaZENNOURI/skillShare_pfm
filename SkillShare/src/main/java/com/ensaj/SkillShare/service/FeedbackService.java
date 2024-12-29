package com.ensaj.SkillShare.service;

import com.ensaj.SkillShare.model.*;
import com.ensaj.SkillShare.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class FeedbackService {

    private FeedBackRepository feedbackRepository;
    private ServiceRepository serviceProposeRepository;
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    public FeedbackService(FeedBackRepository feedbackRepository, ServiceRepository serviceProposeRepository, UtilisateurRepository utilisateurRepository) {
        this.feedbackRepository = feedbackRepository;
        this.serviceProposeRepository = serviceProposeRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    public Feedback addFeedback(float note, String commentaire, int idServicePropose, int idCreateur) throws Exception {
        // Étape 1 : Récupérer le service proposé depuis l'ID
        Optional<ServicePropose> serviceOpt = serviceProposeRepository.findById(idServicePropose);
        if (serviceOpt.isEmpty()) {
            throw new Exception("Le service proposé avec l'ID " + idServicePropose + " n'existe pas.");
        }
        ServicePropose service = serviceOpt.get();

        // Étape 2 : Récupérer le destinataire depuis le service
        Utilisateur destinataire = service.getUtilisateur();
        if (destinataire == null) {
            throw new Exception("Aucun destinataire associé au service proposé.");
        }

        // Étape 3 : Récupérer le créateur depuis l'ID
        Optional<Utilisateur> createurOpt = utilisateurRepository.findById(idCreateur);
        if (createurOpt.isEmpty()) {
            throw new Exception("Le créateur avec l'ID " + idCreateur + " n'existe pas.");
        }
        Utilisateur createur = createurOpt.get();

        // Étape 4 : Créer et sauvegarder le feedback
        Feedback feedback = new Feedback();
        feedback.setNote(note);
        feedback.setCommentaire(commentaire);
        feedback.setDate(new Date());
        feedback.setService(service);
        feedback.setCreateur(createur);
        feedback.setDestinataire(destinataire);

        // Étape 5 : Récupérer la note déjà enregistrée du destinataire
        float noteDejaEnregistre = destinataire.getNoteMoyenne();

        // Étape 6 : Calculer la nouvelle note en ajoutant la note du feedback à la note existante
        float nouvelleNote = noteDejaEnregistre + note;

        // Étape 7 : Mettre à jour la note du destinataire
        destinataire.setNoteMoyenne(nouvelleNote);
        utilisateurRepository.save(destinataire);

        // Étape 8 : Créer une notification
        Notification notification = new Notification();
        // notification.setContenue("Vous avez reçu un nouveau feedback de " + createur.getNom()+" "+createur.getPrenom() + " avec une note de " + note+ " pour le service "+service.getNomService());
        notification.setContenue("You have received new feedback from " + createur.getNom() + " " + createur.getPrenom() + " with a rating of " + note + " for the service " + service.getNomService() + ".");
        notification.setDateEnvoie(LocalDateTime.now());
        notification.setStatut(false); // Statut = non lue
        notification.setCreateur(createur);
        notification.setDestinataire(destinataire);

        // Sauvegarder la notification
        notificationRepository.save(notification);

        return feedbackRepository.save(feedback);
    }


    public List<Map<String, Object>> getFeedbacksForService(int idServicePropose) {
        List<Object[]> results = feedbackRepository.findByService(idServicePropose);
        List<Map<String, Object>> feedbacks = new ArrayList<>();

        for (Object[] row : results) {
            feedbacks.add(Map.of(
                    "commentaire", row[1],
                    "note", row[2],
                    "nomUtilisateur", row[4],
                    "prenomUtilisateur", row[5],
                    "idFeed", row[0],
                    "date", row[3],
                    "image", row[6]
            ));
        }

        return feedbacks;
    }

}