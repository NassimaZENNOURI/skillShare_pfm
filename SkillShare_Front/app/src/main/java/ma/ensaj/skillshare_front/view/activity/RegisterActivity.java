package ma.ensaj.skillshare_front.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import ma.ensaj.skillshare_front.R;
import ma.ensaj.skillshare_front.databinding.ActivityRegisterBinding;
import ma.ensaj.skillshare_front.model.User;
import ma.ensaj.skillshare_front.viewmodel.RegisterViewModel;

public class RegisterActivity extends AppCompatActivity {

    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRegisterBinding binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextView loginTextView = findViewById(R.id.loginTextView);
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        // Observer pour voir la réponse après l'inscription
        registerViewModel.getAuthResponse().observe(this, message -> {
            if (message != null) {
                if (message.contains("Utilisateur ajouté avec succès")) {
                    Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                    // Redirection ou autre action après succès
                } else {
                    Toast.makeText(RegisterActivity.this, "Response: " + message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerViewModel.getError().observe(this, error -> {
            if (error != null) {
                Log.e("RegisterActivity", error);
                Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });

        // Quand l'utilisateur appuie sur le bouton d'inscription
        binding.registerButton.setOnClickListener(v -> {
            String nom = binding.nomEditText.getText().toString();
            String prenom = binding.prenomEditText.getText().toString();
            String email = binding.emailEditText.getText().toString();
            String motDePasse = binding.passwordEditText.getText().toString();
            String localisation = binding.localisationEditText.getText().toString();

            // Créer un objet User
            User user = new User(nom, prenom, email, motDePasse,  localisation);

            // Appeler la méthode d'inscription dans le ViewModel
            registerViewModel.registerUser(user);
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);

        });

        loginTextView.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}