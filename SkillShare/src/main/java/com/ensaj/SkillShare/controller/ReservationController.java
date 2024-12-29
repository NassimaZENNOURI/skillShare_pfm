/*package com.ensaj.SkillShare.controller;

import com.ensaj.SkillShare.model.*;
import com.ensaj.SkillShare.service.*;
import com.ensaj.SkillShare.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/reserver/{idServicePropose}")
    public ResponseEntity<String> reserverService(
            @RequestHeader("Authorization") String token,
            @PathVariable int idServicePropose) {
        try {
            // Vérification du token JWT
            if (token.startsWith("Bearer ")) {
                token = token.substring(7); // Retirer le préfixe "Bearer "
            } else {
                throw new IllegalArgumentException("Le token doit commencer par 'Bearer '");
            }

            // Extraire l'ID du créateur depuis le token
            int idCreateur = jwtUtil.extractUserId(token);

            // Appel du service pour réserver
            reservationService.reserverService(idServicePropose, idCreateur);

            return ResponseEntity.ok("Réservation effectuée avec succès.");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> getReservations(
            @RequestHeader("Authorization") String token) {
        try {
            // Récupérer les réservations de l'utilisateur connecté
            List<Reservation> reservations = reservationService.getReservationsForUser(token);

            // Retourner les réservations en réponse
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
*/

package com.ensaj.SkillShare.controller;

import com.ensaj.SkillShare.model.*;
import com.ensaj.SkillShare.service.*;
import com.ensaj.SkillShare.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/reserver/{idServicePropose}")
    public ResponseEntity<String> reserverService(
            @RequestHeader("Authorization") String token,
            @PathVariable int idServicePropose,@RequestBody LocalDateTime dateHeure) {
        try {
            // Vérification du token JWT
            if (token.startsWith("Bearer ")) {
                token = token.substring(7); // Retirer le préfixe "Bearer "
            } else {
                throw new IllegalArgumentException("Le token doit commencer par 'Bearer '");
            }

            // Extraire l'ID du créateur depuis le token
            int idCreateur = jwtUtil.extractUserId(token);

            // Appel du service pour réserver
            reservationService.reserverService(idServicePropose, idCreateur, dateHeure);

            return ResponseEntity.ok("Réservation effectuée avec succès.");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<Map<String, Object>>> getReservations(
            @RequestHeader("Authorization") String token) {
        try {
            // Récupérer les réservations de l'utilisateur connecté
            List<Map<String, Object>> reservations = reservationService.getReservationsForUser(token);

            // Retourner les réservations en réponse
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    @GetMapping("/check/{idServicePropose}")
    public ResponseEntity<Boolean> checkReservation(
            @RequestHeader("Authorization") String token,
            @PathVariable int idServicePropose) {

        // Vérification du token JWT
        if (token.startsWith("Bearer ")) {
            token = token.substring(7); // Retirer le préfixe "Bearer "
        } else {
            throw new IllegalArgumentException("Le token doit commencer par 'Bearer '");
        }

        // Extraire l'ID du créateur depuis le token
        int idCreateur = jwtUtil.extractUserId(token);

        boolean hasReservation = reservationService.hasUserReservationForService(idCreateur, idServicePropose);
        return ResponseEntity.ok(hasReservation);
    }


    // Endpoint pour accepter une réservation (mettre à jour le statut)
    @PutMapping("/accepter/{idReser}")
    public ResponseEntity<Void> accepterReservation(@PathVariable int idReser) {
        reservationService.accepterReservation(idReser);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // Endpoint pour refuser une réservation (mettre à jour le statut)
    @PutMapping("/refuser/{idReser}")
    public ResponseEntity<Void> refuserReservation(@PathVariable int idReser) {
        reservationService.refuserReservation(idReser);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

}