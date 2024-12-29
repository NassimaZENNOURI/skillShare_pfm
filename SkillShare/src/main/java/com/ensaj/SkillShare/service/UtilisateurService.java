package com.ensaj.SkillShare.service;

import com.ensaj.SkillShare.dto.AuthRequest;
import com.ensaj.SkillShare.dto.AuthResponse;
import com.ensaj.SkillShare.model.Utilisateur;
import com.ensaj.SkillShare.repository.UtilisateurRepository;
import com.ensaj.SkillShare.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
    public class UtilisateurService {

        @Autowired
        private UtilisateurRepository utilisateurRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        //Permet de vérifier si les identifiants (email et mot de passe) sont corrects.
        private AuthenticationManager authenticationManager;

        @Autowired
        private JwtUtil jwtUtil;

        public AuthResponse authenticate(AuthRequest request) {
            Optional<Utilisateur> utilisateur = utilisateurRepository.findByEmail(request.getEmail());
            authenticationManager.authenticate(
                    //Crée un objet avec ton email et ton mot de passe.
                    //Envoie cet objet à Spring Security pour vérifier les informations.
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getMotDePasse())
            );

        String token = jwtUtil.generateToken(utilisateur.get().getNom(),utilisateur.get().getPrenom() ,request.getEmail(), utilisateur.get().getIdUser());
        return new AuthResponse(token);

    }

        public List<Utilisateur> getAllUsers() {
            return utilisateurRepository.findAll();
        }

        public Optional<Utilisateur> findUserById(int id) {
            return utilisateurRepository.findById(id);
        }

        public Optional<Utilisateur> findUserByEmailAndPassword(String email, String password) {
            Optional<Utilisateur> user = utilisateurRepository.findByEmail(email);
            if (user.isPresent() && passwordEncoder.matches(password, user.get().getMotDePasse())) {
                return user;
            }
            return Optional.empty();
        }

        public Optional<Utilisateur> findUserByEmail(String email) {
            return utilisateurRepository.findByEmail(email);
        }

        public Utilisateur addUser(Utilisateur utilisateur) {
            if (utilisateur.getMotDePasse() == null || utilisateur.getMotDePasse().isEmpty()) {
                throw new IllegalArgumentException("Le mot de passe ne peut pas être null ou vide");
            }
            utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
            return utilisateurRepository.save(utilisateur);
        }


        public void updateUser(Utilisateur utilisateur) {
                utilisateurRepository.save(utilisateur);
        }



    }

