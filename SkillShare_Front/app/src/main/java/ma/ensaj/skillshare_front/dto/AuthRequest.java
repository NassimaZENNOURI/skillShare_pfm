package ma.ensaj.skillshare_front.dto;

public class AuthRequest {

    private String email;
    private String motDePasse;

    public AuthRequest(String email, String password) {
        this.email = email;
        this.motDePasse = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return motDePasse;
    }

    public void setPassword(String password) {
        this.motDePasse = password;
    }
}
