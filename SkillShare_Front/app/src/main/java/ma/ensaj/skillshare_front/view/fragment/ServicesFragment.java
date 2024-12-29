package ma.ensaj.skillshare_front.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ma.ensaj.skillshare_front.R;
import ma.ensaj.skillshare_front.view.adapter.ServicesAdapter;
import ma.ensaj.skillshare_front.viewmodel.ProposedServiceViewModel;

public class ServicesFragment extends Fragment {

    private ProposedServiceViewModel servicesViewModel;
    private RecyclerView recyclerView;
    private ServicesAdapter servicesAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_services, container, false);

        recyclerView = view.findViewById(R.id.servicesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        servicesViewModel = new ViewModelProvider(this).get(ProposedServiceViewModel.class);
        int categoryId = getArguments().getInt("categoryId");
        servicesViewModel.loadServicesByCategory(categoryId);

        servicesViewModel.getServicesByIdCategory().observe(getViewLifecycleOwner(), services -> {
            servicesAdapter = new ServicesAdapter(services,servicesViewModel);
            recyclerView.setAdapter(servicesAdapter);
        });


        return view;
    }
}
