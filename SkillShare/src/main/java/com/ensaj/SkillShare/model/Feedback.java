package com.ensaj.SkillShare.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idFeed;

    private float note = 0.0f;
    private String commentaire;
    
    public int getIdFeed() {
		return idFeed;
	}

	public void setIdFeed(int idFeed) {
		this.idFeed = idFeed;
	}

	public float getNote() {
		return note;
	}

	public void setNote(float note) {
		this.note = note;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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

	@Temporal(TemporalType.TIMESTAMP)
    private Date date;

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
}
