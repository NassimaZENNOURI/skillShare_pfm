package ma.ensaj.skillshare_front.network.api;

import java.util.List;

import ma.ensaj.skillshare_front.model.Notification;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NotificationApi {

    @GET("api/notifications/unread")
    Call<List<Notification>> getNotifications(@Header("Authorization") String token);

    @PUT("read/{id}")
    Call<String> markAsRead(@Path("id") int idNotif);

}