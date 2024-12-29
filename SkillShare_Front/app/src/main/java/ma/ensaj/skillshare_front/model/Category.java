package ma.ensaj.skillshare_front.model;

public class Category {
    private int idCategorie;
    private String categorie;


    public int getIdCategorie() {
        return idCategorie;
    }

    public void setIdCategorie(int idCategorie) {
        this.idCategorie = idCategorie;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    // Redéfinir toString pour l'adaptateur du Spinner
    @Override
    public String toString() {
        return categorie;
    }

}