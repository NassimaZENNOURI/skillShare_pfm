package com.ensaj.SkillShare.repository;

import com.ensaj.SkillShare.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
    Optional<Utilisateur> findByEmail(String email);

    /*@Query("SELECT u FROM Utilisateur u WHERE " +
           "ST_Distance_Sphere(point(u.longitude, u.latitude), " +
           "point(?1, ?2)) <= ?3")
    List<Utilisateur> findNearbyUsers(double longitude, double latitude, double radiusInMeters);*/
}
