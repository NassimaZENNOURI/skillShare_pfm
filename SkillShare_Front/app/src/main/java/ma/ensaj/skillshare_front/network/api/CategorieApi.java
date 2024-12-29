package ma.ensaj.skillshare_front.network.api;

import java.util.List;

import ma.ensaj.skillshare_front.model.Category;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CategorieApi {

    @GET("api/categories/allCategories")
    Call<List<Category>> getCategories();

    @POST("api/categories/save")
    Call<ResponseBody> ajouterCategorie(@Body Category categorie);


}