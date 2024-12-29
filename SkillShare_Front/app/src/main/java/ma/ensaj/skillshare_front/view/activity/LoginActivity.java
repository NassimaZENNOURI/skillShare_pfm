package ma.ensaj.skillshare_front.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import ma.ensaj.skillshare_front.R;
import ma.ensaj.skillshare_front.databinding.ActivityLoginBinding;
import ma.ensaj.skillshare_front.model.User;
import ma.ensaj.skillshare_front.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        TextView signUpTextView = findViewById(R.id.signUpTextView);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        loginViewModel.getAuthResponse().observe(this, authResponse -> {
            SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            String token = sharedPreferences.getString("token", null);
            if (token != null) {
                User userInfo = loginViewModel.decodeToken(token);
                try {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("email", userInfo.getEmail());
                    intent.putExtra("idUser", userInfo.getIdUser());
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("LoginActivity", "Error starting MainActivity", e);
                }

            }
        });

        signUpTextView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        loginViewModel.getError().observe(this, error -> {
            Log.d("ERROR",error);
            Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
        });

        binding.loginButton.setOnClickListener(v -> {
            String email = binding.emailEditText.getText().toString();
            String password = binding.passwordEditText.getText().toString();
            Log.d("Login", "Email: " + email + " Password: " + password);
            loginViewModel.login(email, password);
        });
    }
}