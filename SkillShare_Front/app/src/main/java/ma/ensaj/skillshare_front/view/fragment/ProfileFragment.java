package ma.ensaj.skillshare_front.view.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import ma.ensaj.skillshare_front.MyApp;
import ma.ensaj.skillshare_front.R;
import ma.ensaj.skillshare_front.model.Category;
import ma.ensaj.skillshare_front.model.User;
import ma.ensaj.skillshare_front.view.activity.UpdateProfileActivity;
import ma.ensaj.skillshare_front.view.adapter.ProfileAdapter;
import ma.ensaj.skillshare_front.viewmodel.ProfileViewModel;

public class ProfileFragment extends Fragment implements ProfileAdapter.OnCategoryClickListener{

    private ProfileViewModel profileViewModel;
    private RecyclerView recyclerView;
    private ProfileAdapter profileAdapter;
    //private ImageView editProfileImage;
    private ImageView editProfileButton;
    //private Switch notificationSwitch;
    private ImageView addCategoryButton;
    private TextView userName;
    private TextView userEmail;
    private TextView quoteOfTheDayTitle;
    private TextView localisation;
    private TextView quoteAuthor;
    private ImageView imageProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        recyclerView = view.findViewById(R.id.categoriesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //editProfileImage = view.findViewById(R.id.editProfileImage);
        editProfileButton = view.findViewById(R.id.editProfileButton);
        //notificationSwitch = view.findViewById(R.id.notificationSwitch);
        addCategoryButton = view.findViewById(R.id.addCategoryButton);
        userName = view.findViewById(R.id.userName);
        userEmail = view.findViewById(R.id.userJoinDate);
        quoteOfTheDayTitle = view.findViewById(R.id.quoteOfTheDayTitle);
        localisation = view.findViewById(R.id.quoteOfTheDay);
        quoteAuthor = view.findViewById(R.id.quoteAuthor);
        imageProfile= view.findViewById(R.id.profile_Image);


        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            profileAdapter = new ProfileAdapter(categories, getContext(), this);
            recyclerView.setAdapter(profileAdapter);
        });

        profileViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            userName.setText(user.getNom() + " " + user.getPrenom());
            userEmail.setText(user.getEmail());
            localisation.setText(user.getLocalisation());
            quoteAuthor.setText("Score : "+ String.valueOf(user.getNoteMoyenne()));
            String baseUrl = "http://192.168.0.8:3600/skillShare"+user.getImage();
            Glide.with(getContext())
                    .load(baseUrl)  // URL de l'image
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_launcher_background)
                    .into(imageProfile);
        });

        profileViewModel.fetchCategories();
        SharedPreferences sharedPreferences = MyApp.getInstance().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        User userInfo = MyApp.decodeToken(token);
        profileViewModel.fetchUser(userInfo.getIdUser()); // Replace with actual user ID

        // Handle interactions
//        editProfileImage.setOnClickListener(v -> {
//            // Handle edit profile image click
//        });

        editProfileButton.setOnClickListener(v -> {
            User currentUser = profileViewModel.getUser().getValue();
            if (currentUser != null) {
                Intent intent = new Intent(getContext(), UpdateProfileActivity.class);
                intent.putExtra("user", currentUser);
                startActivity(intent);
            }
        });

//        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            // Handle notification switch change
//        });

        addCategoryButton.setOnClickListener(v -> showAddCategoryDialog());

        return view;
    }

    private void showAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_category, null);

        EditText categoryNameInput = dialogView.findViewById(R.id.categoryNameInput);

        builder.setView(dialogView)
                .setTitle("Add category")
                .setPositiveButton("Add", (dialog, id) -> {
                    String categoryName = categoryNameInput.getText().toString().trim();
                    if (!categoryName.isEmpty()) {
                        profileViewModel.addCategory(categoryName);
                    } else {
                        Toast.makeText(getContext(), "Enter a category name", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onCategoryClick(Category category) {
        // Navigate to the ServicesFragment or ServicesActivity
        Bundle bundle = new Bundle();
        bundle.putInt("categoryId", category.getIdCategorie()); // Assurez-vous que votre modèle Category a un getter pour l'ID
        Navigation.findNavController(getView()).navigate(R.id.servicesFragment, bundle);
    }
}




//package ma.ensaj.skillshare_front.fragment;
//
//import static android.content.Context.MODE_PRIVATE;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.*;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModelProvider;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import com.bumptech.glide.Glide;
//
//import ma.ensaj.skillshare_front.MyApp;
//import ma.ensaj.skillshare_front.R;
//import ma.ensaj.skillshare_front.model.Category;
//import ma.ensaj.skillshare_front.model.User;
//import ma.ensaj.skillshare_front.utils.ImagePickerHelper;
//import ma.ensaj.skillshare_front.view.activity.LoginActivity;
//import ma.ensaj.skillshare_front.view.adapter.CategorieAdapter;
//import ma.ensaj.skillshare_front.viewmodel.ProfileViewModel;
//
//public class ProfileFragment extends Fragment implements CategorieAdapter.OnCategorieClickListener {
//    private ProfileViewModel viewModel;
//    private ImageView profileImage;
//    private TextView userName, userScore;
//    private ImageView editProfileButton, editProfileImage;
//    private Switch notificationSwitch;
//    private RecyclerView categoriesRecyclerView;
//    private CategorieAdapter categorieAdapter;
//    private ProgressBar progressBar;
//    private SharedPreferences sharedPreferences;
//    private String token;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
////        sharedPrefManager = new SharedPrefManager(requireContext());
//        sharedPreferences = MyApp.getInstance().getSharedPreferences("user_prefs", MODE_PRIVATE);
//        token = sharedPreferences.getString("token", null);
//        progressBar = getView().findViewById(R.id.progressBar);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_profile, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        initializeViews(view);
//        setupRecyclerView();
//        setupObservers();
//        setupListeners();
//
//        // Charger le profil initial
//        loadUserProfile();
//    }
//
//    private void initializeViews(View view) {
//        profileImage = view.findViewById(R.id.profileImage);
//        userName = view.findViewById(R.id.userName);
//        userScore = view.findViewById(R.id.userScore);
//        editProfileButton = view.findViewById(R.id.editProfileButton);
//        editProfileImage = view.findViewById(R.id.editProfileImage);
//        notificationSwitch = view.findViewById(R.id.notificationSwitch);
//        categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView);
//        progressBar = view.findViewById(R.id.progressBar);
//
//        //Button logoutButton = view.findViewById(R.id.logoutButton);
//        //logoutButton.setOnClickListener(v -> handleLogout());
//
//        ImageButton addCategoryButton = view.findViewById(R.id.addCategoryButton);
//        addCategoryButton.setOnClickListener(v -> showAddCategoryDialog());
//    }
//
//    private void setupRecyclerView() {
//        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
//        categorieAdapter = new CategorieAdapter(null, this);
//        categoriesRecyclerView.setAdapter(categorieAdapter);
//    }
//
//    private void setupObservers() {
//        viewModel.getUserData().observe(getViewLifecycleOwner(), user -> {
//            if (user != null) {
//                userName.setText(String.format("%s %s", user.getNom(), user.getPrenom()));
//                userScore.setText(String.format("SAR %.2f", user.getNoteMoyenne()));
//
//                if (user.getImage() != null && !user.getImage().isEmpty()) {
//                    Glide.with(this)
//                            .load(user.getImage())
//                            .circleCrop()
//                            .into(profileImage);
//                }
//            }
//        });
//
//        viewModel.getCategoriesData().observe(getViewLifecycleOwner(), categories -> {
//            categorieAdapter.updateCategories(categories);
//        });
//
//        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
//            if (error != null) {
//                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
//            }
//        });
//
//        viewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
//            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
//        });
//    }
//
//    private void setupListeners() {
//        editProfileImage.setOnClickListener(v -> ImagePickerHelper.pickImage(this));
//
//        editProfileButton.setOnClickListener(v -> showEditProfileDialog());
//
//        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            // Implémenter la logique de notification
//        });
//    }
//
//    private void loadUserProfile() {
//        if(token!= null){
//            User userInfo = MyApp.decodeToken(token);
//            if (userInfo.getIdUser() != -1) {
//                viewModel.loadUserProfile(userInfo.getIdUser());
//            } else {
//                //handleLogout();
//            }
//        }
//    }
//
////    private void handleLogout() {
////        SharedPreferences.Editor editor = sharedPreferences.edit();
////        // Effacer toutes les données enregistrées
////        editor.clear();
////        editor.apply();
////        redirectToLogin();
////    }
////    private void redirectToLogin() {
////        Context context = requireContext(); // Ou getActivity()
////        Intent intent = new Intent(context, LoginActivity.class);
////        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
////        context.startActivity(intent);
////        getActivity().finish();  // Pour fermer l'activité parent si nécessaire
////    }
//
//
//    // Suite du ProfileFragment.java
//    private void showEditProfileDialog() {
//        View dialogView = LayoutInflater.from(requireContext())
//                .inflate(R.layout.dialog_edit_profile, null);
//
//        EditText nomEdit = dialogView.findViewById(R.id.nomEdit);
//        EditText prenomEdit = dialogView.findViewById(R.id.prenomEdit);
//        EditText emailEdit = dialogView.findViewById(R.id.emailEdit);
//
//        User currentUser = viewModel.getUserData().getValue();
//        if (currentUser != null) {
//            nomEdit.setText(currentUser.getNom());
//            prenomEdit.setText(currentUser.getPrenom());
//            emailEdit.setText(currentUser.getEmail());
//        }
//
//        AlertDialog dialog = new AlertDialog.Builder(requireContext())
//                .setTitle("Modifier le profil")
//                .setView(dialogView)
//                .setPositiveButton("Enregistrer", (dialogInterface, i) -> {
//                    if (currentUser != null) {
//                        currentUser.setNom(nomEdit.getText().toString());
//                        currentUser.setPrenom(prenomEdit.getText().toString());
//                        currentUser.setEmail(emailEdit.getText().toString());
//                        viewModel.updateUserProfile(currentUser.getIdUser(), currentUser);
//                    }
//                })
//                .setNegativeButton("Annuler", null)
//                .create();
//
//        dialog.show();
//    }
//
//    private void showAddCategoryDialog() {
//        View dialogView = LayoutInflater.from(requireContext())
//                .inflate(R.layout.dialog_add_category, null);
//
//        EditText categoryNameEdit = dialogView.findViewById(R.id.categoryNameEdit);
//
//        AlertDialog dialog = new AlertDialog.Builder(requireContext())
//                .setTitle("Ajouter une catégorie")
//                .setView(dialogView)
//                .setPositiveButton("Ajouter", (dialogInterface, i) -> {
//                    String categoryName = categoryNameEdit.getText().toString();
//                    if (!categoryName.isEmpty()) {
//                        // Implémenter l'ajout de catégorie
//                        // À compléter selon votre logique métier
//                    }
//                })
//                .setNegativeButton("Annuler", null)
//                .create();
//
//        dialog.show();
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == ImagePickerHelper.PICK_IMAGE_REQUEST &&
//                resultCode == Activity.RESULT_OK &&
//                data != null &&
//                data.getData() != null) {
//
//            Uri imageUri = data.getData();
//            // Implémenter la logique de mise à jour de l'image
//            // À adapter selon votre logique de gestion des images
//            uploadProfileImage(imageUri);
//        }
//    }
//
//    private void uploadProfileImage(Uri imageUri) {
//        // Implémenter la logique d'upload d'image
//        // Cette méthode dépendra de votre backend
//    }
//
//    @Override
//    public void onCategorieClick(Category categorie) {
//        // Implémenter la logique lors du clic sur une catégorie
//        Toast.makeText(requireContext(),
//                "Catégorie sélectionnée : " + categorie.getCategorie(),
//                Toast.LENGTH_SHORT).show();
//    }
//}