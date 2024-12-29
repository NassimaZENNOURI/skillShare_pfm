/*package ma.ensaj.skillshare_front.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import ma.ensaj.skillshare_front.R;
import ma.ensaj.skillshare_front.model.Feedback;


public class FeedbackAdapter  extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder>{

    private List<Feedback> FeedbackList;

    public FeedbackAdapter(List<Feedback> feedbackList) {
        FeedbackList = feedbackList;
    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_feedback, parent, false);
        return new FeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
        Feedback feedback = FeedbackList.get(position);
        holder.commentTextView.setText(feedback.getCommentaire());
        holder.dateTimeTextView.setText(feedback.getDate());
        String noteText = String.format(Locale.getDefault(), "%.2f", feedback.getNote());
        holder.noteTextView.setText(noteText);
        // Concaténer le prénom et le nom pour l'affichage
        String fullName = feedback.getPrenomUtilisateur() + " " + feedback.getNomUtilisateur();
        holder.fullName.setText(fullName);


    }

    @Override
    public int getItemCount() {
        return FeedbackList.size();
    }

    public static class FeedbackViewHolder extends RecyclerView.ViewHolder {
        TextView noteTextView;
        TextView fullName;
        TextView commentTextView;
        TextView dateTimeTextView;

        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.fullName);
            noteTextView = itemView.findViewById(R.id.rating);
            commentTextView = itemView.findViewById(R.id.comment);
            dateTimeTextView = itemView.findViewById(R.id.dateTime);
        }
    }
}*/


package ma.ensaj.skillshare_front.view.adapter;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import ma.ensaj.skillshare_front.R;
import ma.ensaj.skillshare_front.model.Feedback;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {

    private List<Feedback> feedbackList;
    private final SimpleDateFormat inputFormat;

    public FeedbackAdapter(List<Feedback> feedbackList) {
        this.feedbackList = feedbackList;
        this.inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
        this.inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_feedback, parent, false);
        return new FeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
        Feedback feedback = feedbackList.get(position);

        // Format nom complet
        String fullName = feedback.getPrenomUtilisateur() + " " + feedback.getNomUtilisateur();
        holder.fullName.setText(fullName);

        // Format note
        String noteText = String.format(Locale.getDefault(), "%.1f ★", feedback.getNote());
        holder.noteTextView.setText(noteText);

        // Format commentaire
        holder.commentTextView.setText(feedback.getCommentaire());

        String baseUrl = "http://192.168.0.8:3600/skillShare"+feedback.getImage();
        Glide.with(holder.itemView.getContext())
                .load(baseUrl)  // URL de l'image
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_launcher_background)
                .into(holder.profileImage);
        Log.d("TEST", baseUrl);
        // Format date relative
        try {
            Date date = inputFormat.parse(feedback.getDate());
            if (date != null) {
                CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(
                        date.getTime(),
                        System.currentTimeMillis(),
                        DateUtils.MINUTE_IN_MILLIS,
                        DateUtils.FORMAT_ABBREV_RELATIVE
                );
                holder.dateTimeTextView.setText(relativeTime);
            }
        } catch (ParseException e) {
            holder.dateTimeTextView.setText(feedback.getDate());
        }
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    public static class FeedbackViewHolder extends RecyclerView.ViewHolder {
        TextView noteTextView;
        TextView fullName;
        TextView commentTextView;
        TextView dateTimeTextView;
        ImageView profileImage;

        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.fullName);
            noteTextView = itemView.findViewById(R.id.rating);
            commentTextView = itemView.findViewById(R.id.comment);
            dateTimeTextView = itemView.findViewById(R.id.dateTime);
            profileImage = itemView.findViewById(R.id.profileIcon);
        }
    }
}