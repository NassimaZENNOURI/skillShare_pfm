package ma.ensaj.skillshare_front.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ma.ensaj.skillshare_front.MyApp;
import ma.ensaj.skillshare_front.model.Feedback;
import ma.ensaj.skillshare_front.network.RetrofitInstance;
import ma.ensaj.skillshare_front.network.api.FeedbackApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackViewModel extends AndroidViewModel {

    private MutableLiveData<List<Feedback>> feedbacksLiveData;
    private FeedbackApi feedbackApi;

    public FeedbackViewModel(Application application) {
        super(application);
        feedbacksLiveData = new MutableLiveData<>();
        feedbackApi = RetrofitInstance.getInstance().create(FeedbackApi.class); // Initialisation du service FeedbackApi
    }

    // Méthode pour récupérer les feedbacks
    public LiveData<List<Feedback>> getFeedbacks(int idServicePropose) {
        feedbackApi.getFeedbacksForService(idServicePropose).enqueue(new Callback<List<Feedback>>() {
            @Override
            public void onResponse(Call<List<Feedback>> call, Response<List<Feedback>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("FeedbackViewModel", "Feedbacks récupérés : " + response.body().toString());

                    feedbacksLiveData.setValue(response.body());
                } else {
                    Log.d("FeedbackViewModel", "Erreur dans la réponse : " + response.code());
                    feedbacksLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Feedback>> call, Throwable t) {
                Log.d("FeedbackViewModel", "Échec de la requête : " + t.getMessage());
                feedbacksLiveData.setValue(null);
            }
        });
        return feedbacksLiveData;
    }


    public void addFeedback(int idServicePropose, Feedback feedback) {
        // Initialisation de l'API Retrofit
        FeedbackApi feedbackApi = RetrofitInstance.getInstance().create(FeedbackApi.class);

        // Récupération du token depuis SharedPreferences
        SharedPreferences sharedPreferences = MyApp.getInstance().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        if (token != null) {
            String bearerToken = "Bearer " + token;

            // Appel de l'API pour ajouter le feedback
            Call<ResponseBody> call = feedbackApi.addFeedback(bearerToken, idServicePropose, feedback);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            // Récupérer le message de la réponse
                            String successMessage = response.body().string();
                            Log.d("FeedbackViewModel", "Feedback ajouté avec succès : " + successMessage);
                            getFeedbacks(idServicePropose);
                        } catch (Exception e) {
                            Log.e("FeedbackViewModel", "Erreur lors de la lecture de la réponse : " + e.getMessage());
                        }
                    } else {
                        Log.e("FeedbackViewModel", "Erreur lors de l'ajout : Code " + response.code() + ", Message : " + response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    Log.e("FeedbackViewModel", "Échec de l'ajout du feedback : " + t.getMessage());
                }
            });
        } else {
            Log.e("FeedbackViewModel", "Token manquant ou invalide !");
        }
    }




}