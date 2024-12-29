package ma.ensaj.skillshare_front.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;
import java.util.Map;

import ma.ensaj.skillshare_front.R;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<Map<String, Object>> services;

    public SearchAdapter(List<Map<String, Object>> services) {
        this.services = services;
    }

    public void updateData(List<Map<String, Object>> newServices) {
        System.out.println("Mise à jour des données dans l'adaptateur : " + newServices);
        this.services.clear();
        this.services.addAll(newServices);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_search_results, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, Object> service = services.get(position);

        if (service != null) {
            holder.serviceName.setText(service.get("nomService").toString());
            holder.userName.setText(service.get("prenomUtilisateur") + " " + service.get("nomUtilisateur"));
            holder.price.setText(service.get("prix") + " MAD");
            holder.location.setText(service.get("localisation").toString());
            holder.category.setText(service.get("nomCategorie").toString());
            holder.description.setText(service.get("description").toString());
            String baseUrl = "http://192.168.0.8:3600/skillShare"+service.get("image");
            Glide.with(holder.itemView.getContext())
                    .load(baseUrl)  // URL de l'image
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_launcher_background)
                    .into(holder.profileIcon);
        } else {
            System.out.println("Service est null à la position : " + position);
        }
    }

    @Override
    public int getItemCount() {
        return services != null ? services.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView serviceName, userName, price, location, category, description;
        ImageView profileIcon ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            serviceName = itemView.findViewById(R.id.serviceTitle);
            userName = itemView.findViewById(R.id.userName);
            price = itemView.findViewById(R.id.servicePrix);
            location = itemView.findViewById(R.id.serviceLocalisation);
            category = itemView.findViewById(R.id.serviceCategorie);
            description = itemView.findViewById(R.id.serviceDescription);
            profileIcon = itemView.findViewById(R.id.profileIcon);
        }
    }
}