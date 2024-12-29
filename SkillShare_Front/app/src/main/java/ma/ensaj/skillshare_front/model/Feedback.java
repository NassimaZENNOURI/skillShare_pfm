package ma.ensaj.skillshare_front.model;

public class Feedback {
    private String nomUtilisateur;
    private String prenomUtilisateur;
    private float note;
    private String commentaire;
    private String date;
    private int idFeed;
    private String image ;

    public Feedback(){}

    public Feedback(String commentaire, String date, String nomUtilisateur, float note, String prenomUtilisateur, String image) {
        this.commentaire = commentaire;
        this.date = date;
        this.nomUtilisateur = nomUtilisateur;
        this.note = note;
        this.prenomUtilisateur = prenomUtilisateur;
        this.image = image;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public String getDate() {
        return date;
    }

    public int getIdFeed() {
        return idFeed;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public float getNote() {
        return note;
    }

    public String getPrenomUtilisateur() {
        return prenomUtilisateur;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setIdFeed(int idFeed) {
        this.idFeed = idFeed;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public void setNote(float note) {
        this.note = note;
    }

    public void setPrenomUtilisateur(String prenomUtilisateur) {
        this.prenomUtilisateur = prenomUtilisateur;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}