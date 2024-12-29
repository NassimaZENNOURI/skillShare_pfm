package ma.ensaj.skillshare_front.network.api;

import android.app.appsearch.SearchResult;

import java.util.List;
import java.util.Map;

import ma.ensaj.skillshare_front.dto.AuthRequest;
import ma.ensaj.skillshare_front.dto.AuthResponse;
import ma.ensaj.skillshare_front.model.User;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserApi {

    @POST("api/utilisateurs/login")
    Call<AuthResponse> loginUser(@Body AuthRequest authRequest);

    @GET("api/utilisateurs/{id}")
    Call<User> getUserById(@Path("id") int id);

    @POST("api/utilisateurs/add")
    Call<ResponseBody> registerUser(@Body User user);

    @PUT("api/utilisateurs/update/{id}")
    Call<ResponseBody> updateUser(@Path("id") int id, @Body User user);

    @Multipart
    @POST("api/utilisateurs/uploadImage")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part file);


    @GET("search")
    Call<List<SearchResult>> search(@Query("q") String query);
}
