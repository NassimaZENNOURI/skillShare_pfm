package ma.ensaj.skillshare_front.view.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import ma.ensaj.skillshare_front.R;
import ma.ensaj.skillshare_front.viewmodel.ReservationViewModel;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {

    private List<Map<String, Object>> reservationList;
    private final SimpleDateFormat inputFormat;
    private final SimpleDateFormat outputFormat;
    private ReservationViewModel viewModel;

    public ReservationAdapter(List<Map<String, Object>> reservationList, ReservationViewModel viewModel) {
        this.reservationList = reservationList != null ? reservationList : new ArrayList<>();
        this.viewModel = viewModel;
        // Format for the date returned from the API (e.g., "2024-12-25T21:51:12")
        this.inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        this.inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        // Desired format for displaying the date (e.g., "Dec 25, 2024 09:51 PM")
        this.outputFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault());
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reservation, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Map<String, Object> reservation = reservationList.get(position);

        // Get data from the map
        String nom = (String) reservation.get("nom");
        String prenom = (String) reservation.get("prenom");
        String nomService = (String) reservation.get("nomService");
        String dateHeure = (String) reservation.get("date");
        String baseUrl = "http://192.168.0.8:3600/skillShare"+reservation.get("image");
        Glide.with(holder.itemView.getContext())
                .load(baseUrl)  // URL de l'image
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_launcher_background)
                .into(holder.imageCreator);
        Log.d("Image_Url1",baseUrl);
        // Vérification de la présence de "idreser" et gestion des types
        Object idReserObject = reservation.get("idreser");
        final int idReser = (idReserObject instanceof Double)
                ? ((Double) idReserObject).intValue()  // Convertir en int si c'est un Double
                : (idReserObject instanceof Integer)
                ? (Integer) idReserObject  // Si c'est déjà un Integer, le garder tel quel
                : 0;  // Valeur par défaut en cas de problème

        if (idReser == 0) {
            Log.e("ReservationAdapter", "idreser is null or invalid for reservation at position " + position);
        }
        Log.e("ReservationAdapter", "idreser  " + idReser);

        // Format the date
        String formattedDate = formatDate(dateHeure);

        // Format the content
        String fullName = nom + " " + prenom;
        String contenue = fullName + " has made a reservation for you for Service " + nomService + " on " + formattedDate;
        holder.textContent.setText(contenue);

        // Set click listeners for the buttons
        holder.btnRefuser.setOnClickListener(v -> {
            viewModel.refuserReservation(idReser); // Appeler la méthode pour refuser la réservation
        });

        holder.btnAccepter.setOnClickListener(v -> {
            viewModel.accepterReservation(idReser); // Appeler la méthode pour accepter la réservation
        });
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    private String formatDate(String date) {
        try {
            // Parse the date from the API format
            Date parsedDate = inputFormat.parse(date);
            // Format the parsed date into the desired output format
            if (parsedDate != null) {
                return outputFormat.format(parsedDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date; // Return the original date in case of parsing error
    }

    public static class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView textContent;
        Button btnRefuser;
        Button btnAccepter;
        ShapeableImageView imageCreator;


        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            textContent = itemView.findViewById(R.id.textContent);
            btnAccepter = itemView.findViewById(R.id.btnAccepter);
            btnRefuser = itemView.findViewById(R.id.btnRefuser);
            imageCreator = itemView.findViewById(R.id.imageCreator);
        }
    }
}