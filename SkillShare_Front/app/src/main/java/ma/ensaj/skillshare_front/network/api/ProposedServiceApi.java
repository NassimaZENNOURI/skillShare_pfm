package ma.ensaj.skillshare_front.network.api;

import java.util.List;
import java.util.Map;

import ma.ensaj.skillshare_front.model.ProposedService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProposedServiceApi {

    @GET("api/services/allServices")
    Call<List<ProposedService>> getProposedServices(@Header("Authorization") String token);


    // Récupérer les services par catégorie avec détails (version authentifiée)
    @GET("api/services/allServices/{idCategorie}")
    Call<List<Map<String, Object>>> getServicesByCategory(
            @Path("idCategorie") int idCategorie,
            @Header("Authorization") String token
    );

    // Récupérer les services par catégorie avec détails (version avec idUser explicite)
    @GET("api/services/allServices/{idCategorie}/{idUser}")
    Call<List<Map<String, Object>>> getServicesByCategoryAndUser(
            @Path("idCategorie") int idCategorie,
            @Path("idUser") int idUser
    );

    // Récupérer un service spécifique par son ID
    @GET("api/services/service/{idCategorie}/{idService}")
    Call<List<Object[]>> getServiceById(
            @Path("idCategorie") int idCategorie,
            @Path("idService") int idService,
            @Header("Authorization") String token
    );

    // Ajouter un nouveau service
    @POST("api/services/save")
    Call<ResponseBody> addService(
            @Body Map<String, Object> serviceData,
            @Header("Authorization") String token
    );

    // Supprimer un service
    @DELETE("api/services/delete/{idService}")
    Call<ResponseBody> deleteService(
            @Path("idService") int idService
    );

    // Mettre à jour un service
    @PUT("api/services/update/{idService}")
    Call<String> updateService(
            @Path("idService") int idService,
            @Body ProposedService service,
            @Header("Authorization") String token
    );

    @GET("api/services/search")
    Call<List<Map<String, Object>>> searchServices(
            @Header("Authorization") String token,
            @Query("minPrix") Float minPrix,
            @Query("maxPrix") Float maxPrix,
            @Query("categorie") String categorie,
            @Query("localisation") String localisation,
            @Query("searchText") String searchText
    );

    @GET("api/services/search")
    Call<List<Map<String, Object>>> searchServiceByText(
            @Header("Authorization") String token,
            @Query("searchText") String searchText
    );
}
