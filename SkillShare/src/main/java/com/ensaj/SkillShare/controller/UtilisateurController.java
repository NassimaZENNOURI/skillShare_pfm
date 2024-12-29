package com.ensaj.SkillShare.controller;

import com.ensaj.SkillShare.dto.AuthRequest;
import com.ensaj.SkillShare.dto.AuthResponse;
import com.ensaj.SkillShare.model.Utilisateur;
import com.ensaj.SkillShare.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest authRequest) {
        AuthResponse token = utilisateurService.authenticate(authRequest);
        return utilisateurService.findUserByEmailAndPassword(authRequest.getEmail(), authRequest.getMotDePasse())
                .map(user -> ResponseEntity.ok(token))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse("Invalid email or password")));
    }
    
 // Endpoint pour récupérer un utilisateur par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> getUtilisateurById(@PathVariable int id) {
        Optional<Utilisateur> utilisateur = utilisateurService.findUserById(id);
        if (utilisateur.isPresent()) {
            return ResponseEntity.ok(utilisateur.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody Utilisateur utilisateur) {
        try {
            // Étape 1 : Vérifier si un chemin d'image est fourni
            if (utilisateur.getImage() != null && !utilisateur.getImage().isEmpty()) {
                String imageSource = utilisateur.getImage();
                String fileName = UUID.randomUUID().toString() + ".png";
                Path targetPath = Paths.get("src/main/resources/static/images/").resolve(fileName).normalize();
                // Créer le répertoire s'il n'existe pas
                Files.createDirectories(targetPath.getParent());
                // Vérifier si le chemin local est valide
                Path sourcePath = Paths.get(imageSource).normalize();
                if (!Files.exists(sourcePath) || !Files.isReadable(sourcePath)) {
                    return ResponseEntity.badRequest().body("Chemin d'image local invalide ou introuvable.");
                }
                // Copier l'image depuis le chemin local vers le répertoire cible
                Files.copy(sourcePath, targetPath);
                // Mettre à jour le chemin de l'image dans l'objet utilisateur
                utilisateur.setImage("/images/" + fileName);
            } else {
                utilisateur.setImage("/images/personIcon.png");
            }
            // Étape 2 : Sauvegarder l'utilisateur dans la base de données
            Utilisateur savedUser = utilisateurService.addUser(utilisateur);
            // Étape 3 : Retourner une réponse avec succès
            return ResponseEntity.ok("Utilisateur ajouté avec succès. Chemin de l'image : " + savedUser.getImage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors du traitement ou de l'enregistrement de l'image.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'ajout de l'utilisateur.");
        }
    }


// La premiere methode :
//@PutMapping("/update/{id}")
//public ResponseEntity<String> updateUser(@PathVariable int id, @RequestBody Utilisateur utilisateur) {
//    try {
//        // Étape 1 : Vérifier si l'utilisateur existe dans la base de données
//        Optional<Utilisateur> existingUser = utilisateurService.findUserById(id);
//        if (existingUser == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body("Utilisateur introuvable avec l'ID : " + id);
//        }
//
//        Utilisateur userUpdated = existingUser.get();
//        // Étape 2 : Mettre à jour les champs modifiables
//        userUpdated.setNom(utilisateur.getNom());
//        userUpdated.setPrenom(utilisateur.getPrenom());
//        userUpdated.setEmail(utilisateur.getEmail());
//        userUpdated.setMotDePasse(utilisateur.getMotDePasse());
//
//        // Étape 3 : Gérer la mise à jour de l'image
//        if (utilisateur.getImage() != null && !utilisateur.getImage().isEmpty()) {
//            String imageSource = utilisateur.getImage();
//            String fileName = UUID.randomUUID().toString() + ".png";
//            Path targetPath = Paths.get("src/main/resources/static/images/").resolve(fileName).normalize();
//            Files.createDirectories(targetPath.getParent());
//            Path sourcePath = Paths.get(imageSource).normalize();
//            if (!Files.exists(sourcePath) || !Files.isReadable(sourcePath)) {
//                return ResponseEntity.badRequest().body("Chemin d'image local invalide ou introuvable.");
//            }
//            Files.copy(sourcePath, targetPath);
//            userUpdated.setImage("/images/" + fileName);
//        }
//
//        // Étape 4 : Sauvegarder les modifications dans la base de données
//        utilisateurService.updateUser(id,userUpdated);
//
//        // Étape 5 : Retourner une réponse avec succès
//        return ResponseEntity.ok("Utilisateur mis à jour avec succès.");
//    } catch (IOException e) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body("Erreur lors du traitement ou de l'enregistrement de l'image.");
//    } catch (Exception e) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body("Erreur lors de la mise à jour de l'utilisateur.");
//    }
//}

    // La deuxieme methode :
//    @PutMapping("/update/{id}")
//    public ResponseEntity<String> updateUser(@PathVariable int id, @RequestBody Utilisateur utilisateur) {
//        try {
//            Optional<Utilisateur> existingUser = utilisateurService.findUserById(id);
//            if (existingUser.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body("Utilisateur introuvable avec l'ID : " + id);
//            }
//
//            Utilisateur userUpdated = existingUser.get();
//            userUpdated.setNom(utilisateur.getNom());
//            userUpdated.setPrenom(utilisateur.getPrenom());
//            userUpdated.setEmail(utilisateur.getEmail());
//            userUpdated.setMotDePasse(utilisateur.getMotDePasse());
//
//            // Gérer l'image seulement si une nouvelle image est fournie
//            if (utilisateur.getImage() != null && !utilisateur.getImage().isEmpty()) {
//                String imagePath = utilisateur.getImage();
//
//                // Si l'image est déjà dans le dossier images, pas besoin de la copier
//                if (imagePath.startsWith("/images/")) {
//                    userUpdated.setImage(imagePath);
//                } else {
//                    // C'est une nouvelle image à partir du mobile
//                    File sourceFile = new File(imagePath);
//                    if (!sourceFile.exists()) {
//                        return ResponseEntity.badRequest().body("Fichier image introuvable : " + imagePath);
//                    }
//
//                    String fileName = UUID.randomUUID().toString() + ".jpg";
//                    Path targetPath = Paths.get("src/main/resources/static/images/").resolve(fileName).normalize();
//                    Files.createDirectories(targetPath.getParent());
//
//                    // Copier le fichier
//                    Files.copy(sourceFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
//                    userUpdated.setImage("/images/" + fileName);
//                }
//            } else {
//                // Conserver l'image existante si aucune nouvelle image n'est fournie
//                userUpdated.setImage(existingUser.get().getImage());
//            }
//
//            utilisateurService.updateUser(id, userUpdated);
//            return ResponseEntity.ok("Utilisateur mis à jour avec succès.");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Erreur lors de la mise à jour : " + e.getMessage());
//        }
//    }


    // La troisieme methode :
//    @PutMapping("/update/{id}")
//    public ResponseEntity<String> updateUser(@PathVariable int id, @RequestBody Utilisateur utilisateur) {
//        try {
//            Optional<Utilisateur> existingUser = utilisateurService.findUserById(id);
//            if (existingUser.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body("Utilisateur introuvable avec l'ID : " + id);
//            }
//
//            Utilisateur userUpdated = existingUser.get();
//            userUpdated.setNom(utilisateur.getNom());
//            userUpdated.setPrenom(utilisateur.getPrenom());
//            userUpdated.setEmail(utilisateur.getEmail());
//            userUpdated.setMotDePasse(utilisateur.getMotDePasse());
//
//            // Gérer l'image seulement si une nouvelle image est fournie
//            if (utilisateur.getImage() != null && !utilisateur.getImage().isEmpty()) {
//                String imagePath = utilisateur.getImage();
//
//                // Si l'image est déjà dans le dossier images, pas besoin de la copier
//                if (imagePath.startsWith("/images/")) {
//                    userUpdated.setImage(imagePath);
//                } else {
//                    // C'est une nouvelle image à partir du mobile
//                    String fileName = UUID.randomUUID().toString() + ".png";
//                    Path targetPath = Paths.get("src/main/resources/static/images/").resolve(fileName).normalize();
//                    Files.createDirectories(targetPath.getParent());
//
//                    // Copier le fichier
//                    Files.copy(new ByteArrayInputStream(Base64.getDecoder().decode(imagePath)), targetPath, StandardCopyOption.REPLACE_EXISTING);
//                    userUpdated.setImage("/images/" + fileName);
//                }
//            } else {
//                // Conserver l'image existante si aucune nouvelle image n'est fournie
//                userUpdated.setImage(existingUser.get().getImage());
//            }
//
//            utilisateurService.updateUser(id, userUpdated);
//            return ResponseEntity.ok("Utilisateur mis à jour avec succès.");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Erreur lors de la mise à jour : " + e.getMessage());
//        }
//    }



    // 4 eme methode
    //@PutMapping("/update/{id}")
//    public ResponseEntity<String> updateUser(@PathVariable int id, @RequestBody Utilisateur utilisateur) {
//        try {
//            Optional<Utilisateur> existingUser = utilisateurService.findUserById(id);
//            if (existingUser.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body("Utilisateur introuvable avec l'ID : " + id);
//            }
//            Utilisateur userUpdated = existingUser.get();
//            userUpdated.setNom(utilisateur.getNom());
//            userUpdated.setPrenom(utilisateur.getPrenom());
//            userUpdated.setEmail(utilisateur.getEmail());
//            userUpdated.setMotDePasse(utilisateur.getMotDePasse());
//
//            System.out.println("Nom utilisateur : " + utilisateur.getNom());
//            System.out.println("Image reçue : " + utilisateur.getImage());
//            // Gérer l'image seulement si une nouvelle image est fournie
//            if (utilisateur.getImage() != null && !utilisateur.getImage().isEmpty()) {
//                String imagePath = utilisateur.getImage();
//                System.out.println("imagePath1: "+imagePath);
//                // Si l'image est déjà dans le dossier images, pas besoin de la copier
//                if (imagePath.startsWith("/images/")) {
//                    userUpdated.setImage(imagePath);
//                } else {
//                    // C'est une nouvelle image à partir du mobile
//                    String fileName = UUID.randomUUID().toString() + ".png";
//                    Path targetPath = Paths.get("src/main/resources/static/images/").resolve(fileName).normalize();
//                    Files.createDirectories(targetPath.getParent());
//
//                    // Copier le fichier
//                    Files.copy(new ByteArrayInputStream(Base64.getDecoder().decode(imagePath)), targetPath, StandardCopyOption.REPLACE_EXISTING);
//                    System.out.println("imagePath2: "+fileName);
//                    userUpdated.setImage("/images/" + fileName);
//                }
//            } else {
//                // Conserver l'image existante si aucune nouvelle image n'est fournie
//                userUpdated.setImage(existingUser.get().getImage());
//            }
//            if (userUpdated.getImage() != null && !userUpdated.getImage().isEmpty()) {
//                utilisateurService.updateUser(userUpdated);  // Assurez-vous que l'image est incluse dans cette mise à jour
//            }
//
//            return ResponseEntity.ok("Utilisateur mis à jour avec succès.");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Erreur lors de la mise à jour : " + e.getMessage());
//        }
//    }
//


    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUser(@PathVariable int id, @RequestBody Utilisateur utilisateur) {
        try {
            Optional<Utilisateur> existingUser = utilisateurService.findUserById(id);
            if (existingUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Utilisateur introuvable avec l'ID : " + id);
            }
            Utilisateur userUpdated = existingUser.get();
            userUpdated.setNom(utilisateur.getNom());
            userUpdated.setPrenom(utilisateur.getPrenom());
            userUpdated.setEmail(utilisateur.getEmail());
            userUpdated.setMotDePasse(utilisateur.getMotDePasse());

            System.out.println("imagePath: " + utilisateur.getImage());
            // Gérer l'image seulement si une nouvelle image est fournie
            if (utilisateur.getImage() != null && !utilisateur.getImage().isEmpty()) {
                String imagePath = utilisateur.getImage();
                System.out.println("imagePath1: " + imagePath);
                // Si l'image est déjà dans le dossier images, pas besoin de la copier
                if (imagePath.startsWith("/images/")) {
                    userUpdated.setImage(imagePath);
                } else {
                    // C'est une nouvelle image à partir du mobile
                    String fileName = UUID.randomUUID().toString() + ".png";
                    Path targetPath = Paths.get("src/main/resources/static/images/").resolve(fileName).normalize();
                    Files.createDirectories(targetPath.getParent());

                    // Copier le fichier
                    Files.copy(new ByteArrayInputStream(Base64.getDecoder().decode(imagePath)), targetPath, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("imagePath2: " + fileName);
                    userUpdated.setImage("/images/" + fileName);
                }
            } else {
                // Conserver l'image existante si aucune nouvelle image n'est fournie
                userUpdated.setImage(existingUser.get().getImage());
            }
            utilisateurService.updateUser(userUpdated);  // Assurez-vous que l'image est incluse dans cette mise à jour
            System.out.println("User updated with image: " + userUpdated.getImage());

            return ResponseEntity.ok("Utilisateur mis à jour avec succès.");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la mise à jour : " + e.getMessage());
        }
    }


    @PostMapping("/uploadImage")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Fichier vide");
            }

            String fileName = UUID.randomUUID().toString() + ".png";
            Path targetPath = Paths.get("src/main/resources/static/images/").resolve(fileName).normalize();
            Files.createDirectories(targetPath.getParent());

            // Copier le fichier
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Image uploaded successfully: " + fileName);
            return ResponseEntity.ok("/images/" + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors du téléchargement de l'image : " + e.getMessage());
        }
    }



}
