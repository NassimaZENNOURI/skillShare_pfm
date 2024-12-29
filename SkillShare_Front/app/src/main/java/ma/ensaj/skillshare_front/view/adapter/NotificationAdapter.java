package ma.ensaj.skillshare_front.view.adapter;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.ArrayList;

import ma.ensaj.skillshare_front.R;
import ma.ensaj.skillshare_front.model.Notification;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>{

    private List<Notification> notificationList;
    private final SimpleDateFormat inputFormat;

    public NotificationAdapter(List<Notification> notificationList) {
        this.notificationList = notificationList != null ? notificationList : new ArrayList<>();
        this.inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
        this.inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @NonNull
    @Override
    public NotificationAdapter.NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationAdapter.NotificationViewHolder(view);
    }

    public void updateNotifications(List<Notification> notifications) {
        if (notifications != null) {
            this.notificationList.clear(); // Vider l'ancienne liste
            this.notificationList.addAll(notifications);
            Log.d("NotificationAdapter", "Notifications mises à jour : " + notifications);// Ajouter les nouvelles notifications
            notifyDataSetChanged(); // Notifier le RecyclerView des changements
        }
    }


    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.NotificationViewHolder holder, int position) {
        Notification notification = notificationList.get(position);

        String fullName = notification.getCreateur().getNom() + " " + notification.getCreateur().getPrenom();
        holder.textCreator.setText(fullName);

        holder.textContent.setText(notification.getContenue());
        String baseUrl = "http://192.168.0.7:3600/skillShare"+notification.getCreateur().getImage();
        Glide.with(holder.itemView.getContext())
                .load(baseUrl)
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_launcher_background)
                .into(holder.imageCreator);

        Log.d("Image_Url2",baseUrl);
        Log.d("NotificationAdapter", "Notification affichée : " + notification);
        try {
            Date date = inputFormat.parse(notification.getDateEnvoie());
            if (date != null) {
                CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(
                        date.getTime(),
                        System.currentTimeMillis(),
                        DateUtils.MINUTE_IN_MILLIS,
                        DateUtils.FORMAT_ABBREV_RELATIVE
                );
                holder.textDate.setText(relativeTime);
            }
        } catch (ParseException e) {
            holder.textDate.setText(notification.getDateEnvoie());
        }

    }

    @Override
    public int getItemCount() {
        Log.d("Size", String.valueOf(notificationList.size()));
        return notificationList.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView textCreator;
        TextView textContent;
        TextView textDate;
        ShapeableImageView imageCreator;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            textCreator = itemView.findViewById(R.id.textCreator);
            textContent = itemView.findViewById(R.id.textContent);
            textDate = itemView.findViewById(R.id.textDate);
            imageCreator = itemView.findViewById(R.id.imageCreator);
        }
    }



}