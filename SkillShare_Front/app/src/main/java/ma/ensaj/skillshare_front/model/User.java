package ma.ensaj.skillshare_front.model;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idUser;
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    private String image;
    private String localisation;
    private float noteMoyenne = 0.0f;

    public User() {
    }

    public User( String nom, String prenom, String email, String motDePasse, String localisation) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.localisation = localisation;
    }

    public User(int idUser ,String email){
        this.idUser=idUser;
        this.email=email;
    }

    public User( String nom, String prenom, String email, String motDePasse, String image, String localisation) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.image = image;
        this.localisation = localisation;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public float getNoteMoyenne() {
        return noteMoyenne;
    }

    public void setNoteMoyenne(float noteMoyenne) {
        this.noteMoyenne = noteMoyenne;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }
}
