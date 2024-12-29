package ma.ensaj.skillshare_front.network.api;

import java.util.List;

import ma.ensaj.skillshare_front.model.Feedback;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FeedbackApi {

    @GET("api/feedbacks/feedbacks/{idServicePropose}")
    Call<List<Feedback>> getFeedbacksForService(@Path("idServicePropose") int idServicePropose);


    @POST("api/feedbacks/add/{idServicePropose}")
    Call<ResponseBody> addFeedback(
            @Header("Authorization") String token,
            @Path("idServicePropose") int idServicePropose,
            @Body Feedback feedback
    );


}