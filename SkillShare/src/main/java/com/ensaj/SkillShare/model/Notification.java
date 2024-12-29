package com.ensaj.SkillShare.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Notification {

    public int getIdNotif() {
		return idNotif;
	}

	public void setIdNotif(int idNotif) {
		this.idNotif = idNotif;
	}

	public String getContenue() {
		return contenue;
	}

	public void setContenue(String contenue) {
		this.contenue = contenue;
	}

	public LocalDateTime getDateEnvoie() {
		return dateEnvoie;
	}

	public void setDateEnvoie(LocalDateTime dateEnvoie) {
		this.dateEnvoie = dateEnvoie;
	}

	public boolean isStatut() {
		return statut;
	}

	public void setStatut(boolean statut) {
		this.statut = statut;
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

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idNotif;

    private String contenue;
    private LocalDateTime dateEnvoie;
    private boolean statut;

    // Première clé étrangère : le créateur du feedback
    @ManyToOne
    @JoinColumn(name = "idCreateur", referencedColumnName = "idUser")
    private Utilisateur createur;

    // Deuxième clé étrangère : le destinataire du feedback
    @ManyToOne
    @JoinColumn(name = "idDestinataire", referencedColumnName = "idUser")
    private Utilisateur destinataire;
    
}
