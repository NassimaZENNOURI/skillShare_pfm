package ma.ensaj.skillshare_front;

import android.app.Application;

import com.auth0.android.jwt.JWT;

import ma.ensaj.skillshare_front.model.User;

public class MyApp extends Application {

    private static MyApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MyApp getInstance() {
        return instance;
    }


    public static User decodeToken(String token) {
        try {
            JWT jwt = new JWT(token);
            String email = jwt.getSubject().toString();
            int idUser = jwt.getClaim("idUser").asInt();

            return new User(idUser,email);
        } catch (Exception e) {
            return null;
        }
    }

}
