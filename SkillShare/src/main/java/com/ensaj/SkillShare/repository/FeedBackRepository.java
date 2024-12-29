package com.ensaj.SkillShare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ensaj.SkillShare.model.Feedback;
import com.ensaj.SkillShare.model.ServicePropose;

import java.util.*;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedBackRepository extends JpaRepository<Feedback, Integer> {
  
	@Query("SELECT f.idFeed, f.commentaire, f.note, f.date, u.nom, u.prenom, u.image " +
	        "FROM Feedback f " +
	        "JOIN f.createur u " +
	        "WHERE f.service.idService =:idService")
	List<Object[]> findByService(@Param("idService") int idService);

}
