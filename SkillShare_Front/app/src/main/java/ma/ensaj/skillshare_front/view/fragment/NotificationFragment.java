package ma.ensaj.skillshare_front.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ma.ensaj.skillshare_front.R;
import ma.ensaj.skillshare_front.model.Notification;
import ma.ensaj.skillshare_front.view.adapter.NotificationAdapter;
import ma.ensaj.skillshare_front.viewmodel.NotificationViewModel;

public class NotificationFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private NotificationViewModel viewModel;

    public NotificationFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        recyclerView = view.findViewById(R.id.recyclerNotifications);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NotificationAdapter(null);
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(NotificationViewModel.class);

        observeNotifications();

        return view;
    }

    private void observeNotifications() {
        viewModel.getNotifications().observe(getViewLifecycleOwner(), new Observer<List<Notification>>() {
            @Override
            public void onChanged(List<Notification> notifications) {
                if (notifications != null && !notifications.isEmpty()) {
                    Log.d("NotificationFragment", "Notifications observées : " + notifications);

                    recyclerView.setVisibility(View.VISIBLE);
                    adapter.updateNotifications(notifications); // Appelle la méthode de l'adaptateur
                } else {
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });
    }



}