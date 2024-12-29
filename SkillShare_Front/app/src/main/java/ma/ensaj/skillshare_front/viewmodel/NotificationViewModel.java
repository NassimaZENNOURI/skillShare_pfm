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
import ma.ensaj.skillshare_front.model.Notification;
import ma.ensaj.skillshare_front.network.RetrofitInstance;
import ma.ensaj.skillshare_front.network.api.NotificationApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationViewModel extends AndroidViewModel {

    private MutableLiveData<List<Notification>> notificationsLiveData;
    private NotificationApi notificationApi;

    public NotificationViewModel(@NonNull Application application) {
        super(application);
        notificationsLiveData = new MutableLiveData<>();
        notificationApi = RetrofitInstance.getInstance().create(NotificationApi.class);
    }

    public LiveData<List<Notification>> getNotifications() {
        SharedPreferences sharedPreferences = MyApp.getInstance().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String t = sharedPreferences.getString("token", null);
        String token = "Bearer "+t;
        notificationApi.getNotifications(token).enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("NotificationViewModel", "Notifications récupérés : " + response.body().toString());

                    notificationsLiveData.setValue(response.body());
                } else {
                    Log.d("NotificationViewModel", "Erreur dans la réponse : " + response.code());
                    notificationsLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                Log.d("NotificationViewModel", "Échec de la requête : " + t.getMessage());
                notificationsLiveData.setValue(null);
            }
        });
        return notificationsLiveData;
    }


    public void markAsRead(int idNotif) {
        notificationApi.markAsRead(idNotif).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d("Notification", "Notification marquée comme lue");
                } else {
                    Log.e("Notification", "Échec de la mise à jour");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Notification", "Erreur : " + t.getMessage());
            }
        });
    }

}

