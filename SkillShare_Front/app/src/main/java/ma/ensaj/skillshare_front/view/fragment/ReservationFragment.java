package ma.ensaj.skillshare_front.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import ma.ensaj.skillshare_front.view.adapter.ReservationAdapter;
import ma.ensaj.skillshare_front.viewmodel.ReservationViewModel;

public class ReservationFragment extends Fragment {

    private RecyclerView recyclerView;
    private ReservationAdapter adapter;
    private ReservationViewModel viewModel;

    public ReservationFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservation, container, false);

        recyclerView = view.findViewById(R.id.recyclerReservaions);

        // Initialisation du RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ReservationAdapter(new ArrayList<>(), viewModel); // Initialisez avec une liste vide
        recyclerView.setAdapter(adapter);

        // Configuration du ViewModel
        viewModel = new ViewModelProvider(this).get(ReservationViewModel.class);

        // Observer les données du ViewModel
        observeReservations();

        return view;
    }

    private void observeReservations() {
        viewModel.getReservation().observe(getViewLifecycleOwner(), new Observer<List<Map<String, Object>>>() {
            @Override
            public void onChanged(List<Map<String, Object>> reservations) {

                if (reservations != null && !reservations.isEmpty()) {
                    recyclerView.setVisibility(View.VISIBLE);
                    // Update the adapter with new data
                    adapter = new ReservationAdapter(reservations, viewModel);
                    recyclerView.setAdapter(adapter);
                } else {
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });
    }
}