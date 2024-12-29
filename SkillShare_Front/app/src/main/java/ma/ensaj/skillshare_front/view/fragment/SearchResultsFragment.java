package ma.ensaj.skillshare_front.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ma.ensaj.skillshare_front.R;
import ma.ensaj.skillshare_front.view.adapter.SearchAdapter;
import ma.ensaj.skillshare_front.viewmodel.MainViewModel;

public class SearchResultsFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchAdapter adapter;
    private MainViewModel mainViewModel;

    public SearchResultsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_results_fragment, container, false);

        // RecyclerView setup
        recyclerView = view.findViewById(R.id.searchResultrecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialiser l'adaptateur avec une liste vide
        adapter = new SearchAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Setup ViewModel and observe data
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        mainViewModel.getServicesLiveData().observe(getViewLifecycleOwner(), new Observer<List<Map<String, Object>>>() {
            @Override
            public void onChanged(List<Map<String, Object>> services) {
                if (services != null && !services.isEmpty()) {
                    System.out.println("Données reçues : " + services);
                    adapter.updateData(services);
                } else {
                    System.out.println("Liste de services vide ou nulle !");
                }
            }
        });

        return view;
    }
}