package ma.ensaj.skillshare_front.model;

import ma.ensaj.skillshare_front.model.User;

public class Notification {

    private int idNotif;
    private String contenue;
    private String dateEnvoie;
    private boolean statut;
    private User createur;       // Représente l'utilisateur créateur
    private User destinataire;   // Représente l'utilisateur destinataire

    // Constructeurs
    public Notification() {}

    public Notification(int idNotif, String contenue, String dateEnvoie, boolean statut, User createur, User destinataire) {
        this.idNotif = idNotif;
        this.contenue = contenue;
        this.dateEnvoie = dateEnvoie;
        this.statut = statut;
        this.createur = createur;
        this.destinataire = destinataire;
    }

    // Getters et Setters
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

    public String getDateEnvoie() {
        return dateEnvoie;
    }

    public void setDateEnvoie(String dateEnvoie) {
        this.dateEnvoie = dateEnvoie;
    }

    public boolean isStatut() {
        return statut;
    }

    public void setStatut(boolean statut) {
        this.statut = statut;
    }

    public User getCreateur() {
        return createur;
    }

    public void setCreateur(User createur) {
        this.createur = createur;
    }

    public User getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(User destinataire) {
        this.destinataire = destinataire;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "idNotif=" + idNotif +
                ", contenue='" + contenue + '\'' +
                ", dateEnvoie=" + dateEnvoie +
                ", statut=" + statut +
                ", createur=" + createur +
                ", destinataire=" + destinataire +
                '}';
    }
}
