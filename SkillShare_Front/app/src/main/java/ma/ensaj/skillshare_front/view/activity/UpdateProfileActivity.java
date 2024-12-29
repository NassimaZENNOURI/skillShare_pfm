package ma.ensaj.skillshare_front.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import ma.ensaj.skillshare_front.R;
import ma.ensaj.skillshare_front.model.User;
import ma.ensaj.skillshare_front.viewmodel.ProfileViewModel;


public class UpdateProfileActivity extends AppCompatActivity {
    private EditText editNom, editPrenom, editEmail, editLocalisation;
    private ImageView profileImageView;
    private Button saveButton;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        // Initialiser les vues
        editNom = findViewById(R.id.editNom);
        editPrenom = findViewById(R.id.editPrenom);
        editEmail = findViewById(R.id.editEmail);
        editLocalisation = findViewById(R.id.editLocalisation);
        profileImageView = findViewById(R.id.profileImageView);
        saveButton = findViewById(R.id.saveButton);

        // Récupérer les données de l'utilisateur actuel
        currentUser = (User) getIntent().getSerializableExtra("user");

        // Remplir les champs avec les données actuelles
        if (currentUser != null) {
            editNom.setText(currentUser.getNom());
            editPrenom.setText(currentUser.getPrenom());
            editEmail.setText(currentUser.getEmail());
            editLocalisation.setText(currentUser.getLocalisation());

            // Charger l'image de profil actuelle
            String baseUrl = "http://192.168.0.8:3600/skillShare" + currentUser.getImage();
            Glide.with(this)
                    .load(baseUrl)
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_launcher_background)
                    .into(profileImageView);
        }

        // Gestionnaire de clic pour changer l'image
        profileImageView.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Sélectionner une image"), PICK_IMAGE_REQUEST);
        });

        // Gestionnaire de clic pour sauvegarder
        saveButton.setOnClickListener(v -> updateProfile());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profileImageView.setImageURI(imageUri);
        }
    }

    private void updateProfile() {
        // Mettre à jour les données utilisateur
        currentUser.setNom(editNom.getText().toString());
        currentUser.setPrenom(editPrenom.getText().toString());
        currentUser.setEmail(editEmail.getText().toString());
        currentUser.setLocalisation(editLocalisation.getText().toString());
        // Appel au ViewModel pour sauvegarder les modifications
        ProfileViewModel viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        if (imageUri != null) {
            // Télécharger l'image et obtenir l'URL
            viewModel.uploadImage(currentUser,this, imageUri, viewModel);
        } else {
            // Mettre à jour le profil de l'utilisateur sans changer l'image
            viewModel.updateUser(currentUser);
        }
//        Intent intent = new Intent(UpdateProfileActivity.this, ProfileFragment.class);
//        startActivity(intent);
        //navigateToBlankFragment();
//        Intent intent = new Intent();

    }

//    private void navigateToBlankFragment() {
//        // Utilisez NavController pour la navigation
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        navController.navigate(R.id.navigation_profile);
//    }

//    private String encodeImageToBase64(Uri uri) {
//        try {
//            InputStream inputStream = getContentResolver().openInputStream(uri);
//            byte[] bytes = getBytes(inputStream);
//            return Base64.encodeToString(bytes, Base64.DEFAULT);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    private byte[] getBytes(InputStream inputStream) throws IOException {
//        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
//        int bufferSize = 1024;
//        byte[] buffer = new byte[bufferSize];
//        int len;
//        while ((len = inputStream.read(buffer)) != -1) {
//            byteBuffer.write(buffer, 0, len);
//        }
//        return byteBuffer.toByteArray();
//    }


    // Méthode utilitaire pour obtenir le chemin réel à partir de l'URI
//    private String getRealPathFromURI(Uri contentUri) {
//        try {
//            String[] proj = {MediaStore.Images.Media.DATA};
//            Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
//            if (cursor != null) {
//                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                cursor.moveToFirst();
//                String path = cursor.getString(column_index);
//                cursor.close();
//                return path;
//            }
//            return contentUri.getPath();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return contentUri.getPath();
//        }
//    }
//
//    public String encodeImageToBase64(String imagePath) {
//        try {
//            File file = new File(imagePath);
//            FileInputStream fis = new FileInputStream(file);
//            byte[] bytes = new byte[(int) file.length()];
//            fis.read(bytes);
//            fis.close();
//            return Base64.encodeToString(bytes, Base64.DEFAULT);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

//    private String getRealPathFromURI(Uri contentUri) {
//        String[] proj = {MediaStore.Images.Media.DATA};
//        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
//        Cursor cursor = loader.loadInBackground();
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        String result = cursor.getString(column_index);
//        cursor.close();
//        return result;
//    }
}



//public class UpdateProfileActivity extends AppCompatActivity {
//
//    private EditText editName, editPrenom, editEmail, editLocalisation;
//    private ImageView profileImage;
//    private Button updateButton;
//    private User currentUser; // L'utilisateur actuel
//    private Uri selectedImageUri;
//    private ProfileViewModel profileViewModel;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_update_profile);
//
//        editName = findViewById(R.id.editName);
//        editPrenom = findViewById(R.id.editPrenom);
//        editEmail = findViewById(R.id.editEmail);
//        editLocalisation = findViewById(R.id.editLocalisation);
//        profileImage = findViewById(R.id.editProfileImage);
//        updateButton = findViewById(R.id.updateButton);
//
//        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
//
//        // Charger les données utilisateur depuis l'intent
//        currentUser = (User) getIntent().getSerializableExtra("user");
//        if (currentUser != null) {
//            populateFields(currentUser);
//        }
//
//        // Changer l'image de profil
//        profileImage.setOnClickListener(v -> pickImageFromGallery());
//
//        // Sauvegarder les modifications
//        updateButton.setOnClickListener(v -> updateUserProfile());
//    }
//
//    private void populateFields(User user) {
//        editName.setText(user.getNom());
//        editPrenom.setText(user.getPrenom());
//        editEmail.setText(user.getEmail());
//        editLocalisation.setText(user.getLocalisation());
//
//        Glide.with(this)
//                .load("http://192.168.0.8:3600/skillShare" + user.getImage())
//                .placeholder(R.drawable.ic_profile)
//                .into(profileImage);
//    }
//
//    private void pickImageFromGallery() {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        startActivityForResult(intent, 1);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
//            selectedImageUri = data.getData();
//            profileImage.setImageURI(selectedImageUri);
//        }
//    }
//
//    private void updateUserProfile() {
//        String name = editName.getText().toString();
//        String prenom = editPrenom.getText().toString();
//        String email = editEmail.getText().toString();
//        String localisation = editLocalisation.getText().toString();
//
//        currentUser.setNom(name);
//        currentUser.setPrenom(prenom);
//        currentUser.setEmail(email);
//        currentUser.setLocalisation(localisation);
//
//        // Appel à l'API pour mettre à jour les données utilisateur
//        profileViewModel.updateUser(currentUser, selectedImageUri, this);
//    }
//}
