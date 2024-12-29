package ma.ensaj.skillshare_front.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ma.ensaj.skillshare_front.model.User;
import ma.ensaj.skillshare_front.network.RetrofitInstance;
import ma.ensaj.skillshare_front.network.api.UserApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterViewModel extends ViewModel {

    private MutableLiveData<String> authResponse;
    private MutableLiveData<String> error;

    public RegisterViewModel() {
        authResponse = new MutableLiveData<>();
        error = new MutableLiveData<>();
    }

    public LiveData<String> getAuthResponse() {
        return authResponse;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void registerUser(User user) {
        UserApi userApi = RetrofitInstance.getInstance().create(UserApi.class);
        Call<ResponseBody> call = userApi.registerUser(user);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // Lire le contenu de la réponse comme chaîne
                        String successMessage = response.body().string();
                        authResponse.postValue(successMessage);
                        Log.d("RegisterViewModel", "Réponse de l'inscription : " + successMessage);
                    } catch (Exception e) {
                        Log.e("RegisterViewModel", "Erreur lors de la lecture de la réponse : " + e.getMessage());
                        error.postValue("Erreur de lecture : " + e.getMessage());
                    }
                } else {
                    error.postValue("Erreur dans la réponse : " + response.message() + " (Code: " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                error.postValue("Erreur réseau : " + t.getMessage());
            }
        });
    }
}