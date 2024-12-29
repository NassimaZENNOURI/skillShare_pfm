package ma.ensaj.skillshare_front.view.adapter;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ma.ensaj.skillshare_front.MyApp;
import ma.ensaj.skillshare_front.R;
import ma.ensaj.skillshare_front.model.ProposedService;
import ma.ensaj.skillshare_front.network.api.ReservationApi;
import ma.ensaj.skillshare_front.network.RetrofitInstance;
import ma.ensaj.skillshare_front.view.activity.FeedbackActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProposedServiceAdapter extends RecyclerView.Adapter<ProposedServiceAdapter.ServiceViewHolder> {

    private List<ProposedService> ProposedServiceList;
    private Context context;

    public ProposedServiceAdapter(List<ProposedService> serviceList, Context context) {
        this.ProposedServiceList = serviceList;
        this.context = context;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.proposedservice_item, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        ProposedService service = ProposedServiceList.get(position);
        holder.nameTextView.setText(service.getNomService());
        holder.descriptionTextView.setText(service.getDescription());
        String prixText = String.format(Locale.getDefault(), "%.2f", service.getPrix());
        holder.prixTextView.setText(prixText+" DH");
        String fullName = service.getPrenomUtilisateur() + " " + service.getNomUtilisateur();
        holder.fullName.setText(fullName);
        holder.categorieTextView.setText(service.getNomCategorie());
        holder.localisationTextView.setText(service.getLocalisation());
        String baseUrl = "http://192.168.0.8:3600/skillShare"+service.getImage();
        Glide.with(holder.itemView.getContext())
                .load(baseUrl)  // URL de l'image
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_launcher_background)
                .into(holder.profileImage);

        Log.d("TEEEESSSSST", baseUrl);
        // Gérer le clic sur le bouton pour accéder aux feedbacks
        holder.feedbackEditText.setOnClickListener(v -> {
            // Ouvrir FeedbackActivity et passer l'ID du service
            Intent intent = new Intent(v.getContext(), FeedbackActivity.class);
            intent.putExtra("idServicePropose", service.getIdService()); // Assurez-vous que cette méthode existe dans ProposedService
            v.getContext().startActivity(intent);
        });

        // Check reservation status
        checkReservationStatus(holder, service.getIdService());

        // Gérer le clic sur le bouton de réservation
        holder.reservationButton.setOnClickListener(v -> {
            showDateTimePicker(holder, service.getIdService());
        });
    }

    @Override
    public int getItemCount() {
        return ProposedServiceList.size();
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView fullName;
        TextView descriptionTextView;
        TextView prixTextView;
        TextView reservationButton;
        TextView feedbackEditText;
        ImageView profileImage;
        ImageView reservationIcon;
        TextView localisationTextView;
        TextView categorieTextView;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.userName);
            nameTextView = itemView.findViewById(R.id.serviceTitle);
            descriptionTextView = itemView.findViewById(R.id.serviceDescription);
            prixTextView = itemView.findViewById(R.id.servicePrix);
            reservationButton = itemView.findViewById(R.id.reservationButton);
            feedbackEditText = itemView.findViewById(R.id.feedbackText);
            profileImage =itemView.findViewById(R.id.profileIcon);
            reservationIcon = itemView.findViewById(R.id.reservationIcon);
            localisationTextView = itemView.findViewById(R.id.serviceLocalisation);
            categorieTextView = itemView.findViewById(R.id.serviceCategorie);
        }
    }

    private void showDateTimePicker(ServiceViewHolder holder, int idServicePropose) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                        String dateHeure = sdf.format(calendar.getTime());
                        reserveService(holder, idServicePropose, dateHeure);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void reserveService(ServiceViewHolder holder, int idServicePropose, String dateHeure) {
        ReservationApi reservationApi = RetrofitInstance.getInstance().create(ReservationApi.class);
        SharedPreferences sharedPreferences = MyApp.getInstance().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String t = sharedPreferences.getString("token", null);
        String token = "Bearer "+t; // Remplacez par votre token JWT
        Call<ResponseBody> call = reservationApi.reserverService(idServicePropose,token ,dateHeure);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    holder.reservationButton.setText("Reservated");
                    holder.reservationButton.setEnabled(false);
                    holder.reservationButton.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.desactive));
                    // Changer l'icône de l'ImageView
                    holder.reservationIcon.setImageResource(R.drawable.ic_check);
                    Toast.makeText(context, "Réservation effectuée avec succès.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Échec de la réservation.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Erreur de connexion.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkReservationStatus(ServiceViewHolder holder, int idServicePropose) {
        ReservationApi reservationApi = RetrofitInstance.getInstance().create(ReservationApi.class);
        SharedPreferences sharedPreferences = MyApp.getInstance().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String t = sharedPreferences.getString("token", null);
        String token = "Bearer " + t;

        Call<Boolean> call = reservationApi.checkReservation(idServicePropose, token);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean isReserved = response.body();
                    if (isReserved) {
                        holder.reservationIcon.setImageResource(R.drawable.ic_check);
                        holder.reservationButton.setText("Reserved");
                        holder.reservationButton.setEnabled(false);
                        holder.reservationButton.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.desactive));
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                // Handle failure
                Toast.makeText(context, "Error checking reservation status", Toast.LENGTH_SHORT).show();
            }
        });
    }
}



//package ma.ensaj.skillshare_front.view.adapter;
//
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.List;
//import java.util.Locale;
//
//import ma.ensaj.skillshare_front.R;
//import ma.ensaj.skillshare_front.model.ProposedService;
//import ma.ensaj.skillshare_front.view.activity.FeedbackActivity;
//
//public class ProposedServiceAdapter extends RecyclerView.Adapter<ProposedServiceAdapter.ServiceViewHolder>{
//
//    private List<ProposedService> ProposedServiceList;
//
//    public ProposedServiceAdapter(List<ProposedService> serviceList) {
//        this.ProposedServiceList = serviceList;
//    }
//
//    @NonNull
//    @Override
//    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.proposedservice_item, parent, false);
//        return new ServiceViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
//        ProposedService service = ProposedServiceList.get(position);
//        holder.nameTextView.setText(service.getNomService());
//        holder.descriptionTextView.setText(service.getDescription());
//        String prixText = String.format(Locale.getDefault(), "%.2f", service.getPrix());
//        holder.prixTextView.setText(prixText);
//        String fullName = service.getPrenomUtilisateur() + " " + service.getNomUtilisateur();
//        holder.fullName.setText(fullName);
//
//        // Gérer le clic sur le bouton pour accéder aux feedbacks
//        holder.feedbackEditText.setOnClickListener(v -> {
//            Intent intent = new Intent(v.getContext(), FeedbackActivity.class);
//            intent.putExtra("idServicePropose", service.getIdService());
//            v.getContext().startActivity(intent);
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return ProposedServiceList.size();
//    }
//
//    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
//        TextView nameTextView;
//        TextView fullName;
//        TextView descriptionTextView;
//        TextView prixTextView;
//        TextView reservationButton;
//        TextView feedbackEditText;
//
//        public ServiceViewHolder(@NonNull View itemView) {
//            super(itemView);
//            fullName = itemView.findViewById(R.id.userName);
//            nameTextView = itemView.findViewById(R.id.serviceTitle);
//            descriptionTextView = itemView.findViewById(R.id.serviceDescription);
//            prixTextView = itemView.findViewById(R.id.servicePrix);
//            reservationButton = itemView.findViewById(R.id.reservationButton);
//            feedbackEditText = itemView.findViewById(R.id.feedbackText);
//        }
//    }
//}