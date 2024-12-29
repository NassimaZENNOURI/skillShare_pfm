package ma.ensaj.skillshare_front.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import de.hdodenhof.circleimageview.CircleImageView;

import ma.ensaj.skillshare_front.R;
import ma.ensaj.skillshare_front.viewmodel.LoginViewModel;
import ma.ensaj.skillshare_front.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private MainViewModel mainViewModel;

    private Toolbar appBarLayout;
    private SearchView searchView;
    private LinearLayoutCompat bottomNavigation;
    private NavController navController;
    private CircleImageView profileImage;
    private View currentSelectedItem;
    private String token;
    private LoginViewModel loginViewModel ;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // Récupérer le token
        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        //---------------------------

        // Vérifier si l'utilisateur est connecté
        if (!isUserLoggedIn()) {
            redirectToLogin();
            return;
        }

        initializeViews();
        setupToolbar();
        setupNavigation();
        setupSearchView();
        setupProfileImage();
        setupBottomNavigation();

    }

    private boolean isUserLoggedIn() {
        return token != null;
    }

    private void redirectToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void initializeViews() {
        appBarLayout = findViewById(R.id.appBarLayout);
        searchView = findViewById(R.id.searchView);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        profileImage = findViewById(R.id.profileImage);
    }

    private void setupToolbar() {
        setSupportActionBar(appBarLayout);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void setupNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }
    }

    private void setupSearchView() {
        searchView.setQueryHint("Search...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null && !query.trim().isEmpty()) {
                    // Si l'utilisateur soumet la recherche
                    searchView.clearFocus();
                    // Appel à la méthode du ViewModel pour rechercher les services
                    mainViewModel.searchServices(query.trim());

                    navigateToBlankFragment();

                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null && !newText.trim().isEmpty()) {
                    // Appel à la méthode du ViewModel à chaque changement de texte
                    mainViewModel.searchServices(newText.trim());
                }
                return true;
            }
        });
    }

    private void navigateToBlankFragment() {
        // Utilisez NavController pour la navigation
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.navigate(R.id.navigation_result);
    }

    private void setupBottomNavigation() {
        bottomNavigation.setOrientation(LinearLayoutCompat.HORIZONTAL);
        bottomNavigation.removeAllViews();

        addNavigationItem(R.drawable.ic_home, "Home", R.id.navigation_home);
        addNavigationItem(R.drawable.ic_filter, "Filter", R.id.navigation_filter);  // Ajout du filtre
        addNavigationItem(R.drawable.ic_post, "Post", R.id.navigation_post);
        addNavigationItem(R.drawable.ic_reservated, "Reservations", R.id.navigation_reservation);  // Ajout du filtre
        addNavigationItem(R.drawable.ic_notifications, "Notifications", R.id.navigation_notifications);

        // Sélectionner l'item Home par défaut
        View homeItem = bottomNavigation.getChildAt(0);
        if (homeItem != null) {
            selectNavigationItem(homeItem);
        }
    }

    private void addNavigationItem(int iconRes, String title, int destinationId) {
        View itemView = LayoutInflater.from(this)
                .inflate(R.layout.bottom_nav_item, bottomNavigation, false);

        ImageView icon = itemView.findViewById(R.id.nav_item_icon);
        TextView titleView = itemView.findViewById(R.id.nav_item_title);

        icon.setImageResource(iconRes);
        titleView.setText(title);

        LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(
                0,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        itemView.setLayoutParams(params);

        itemView.setOnClickListener(v -> {
            try {
                navController.navigate(destinationId);
                selectNavigationItem(v);
            } catch (Exception e) {
                Toast.makeText(this, "Navigation error", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });

        bottomNavigation.addView(itemView);
    }

    private void selectNavigationItem(View selectedItem) {
        // Désélectionner l'item précédent
        if (currentSelectedItem != null) {
            ImageView prevIcon = currentSelectedItem.findViewById(R.id.nav_item_icon);
            TextView prevTitle = currentSelectedItem.findViewById(R.id.nav_item_title);
            prevIcon.setSelected(false);
            prevTitle.setSelected(false);
        }

        // Sélectionner le nouvel item
        ImageView icon = selectedItem.findViewById(R.id.nav_item_icon);
        TextView title = selectedItem.findViewById(R.id.nav_item_title);
        icon.setSelected(true);
        title.setSelected(true);

        currentSelectedItem = selectedItem;
    }

    private void setupProfileImage() {
        loginViewModel.getUserLiveData().observe(this, user -> {
            String profileImageUrl = user.getImage();
            String baseUrl = "http://192.168.0.8:3600/skillShare";

            Glide.with(this)
                    .load(baseUrl+profileImageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.ic_launcher_background)
                    .into(profileImage);

            profileImage.setOnClickListener(v -> showProfileMenu());
        });
        loginViewModel.getError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                // Gérer les erreurs
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
        // Déclencher la récupération des informations utilisateur
        loginViewModel.getUserInfos();
    }

    private void showProfileMenu() {
        PopupMenu popup = new PopupMenu(this, profileImage);
        popup.getMenuInflater().inflate(R.menu.profile_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_profile) {
                navController.navigate(R.id.navigation_profile);
                return true;
            } else if (itemId == R.id.menu_settings) {
                // TODO: Implémenter la navigation vers les paramètres
                return true;
            } else if (itemId == R.id.menu_logout) {
                handleLogout();
                return true;
            }
            return false;
        });

        popup.show();
    }

    private void handleLogout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Effacer toutes les données enregistrées
        editor.clear();
        editor.apply();
        redirectToLogin();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Nettoyer les ressources si nécessaire
    }
}

