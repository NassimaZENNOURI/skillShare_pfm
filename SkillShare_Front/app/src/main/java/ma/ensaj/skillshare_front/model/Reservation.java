package ma.ensaj.skillshare_front.model;

import java.time.LocalDateTime;

public class Reservation {

    private Long id;
    private Long idService;
    private Long idUtilisateur;
    private LocalDateTime dateHeure;
    private String status = "PENDING";
    private boolean accepted = false;

    private String date;
    private int idReser;
    private String prenom;
    private String nom;
    private String nomService;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIdReser() {
        return idReser;
    }

    public void setIdReser(int idReser) {
        this.idReser = idReser;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNomService() {
        return nomService;
    }

    public void setNomService(String nomService) {
        this.nomService = nomService;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Reservation() {
    }

    public Reservation(boolean accepted, String date, LocalDateTime dateHeure, Long id, int idReser, Long idService, Long idUtilisateur, String nom, String nomService, String status, String prenom) {
        this.accepted = accepted;
        this.date = date;
        this.dateHeure = dateHeure;
        this.id = id;
        this.idReser = idReser;
        this.idService = idService;
        this.idUtilisateur = idUtilisateur;
        this.nom = nom;
        this.nomService = nomService;
        this.status = status;
        this.prenom = prenom;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdService() {
        return idService;
    }

    public void setIdService(Long idService) {
        this.idService = idService;
    }

    public Long getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(Long idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public LocalDateTime getDateHeure() {
        return dateHeure;
    }

    public void setDateHeure(LocalDateTime dateHeure) {
        this.dateHeure = dateHeure;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }


}