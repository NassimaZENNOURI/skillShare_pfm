package com.ensaj.SkillShare.controller;

import com.ensaj.SkillShare.model.Feedback;
import com.ensaj.SkillShare.service.*;
import com.ensaj.SkillShare.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedBackController {

    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private JwtUtil jwtUtil; // Utilitaire pour extraire les informations du token JWT

    @PostMapping("/add/{idServicePropose}")
    public ResponseEntity<String> addFeedback(
            @RequestHeader("Authorization") String token, // Le token JWT envoyé par l'utilisateur
            @PathVariable int idServicePropose, // L'ID du service proposé dans l'URL
            @RequestBody Feedback feedbackRequest // Le JSON envoyé dans le corps de la requête
    ) {
        try {
        // Vérification que le token commence bien par "Bearer "
        if (token.startsWith("Bearer ")) {
            token = token.substring(7); // Retirer le préfixe "Bearer "
        } else {
            throw new IllegalArgumentException("Le token doit commencer par 'Bearer '");
        }
         // Extraire l'ID du créateur depuis le token
         int idCreateur = jwtUtil.extractUserId(token);
         
         // Ajouter le feedback
         feedbackService.addFeedback(
                 feedbackRequest.getNote(),
                 feedbackRequest.getCommentaire(),
                 idServicePropose,
                 idCreateur
         );
         return ResponseEntity.ok("Ajout avec succes ");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/feedbacks/{idServicePropose}")
    public ResponseEntity<List<Map<String, Object>>> getFeedbacksForService(@PathVariable int idServicePropose) {
        try {
        	List<Map<String, Object>> feedbacks = feedbackService.getFeedbacksForService(idServicePropose);
            if (feedbacks.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(feedbacks);
            }
            return ResponseEntity.ok(feedbacks);
        } catch (Exception e) {
            e.printStackTrace(); // Pour afficher les erreurs dans les logs
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


}