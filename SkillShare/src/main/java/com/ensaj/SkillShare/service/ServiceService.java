package com.ensaj.SkillShare.service;

import com.ensaj.SkillShare.model.Categorie;
import com.ensaj.SkillShare.model.ServicePropose;
import com.ensaj.SkillShare.model.Utilisateur;
import com.ensaj.SkillShare.repository.CategorieRepository;
import com.ensaj.SkillShare.repository.ServiceRepository;
import com.ensaj.SkillShare.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import java.lang.*;

@Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private CategorieRepository categorieRepository;

    public List<Map<String, Object>> getAllServicesWithDetails(int idCategorie, int idUser) {
        List<Object[]> results = serviceRepository.findAllServicesByCategoryAndUser(idCategorie, idUser);
        System.out.println("List_services: "+ results);
        List<Map<String, Object>> services = new ArrayList<>();

        for (Object[] row : results) {
            services.add(Map.of(
                    "nomService", row[0],
                    "description", row[1],
                    "prix", row[2],
                    "nomUtilisateur", row[3],
                    "prenomUtilisateur", row[4],
                    "nomCategorie", row[5],
                    "idService", row[6],
                    "idCategorie", row[7]

            ));
        }

        return services;
    }


    public List<Map<String, Object>> getAllServices(int idUser) {
        List<Object[]> results = serviceRepository.findAllServicesExcludingUser(idUser);
        List<Map<String, Object>> services = new ArrayList<>();

        for (Object[] row : results) {
            services.add(Map.of(
                    "idService", row[0],
                    "nomService", row[1],
                    "description", row[2],
                    "prix", row[3],
                    "nomUtilisateur", row[4],
                    "prenomUtilisateur", row[5],
                    "localisation", row[6],
                    "image", row[7],
                    "nomCategorie", row[8]
            ));
        }

        return services;
    }



    public List<Object[]> getServiceById(int idCategorie, int idUser, int idService) {
        List<Object[]> servicePropose = serviceRepository.findAllServicesByCategoryAndUserAndId(idCategorie, idUser, idService);
        return servicePropose;
    }

    public void addService(Map<String, Object> requestBody, int idUser, int idCategorie) {
        // Créer un objet ServicePropose
        ServicePropose service = new ServicePropose();

        // Récupérer les valeurs du requestBody et les affecter à l'objet service
        service.setNomService((String) requestBody.get("nom_service"));
        service.setDescription((String) requestBody.get("description"));
        Double prixDouble = (Double) requestBody.get("prix");
        float prix = prixDouble.floatValue();
        service.setPrix(prix);

        Utilisateur utilisateur = utilisateurRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("Utilisateur avec id " + idUser + " non trouvé"));
        service.setUtilisateur(utilisateur);

        Categorie categorie = categorieRepository.findById(idCategorie)
                .orElseThrow(() -> new RuntimeException("Catégorie avec id " + idCategorie + " non trouvée"));
        service.setCategories(categorie);

        serviceRepository.save(service);
    }

    public void deleteService(int idService) {
        serviceRepository.deleteById(idService);
    }

    public void updateService(int idService, ServicePropose service) {
        Optional<ServicePropose> existingService = serviceRepository.findById(idService);
        if (existingService.isPresent()) {
            ServicePropose s = existingService.get();
            s.setNomService(service.getNomService());
            s.setDescription(service.getDescription());
            s.setPrix(service.getPrix());
            s.setUtilisateur(service.getUtilisateur());
            s.setCategories(service.getCategories());
            serviceRepository.save(s);
        }
    }


    public List<Map<String, Object>> searchServices(Float minPrix, Float maxPrix, String categorie, String localisation, int idUser, String searchText) {
        List<Object[]> results = serviceRepository.searchServices(idUser, minPrix, maxPrix, categorie, localisation, searchText);
        List<Map<String, Object>> services = new ArrayList<>();

        for (Object[] row : results) {
            services.add(Map.of(
                    "idService", row[0],
                    "nomService", row[1],
                    "description", row[2],
                    "prix", row[3],
                    "nomUtilisateur", row[4],
                    "prenomUtilisateur", row[5],
                    "localisation", row[6],
                    "image", row[7],
                    "nomCategorie", row[8]
            ));
        }

        return services;

    }

    public List<Map<String, Object>> searchServiceByText(int idUser, String searchText) {
        List<Object[]> results = serviceRepository.searchServiceByText(idUser, searchText);
        List<Map<String, Object>> services = new ArrayList<>();
        for (Object[] row : results) {
            services.add(Map.of(
                    "idService", row[0],
                    "nomService", row[1],
                    "description", row[2],
                    "prix", row[3],
                    "nomUtilisateur", row[4],
                    "prenomUtilisateur", row[5],
                    "localisation", row[6],
                    "image", row[7],
                    "nomCategorie", row[8]
            ));
        }
        return services;
    }

}