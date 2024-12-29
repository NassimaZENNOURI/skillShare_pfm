package ma.ensaj.skillshare_front.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.Map;

import ma.ensaj.skillshare_front.MyApp;
import ma.ensaj.skillshare_front.network.RetrofitInstance;
import ma.ensaj.skillshare_front.network.api.ProposedServiceApi;
import ma.ensaj.skillshare_front.network.api.UserApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {
    private MutableLiveData<List<Map<String, Object>>> servicesLiveData = new MutableLiveData<>();
    private UserApi apiService;

    public MainViewModel() {
        apiService = RetrofitInstance.getInstance().create(UserApi.class);
    }

    public MainViewModel(Application application) {
        apiService = RetrofitInstance.getInstance().create(UserApi.class);
    }

    public LiveData<List<Map<String, Object>>> getServicesLiveData() {
        return servicesLiveData;
    }

    public void searchAndFilterServices(Float minPrice, Float maxPrice, String category, String city, String searchText) {
        ProposedServiceApi proposedServiceApi = RetrofitInstance.getInstance().create(ProposedServiceApi.class);
        SharedPreferences sharedPreferences = MyApp.getInstance().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String t = sharedPreferences.getString("token", null);
        String token = "Bearer "+t;
        Call<List<Map<String, Object>>> call = proposedServiceApi.searchServices(token, minPrice, maxPrice, category, city, searchText);
        call.enqueue(new Callback<List<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    servicesLiveData.postValue(response.body());
                    System.out.println("Réponse API : " + response.body());
                    System.out.println("Mise à jour de LiveData avec : " + response.body());
                } else {
                    System.err.println("Erreur dans la réponse : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
                t.printStackTrace();
                System.err.println("Erreur dans l'appel API : " + t.getMessage());
            }
        });
    }

    public void searchServices(String searchText) {
        ProposedServiceApi proposedServiceApi = RetrofitInstance.getInstance().create(ProposedServiceApi.class);
        SharedPreferences sharedPreferences = MyApp.getInstance().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String t = sharedPreferences.getString("token", null);
        String token = "Bearer "+t;
        Call<List<Map<String, Object>>> call = proposedServiceApi.searchServiceByText(token, searchText);
        call.enqueue(new Callback<List<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    servicesLiveData.postValue(response.body());
                    System.out.println("Réponse API : " + response.body());
                    System.out.println("Mise à jour de LiveData avec : " + response.body());
                } else {
                    System.err.println("Erreur dans la réponse : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
                t.printStackTrace();
                System.err.println("Erreur dans l'appel API : " + t.getMessage());
            }
        });
    }


}