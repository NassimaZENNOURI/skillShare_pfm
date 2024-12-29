package ma.ensaj.skillshare_front.view.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import ma.ensaj.skillshare_front.MyApp;
import ma.ensaj.skillshare_front.R;
import ma.ensaj.skillshare_front.model.ProposedService;
import ma.ensaj.skillshare_front.network.api.ProposedServiceApi;
import ma.ensaj.skillshare_front.view.adapter.ProposedServiceAdapter;
import ma.ensaj.skillshare_front.network.RetrofitInstance;
import ma.ensaj.skillshare_front.viewmodel.ProposedServiceViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProposedServiceAdapter proposedServiceAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fetchServices();

        return view;
    }

    private void fetchServices() {
        ProposedServiceApi proposedServiceApi = RetrofitInstance.getInstance().create(ProposedServiceApi.class);
        SharedPreferences sharedPreferences = MyApp.getInstance().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String t = sharedPreferences.getString("token", null);
        String token = "Bearer "+t;
        proposedServiceApi.getProposedServices(token).enqueue(new Callback<List<ProposedService>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProposedService>> call, @NonNull Response<List<ProposedService>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    proposedServiceAdapter = new ProposedServiceAdapter(response.body(),getContext());
                    recyclerView.setAdapter(proposedServiceAdapter);
                } else {
                    Toast.makeText(getContext(), "Erreur de chargement des services", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProposedService>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Échec de connexion: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}