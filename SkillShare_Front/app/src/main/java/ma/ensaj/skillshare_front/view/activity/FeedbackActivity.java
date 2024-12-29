package ma.ensaj.skillshare_front.view.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ma.ensaj.skillshare_front.R;
import ma.ensaj.skillshare_front.model.Feedback;
import ma.ensaj.skillshare_front.view.adapter.FeedbackAdapter;
import ma.ensaj.skillshare_front.viewmodel.FeedbackViewModel;

public class FeedbackActivity extends AppCompatActivity {

    private RecyclerView feedbackRecyclerView;
    private FeedbackAdapter feedbackAdapter;
    private FeedbackViewModel feedbackViewModel;

    private EditText feedbackCommentEditText;
    private SeekBar ratingSeekBar;
    private Button submitFeedbackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Initialisation des vues
        feedbackRecyclerView = findViewById(R.id.feedbackRecyclerView);
        feedbackRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        feedbackAdapter = new FeedbackAdapter(new ArrayList<>());
        feedbackRecyclerView.setAdapter(feedbackAdapter);

        feedbackCommentEditText = findViewById(R.id.feedbackComment);
        ratingSeekBar = findViewById(R.id.ratingSeekBar);
        submitFeedbackButton = findViewById(R.id.submitFeedbackButton);

        feedbackViewModel = new ViewModelProvider(this).get(FeedbackViewModel.class);

        // Récupérer l'ID du service passé par Intent
        int idServicePropose = getIntent().getIntExtra("idServicePropose", -1);

        if (idServicePropose != -1) {
            // Observer les feedbacks existants
            feedbackViewModel.getFeedbacks(idServicePropose).observe(this, feedbacks -> {
                if (feedbacks != null && !feedbacks.isEmpty()) {
                    feedbackAdapter = new FeedbackAdapter(feedbacks);
                    feedbackRecyclerView.setAdapter(feedbackAdapter);
                } else {
                    Toast.makeText(this, "Aucun feedback disponible", Toast.LENGTH_SHORT).show();
                }
            });

            // Gestion du bouton de soumission de feedback
            submitFeedbackButton.setOnClickListener(v -> {
                String comment = feedbackCommentEditText.getText().toString().trim();
                int rating = ratingSeekBar.getProgress();

                if (comment.isEmpty()) {
                    Toast.makeText(this, "Veuillez entrer un commentaire", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Création d'un nouvel objet Feedback
                Feedback newFeedback = new Feedback();
                newFeedback.setCommentaire(comment);
                newFeedback.setNote(rating);

                // Appel au ViewModel pour ajouter le feedback
                feedbackViewModel.addFeedback(idServicePropose, newFeedback);
                Toast.makeText(this, "Feedback ajouté avec succès", Toast.LENGTH_SHORT).show();

                // Réinitialisation des champs
                feedbackCommentEditText.setText("");
                ratingSeekBar.setProgress(0);
            });

        } else {
            Toast.makeText(this, "Service ID invalide", Toast.LENGTH_SHORT).show();
        }
    }
}