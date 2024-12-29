package ma.ensaj.skillshare_front.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.auth0.android.jwt.JWT;

import ma.ensaj.skillshare_front.MyApp;
import ma.ensaj.skillshare_front.dto.AuthRequest;
import ma.ensaj.skillshare_front.dto.AuthResponse;
import ma.ensaj.skillshare_front.model.User;
import ma.ensaj.skillshare_front.network.api.UserApi;
import ma.ensaj.skillshare_front.network.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<AuthResponse> authResponseLiveData = new MutableLiveData<>();
    private MutableLiveData<User> authResponseLiveDataUser = new MutableLiveData<>();
    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private String token;

    public LiveData<AuthResponse> getAuthResponse() {
        return authResponseLiveData;
    }

    public LiveData<User> getUserLiveData() {
        return authResponseLiveDataUser;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }

    public User decodeToken(String token) {
        try {
            JWT jwt = new JWT(token);
            String email = jwt.getSubject().toString();
            int idUser = jwt.getClaim("idUser").asInt();

            return new User(idUser,email);
        } catch (Exception e) {
            return null;
        }
    }

    public void login(String email, String password) {
        AuthRequest authRequest = new AuthRequest(email, password);

        UserApi userApi = RetrofitInstance.getInstance().create(UserApi.class);
        Call<AuthResponse> call = userApi.loginUser(authRequest);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    authResponseLiveData.setValue(response.body());
                    // Stocker le token dans SharedPreferences
                    SharedPreferences sharedPreferences = MyApp.getInstance().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", response.body().getToken());
                    editor.apply();
                } else {
                    errorLiveData.setValue("Invalid email or password");
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                errorLiveData.setValue("Network error: " + t.getMessage());
            }
        });
    }

    public void getUserInfos() {
        // Récupérer le token
        SharedPreferences sharedPreferences = MyApp.getInstance().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        User user = decodeToken(token);
        //recuperer id user
        UserApi userApi = RetrofitInstance.getInstance().create(UserApi.class);
        Call<User> call = userApi.getUserById(user.getIdUser());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    authResponseLiveDataUser.setValue(response.body());
                } else {
                    errorLiveData.setValue("ERREUR");
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                errorLiveData.setValue("Network error: " + t.getMessage());
            }
        });
    }

}
