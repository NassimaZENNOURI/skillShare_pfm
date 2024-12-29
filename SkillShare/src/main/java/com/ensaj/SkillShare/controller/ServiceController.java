package com.ensaj.SkillShare.controller;

import com.ensaj.SkillShare.model.ServicePropose;
import com.ensaj.SkillShare.service.ServiceService;
import com.ensaj.SkillShare.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    @Autowired
    ServiceService serviceService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/allServices/{idCategorie}/{idUser}")
    public ResponseEntity<List<Map<String, Object>>> getAllServicesWithDetails(
            @PathVariable int idCategorie,
            @PathVariable int idUser) {

        List<Map<String, Object>> services = serviceService.getAllServicesWithDetails(idCategorie, idUser);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/allServices")
    public ResponseEntity<List<Map<String, Object>>> getAllServices(
            @RequestHeader("Authorization") String token) {
        // Supprimer "Bearer " du token si présent
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // Extraire l'idUser du token
        int idUser = jwtUtil.extractUserId(token);


        List<Map<String, Object>> services = serviceService.getAllServices(idUser);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/allServices/{idCategorie}")
    public ResponseEntity<List<Map<String, Object>>> getAllServicesWithDetails(
            @PathVariable int idCategorie,
            @RequestHeader("Authorization") String token) {

        // Supprimer "Bearer " du token si présent
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // Extraire l'idUser du token
        int idUser = jwtUtil.extractUserId(token);

        List<Map<String, Object>> services = serviceService.getAllServicesWithDetails(idCategorie, idUser);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/service/{idCategorie}/{idService}")
    public ResponseEntity<List<Object[]>> getServiceById(
            @PathVariable int idCategorie,
            @RequestHeader("Authorization") String token,
            @PathVariable int idService) {

        // Supprimer "Bearer " du token si présent
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // Extraire l'idUser du token
        int idUser = jwtUtil.extractUserId(token);

        List<Object[]> service = serviceService.getServiceById(idCategorie, idUser, idService);
        return ResponseEntity.ok(service);
    }

    @PostMapping("/save")
    public ResponseEntity<String> ajouterService(@RequestBody Map<String, Object> requestBody,
                                                 @RequestHeader("Authorization") String token) {

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        int idUser = jwtUtil.extractUserId(token);

        int idCategorie = ((Number) requestBody.get("id_categorie")).intValue();

        serviceService.addService(requestBody, idUser, idCategorie);

        return ResponseEntity.ok("Service saved successfully");
    }


    @DeleteMapping("/delete/{idService}")
    public ResponseEntity<String> deleteService(@PathVariable int idService) {
        serviceService.deleteService(idService);
        return ResponseEntity.ok("Service deleted successfully");
    }


    @PutMapping("/update/{idService}")
    public ResponseEntity<String> updateService(@PathVariable int idService, @RequestBody ServicePropose service, @RequestHeader("Authorization") String token) {
        try {
            serviceService.updateService(idService, service);
            return ResponseEntity.ok("Service mis à jour avec succès.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la mise à jour du service : " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> searchServices(
            @RequestParam(required = false) Float minPrix,
            @RequestParam(required = false) Float maxPrix,
            @RequestParam(required = false) String categorie,
            @RequestParam(required = false) String localisation,
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) String searchText) {
        // Supprimer "Bearer " du token si présent
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        // Extraire l'idUser du token
        int idUser = jwtUtil.extractUserId(token);
        System.out.println("localisation"+localisation);
        List<Map<String, Object>> services = serviceService.searchServices(minPrix, maxPrix, categorie, localisation, idUser, searchText);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/searchText")
    public ResponseEntity<List<Map<String, Object>>> searchServiceByText(
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) String searchText) {
        // Supprimer "Bearer " du token si présent
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        // Extraire l'idUser du token
        int idUser = jwtUtil.extractUserId(token);
        List<Map<String, Object>> services = serviceService.searchServiceByText(idUser, searchText);
        return ResponseEntity.ok(services);
    }

}