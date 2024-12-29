/*package ma.ensaj.skillshare_front.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ma.ensaj.skillshare_front.MyApp;
import ma.ensaj.skillshare_front.R;
import ma.ensaj.skillshare_front.model.Category;
import ma.ensaj.skillshare_front.network.RetrofitInstance;
import ma.ensaj.skillshare_front.network.api.CategorieApi;
import ma.ensaj.skillshare_front.network.api.ProposedServiceApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostFragment extends Fragment {

    private EditText editServiceName, editServiceDescription, editServicePrice;
    private Button btnPublish, btnCancel;
    private Spinner categorySpinner;
    private SharedPreferences sharedPreferences;

    public PostFragment() {
        // Required empty public constructor
    }

    public static PostFragment newInstance() {
        return new PostFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post, container, false);

        // Initialiser les vues
        editServiceName = rootView.findViewById(R.id.editServiceName);
        editServiceDescription = rootView.findViewById(R.id.editServiceDescription);
        editServicePrice = rootView.findViewById(R.id.editServicePrice);
        btnPublish = rootView.findViewById(R.id.btnPublish);
        btnCancel = rootView.findViewById(R.id.btnCancel);
        categorySpinner = rootView.findViewById(R.id.spinnerCategory);

        // Récupérer les préférences partagées pour le token
        sharedPreferences = getActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        // Récupérer les catégories depuis l'API
        fetchCategories();

        // Gestion des événements des boutons
        btnPublish.setOnClickListener(v -> {
            // Récupérer les données du formulaire
            String serviceName = editServiceName.getText().toString();
            String serviceDescription = editServiceDescription.getText().toString();
            String servicePrice = editServicePrice.getText().toString();

            if (!serviceName.isEmpty() && !serviceDescription.isEmpty() && !servicePrice.isEmpty()) {
                // Récupérer l'ID de la catégorie sélectionnée
                Category selectedCategory = (Category) categorySpinner.getSelectedItem();
                int categoryId = selectedCategory != null ? selectedCategory.getIdCategorie() : -1;

                // Appeler la méthode pour ajouter un service
                addServiceToBackend(serviceName, serviceDescription, servicePrice, categoryId);
            } else {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> {
            // Réinitialiser les champs
            editServiceName.setText("");
            editServiceDescription.setText("");
            editServicePrice.setText("");
        });

        return rootView;
    }

    private void fetchCategories() {
        CategorieApi categorieApi = RetrofitInstance.getInstance().create(CategorieApi.class);

        categorieApi.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    List<Category> categories = response.body();
                    // Adapter personnalisé pour afficher les noms des catégories
                    ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(getContext(),
                            android.R.layout.simple_spinner_item, categories) {
                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            TextView text = (TextView) view.findViewById(android.R.id.text1);
                            text.setText(categories.get(position).getCategorie());  // Afficher le nom de la catégorie
                            return view;
                        }

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            TextView text = (TextView) view.findViewById(android.R.id.text1);
                            text.setText(categories.get(position).getCategorie());  // Afficher le nom de la catégorie
                            return view;
                        }
                    };
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    categorySpinner.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Failed to load categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(getContext(), "Error fetching categories: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addServiceToBackend(String serviceName, String serviceDescription, String servicePrice, int categoryId) {
        float price = 0f;
        try {
            price = Float.parseFloat(servicePrice);  // Convertir en float
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid price format", Toast.LENGTH_SHORT).show();
            return;  // Arrêter l'exécution si le format du prix est incorrect
        }

        // Récupération du token depuis SharedPreferences
        SharedPreferences sharedPreferences = MyApp.getInstance().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        if (token != null) {
            String bearerToken = "Bearer " + token;

            // Créer un objet Map pour envoyer les données au backend
            Map<String, Object> serviceData = new HashMap<>();
            serviceData.put("nom_service", serviceName);
            serviceData.put("description", serviceDescription);
            serviceData.put("prix", price);
            serviceData.put("id_categorie", categoryId);  // Ajouter l'ID de la catégorie

            // Créer Retrofit instance via RetrofitInstance
            ProposedServiceApi apiService = RetrofitInstance.getInstance().create(ProposedServiceApi.class);

            // Effectuer l'appel API pour ajouter le service
            Call<ResponseBody> call = apiService.addService(serviceData, bearerToken);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String responseBody = response.body().string();
                            Toast.makeText(getContext(), responseBody, Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Toast.makeText(getContext(), "Error reading response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Failed to add service: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("API Failure", "Network error: " + t.getMessage(), t);
                    // En cas d'erreur de réseau
                    Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}*/
package ma.ensaj.skillshare_front.view.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ma.ensaj.skillshare_front.MyApp;
import ma.ensaj.skillshare_front.R;
import ma.ensaj.skillshare_front.model.Category;
import ma.ensaj.skillshare_front.network.RetrofitInstance;
import ma.ensaj.skillshare_front.network.api.CategorieApi;
import ma.ensaj.skillshare_front.network.api.ProposedServiceApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostFragment extends Fragment {

    private TextInputEditText editServiceName, editServiceDescription, editServicePrice;
    private Button btnPublish, btnCancel;
    private MaterialAutoCompleteTextView categoryAutoComplete;
    private SharedPreferences sharedPreferences;
    private List<Category> categories;

    public PostFragment() {
        // Required empty public constructor
    }

    public static PostFragment newInstance() {
        return new PostFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post, container, false);

        // Initialiser les vues avec MaterialComponents
        editServiceName = rootView.findViewById(R.id.editServiceName);
        editServiceDescription = rootView.findViewById(R.id.editServiceDescription);
        editServicePrice = rootView.findViewById(R.id.editServicePrice);
        btnPublish = rootView.findViewById(R.id.btnPublish);
        btnCancel = rootView.findViewById(R.id.btnCancel);
        categoryAutoComplete = rootView.findViewById(R.id.spinnerCategory);

        // Récupérer les préférences partagées pour le token
        sharedPreferences = getActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        // Récupérer les catégories depuis l'API
        fetchCategories();

        // Gestion des événements des boutons
        btnPublish.setOnClickListener(v -> {
            String serviceName = editServiceName.getText().toString();
            String serviceDescription = editServiceDescription.getText().toString();
            String servicePrice = editServicePrice.getText().toString();

            if (!serviceName.isEmpty() && !serviceDescription.isEmpty() && !servicePrice.isEmpty()) {
                Category selectedCategory = getCurrentlySelectedCategory();
                if (selectedCategory != null) {
                    addServiceToBackend(serviceName, serviceDescription, servicePrice, selectedCategory.getIdCategorie());
                } else {
                    Toast.makeText(getContext(), "Please select a category", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> {
            editServiceName.setText("");
            editServiceDescription.setText("");
            editServicePrice.setText("");
            categoryAutoComplete.setText("");
        });

        return rootView;
    }

    private Category getCurrentlySelectedCategory() {
        String selectedCategoryName = categoryAutoComplete.getText().toString();
        if (categories != null) {
            for (Category category : categories) {
                if (category.getCategorie().equals(selectedCategoryName)) {
                    return category;
                }
            }
        }
        return null;
    }

    private void fetchCategories() {
        CategorieApi categorieApi = RetrofitInstance.getInstance().create(CategorieApi.class);

        categorieApi.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categories = response.body();
                    String[] categoryNames = new String[categories.size()];
                    for (int i = 0; i < categories.size(); i++) {
                        categoryNames[i] = categories.get(i).getCategorie();
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            getContext(),
                            android.R.layout.simple_dropdown_item_1line,
                            categoryNames
                    );
                    categoryAutoComplete.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Failed to load categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(getContext(), "Error fetching categories: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addServiceToBackend(String serviceName, String serviceDescription, String servicePrice, int categoryId) {
        float price = 0f;
        try {
            price = Float.parseFloat(servicePrice);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid price format", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = MyApp.getInstance().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        if (token != null) {
            String bearerToken = "Bearer " + token;

            Map<String, Object> serviceData = new HashMap<>();
            serviceData.put("nom_service", serviceName);
            serviceData.put("description", serviceDescription);
            serviceData.put("prix", price);
            serviceData.put("id_categorie", categoryId);

            ProposedServiceApi apiService = RetrofitInstance.getInstance().create(ProposedServiceApi.class);

            Call<ResponseBody> call = apiService.addService(serviceData, bearerToken);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String responseBody = response.body().string();
                            Toast.makeText(getContext(), responseBody, Toast.LENGTH_SHORT).show();
                            // Clear fields after successful submission
                            editServiceName.setText("");
                            editServiceDescription.setText("");
                            editServicePrice.setText("");
                            categoryAutoComplete.setText("");
                        } catch (IOException e) {
                            Toast.makeText(getContext(), "Error reading response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Failed to add service: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("API Failure", "Network error: " + t.getMessage(), t);
                    Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}