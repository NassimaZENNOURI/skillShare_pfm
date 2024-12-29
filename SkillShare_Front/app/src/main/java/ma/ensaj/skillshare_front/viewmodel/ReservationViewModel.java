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
import java.util.Map;

import ma.ensaj.skillshare_front.MyApp;
import ma.ensaj.skillshare_front.model.Reservation;
import ma.ensaj.skillshare_front.network.RetrofitInstance;
import ma.ensaj.skillshare_front.network.api.ReservationApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationViewModel extends AndroidViewModel {

    private MutableLiveData<List<Map<String, Object>>> reservationLiveData;
    private ReservationApi reservationApi;

    public ReservationViewModel(@NonNull Application application) {
        super(application);
        reservationLiveData = new MutableLiveData<>();
        reservationApi = RetrofitInstance.getInstance().create(ReservationApi.class);
    }

    // Méthode pour récupérer les notifications
    public LiveData<List<Map<String, Object>>> getReservation() {
        SharedPreferences sharedPreferences = MyApp.getInstance().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String t = sharedPreferences.getString("token", null);
        String token = "Bearer "+t;
        reservationApi.getReservation(token).enqueue(new Callback<List<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("ReservationViewModel", "Reservations récupérés : " + response.body().toString());

                    reservationLiveData.setValue(response.body());
                } else {
                    Log.d("ReservationViewModel", "Erreur dans la réponse : " + response.code());
                    reservationLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
                Log.d("ReservationViewModel", "Échec de la requête : " + t.getMessage());
                reservationLiveData.setValue(null);
            }
        });
        return reservationLiveData;
    }

    // Méthode pour accepter la réservation
    public void accepterReservation(int idReser) {
        SharedPreferences sharedPreferences = MyApp.getInstance().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String t = sharedPreferences.getString("token", null);
        String token = "Bearer "+t;
        reservationApi.accepter(idReser, token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("ReservationViewModel", "Réservation acceptée.");
                    getReservation();
                } else {
                    Log.d("ReservationViewModel", "Erreur dans la réponse : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("ReservationViewModel", "Échec de la requête : " + t.getMessage());
            }
        });
    }

    // Méthode pour refuser la réservation
    public void refuserReservation(int idReser) {
        SharedPreferences sharedPreferences = MyApp.getInstance().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String t = sharedPreferences.getString("token", null);
        String token = "Bearer "+t;
        reservationApi.refuser(idReser, token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("ReservationViewModel", "Réservation refusée.");
                    getReservation();
                } else {
                    Log.d("ReservationViewModel", "Erreur dans la réponse : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("ReservationViewModel", "Échec de la requête : " + t.getMessage());
            }
        });
    }



}
