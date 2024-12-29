package ma.ensaj.skillshare_front.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.Map;

import ma.ensaj.skillshare_front.MyApp;
import ma.ensaj.skillshare_front.model.ProposedService;
import ma.ensaj.skillshare_front.network.api.ProposedServiceApi;
import ma.ensaj.skillshare_front.network.RetrofitInstance;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProposedServiceViewModel extends ViewModel {

    private final MutableLiveData<List<ProposedService>> services = new MutableLiveData<>();
    private final MutableLiveData<List<Map<String, Object>>> servicesByIdCategory = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isServiceDeleted = new MutableLiveData<>();
    private String token;

    private final ProposedServiceApi serviceApi;

    public ProposedServiceViewModel() {
        serviceApi = RetrofitInstance.getInstance().create(ProposedServiceApi.class);
        loadServices();
        SharedPreferences sharedPreferences = MyApp.getInstance().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String t = sharedPreferences.getString("token", null);
        token = "Bearer "+t;
    }

    public LiveData<List<ProposedService>> getServices() {
        return services;
    }
    public LiveData<List<Map<String, Object>>> getServicesByIdCategory() {
        return servicesByIdCategory;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void loadServices() {
        isLoading.setValue(true);
        SharedPreferences sharedPreferences = MyApp.getInstance().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String t = sharedPreferences.getString("token", null);
        String token = "Bearer "+t;
        serviceApi.getProposedServices(token).enqueue(new Callback<List<ProposedService>>() {
            @Override
            public void onResponse(Call<List<ProposedService>> call, Response<List<ProposedService>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    services.setValue(response.body());
                } else {
                    errorMessage.setValue("Erreur lors du chargement des services");
                }
            }

            @Override
            public void onFailure(Call<List<ProposedService>> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Erreur réseau: " + t.getMessage());
            }
        });
    }

    //loadServicesByCategory(int categoryId, String token)
    public void loadServicesByCategory(int categoryId) {
        isLoading.setValue(true);
        serviceApi.getServicesByCategory(categoryId, token).enqueue(new Callback<List<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    servicesByIdCategory.setValue(response.body());
                } else {
                    errorMessage.setValue("Erreur lors du chargement des services par catégorie");
                }
            }

            @Override
            public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Erreur réseau: " + t.getMessage());
            }
        });
    }
    public void deleteService(int idService, int idCategorie) {
        Log.e("ViewModel","categorie Id1  " + idCategorie);
        serviceApi.deleteService(idService).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.e("ViewModel", "Delete successful for idService: " + idService);
                    isServiceDeleted.setValue(true);
                    Log.e("ViewModel", "categorie Id2  " + idCategorie); // Vérifiez si ceci est exécuté
                    loadServicesByCategory(idCategorie); // Recharge les services pour la catégorie
                } else {
                    Log.e("ViewModel", "Delete failed with response code: " + response.code());
                    isServiceDeleted.setValue(false);
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ViewModel", "Delete service failed: " + t.getMessage());
                isServiceDeleted.setValue(false);
            }

        });
    }
    public LiveData<Boolean> getIsServiceDeleted() {
        return isServiceDeleted;
    }

    public void getServiceById(int categoryId, String token, int serviceId) {
        isLoading.setValue(true);
        serviceApi.getServiceById(categoryId, serviceId, token).enqueue(new Callback<List<Object[]>>() {
            @Override
            public void onResponse(Call<List<Object[]>> call, Response<List<Object[]>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    // Traiter la réponse si nécessaire
                } else {
                    errorMessage.setValue("Erreur lors du chargement du service");
                }
            }

            @Override
            public void onFailure(Call<List<Object[]>> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Erreur réseau: " + t.getMessage());
            }
        });
    }

    public void refreshServices() {
        loadServices();
    }

    public void clearError() {
        errorMessage.setValue(null);
    }
}
