package com.ensaj.SkillShare.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idReser;

	private LocalDateTime dateHeure;
	//private boolean statut;
	private String statut;

	// Première clé étrangère : le créateur du feedback
	@ManyToOne
	@JoinColumn(name = "idCreateur", referencedColumnName = "idUser")
	private Utilisateur createur;

	// Deuxième clé étrangère : le destinataire du feedback
	@ManyToOne
	@JoinColumn(name = "idDestinataire", referencedColumnName = "idUser")
	private Utilisateur destinataire;

	@ManyToOne
	@JoinColumn(name = "idService")
	private ServicePropose service;


	public int getIdReser() {
		return idReser;
	}

	public void setIdReser(int idReser) {
		this.idReser = idReser;
	}

	public LocalDateTime getDateHeure() {
		return dateHeure;
	}

	public void setDateHeure(LocalDateTime dateHeure) {
		this.dateHeure = dateHeure;
	}



	public Utilisateur getCreateur() {
		return createur;
	}

	public void setCreateur(Utilisateur createur) {
		this.createur = createur;
	}

	public Utilisateur getDestinataire() {
		return destinataire;
	}

	public void setDestinataire(Utilisateur destinataire) {
		this.destinataire = destinataire;
	}

	public ServicePropose getService() {
		return service;
	}

	public void setService(ServicePropose service) {
		this.service = service;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statutR) {
		this.statut = statutR;
	}


}