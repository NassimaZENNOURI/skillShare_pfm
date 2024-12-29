package ma.ensaj.skillshare_front.model;

public class ProposedService {

    private int idService;
    private String nomService;
    private String description;
    private float prix;
    //private User utilisateur; // Mappe l'entité Utilisateur
    private Category categories;
    private String nomUtilisateur;
    private String prenomUtilisateur;
    private String image;
    private String localisation;
    private String nomCategorie;


    public ProposedService() {
    }

    public ProposedService(int idService, String nomService, String description, float prix, String nomUtilisateur, Category categories, String prenomUtilisateur, String image, String localisation, String nomCategorie) {
        this.idService = idService;
        this.nomService = nomService;
        this.description = description;
        this.prix = prix;
        this.nomUtilisateur = nomUtilisateur;
        this.categories = categories;
        this.prenomUtilisateur = prenomUtilisateur;
        this.image = image;
        this.localisation = localisation;
        this.nomCategorie = nomCategorie;
    }

    public String getLocalisation() {
        return localisation;
    }

    public String getNomCategorie() {
        return nomCategorie;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public void setNomCategorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public String getPrenomUtilisateur() {
        return prenomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public void setPrenomUtilisateur(String prenomUtilisateur) {
        this.prenomUtilisateur = prenomUtilisateur;
    }

    public int getIdService() {
        return idService;
    }

    public void setIdService(int idService) {
        this.idService = idService;
    }

    public String getNomService() {
        return nomService;
    }

    public void setNomService(String nomService) {
        this.nomService = nomService;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    // public User getUtilisateur() {return utilisateur;}

    //public void setUtilisateur(User utilisateur) {this.utilisateur = utilisateur;}

    public Category getCategories() {
        return categories;
    }

    public void setCategories(Category categories) {
        this.categories = categories;
    }
}