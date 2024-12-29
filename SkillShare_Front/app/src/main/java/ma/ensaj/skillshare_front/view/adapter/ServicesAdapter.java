package ma.ensaj.skillshare_front.view.adapter;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

import ma.ensaj.skillshare_front.R;
import ma.ensaj.skillshare_front.viewmodel.ProposedServiceViewModel;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ServicesViewHolder> {

    private List<Map<String, Object>> serviceList;
    private ProposedServiceViewModel viewModel;


    public ServicesAdapter(List<Map<String, Object>> serviceList, ProposedServiceViewModel viewModel) {
        this.serviceList = serviceList;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        return new ServicesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesViewHolder holder, int position) {
        Map<String, Object> service = serviceList.get(position);
        holder.serviceName.setText((String) service.get("nomService"));
        holder.categoryName.setText((String) service.get("nomCategorie"));
        holder.description.setText((String) service.get("description"));
        holder.price.setText("Price : "+String.valueOf(service.get("prix")) + " DH");


        holder.deleteIcon.setOnClickListener(v -> {
            Object idServiceObj = service.get("idService");
            Object idCategorieObj = service.get("idCategorie");
            Log.e("ReservationAdapter", "idreser  " + idServiceObj.toString());
            final int idServ = (idServiceObj instanceof Double)
                    ? ((Double) idServiceObj).intValue()
                    : (idServiceObj instanceof Integer)
                    ? (Integer) idServiceObj
                    : 0;
            final int idCateg = (idCategorieObj instanceof Double)
                    ? ((Double) idCategorieObj).intValue()
                    : (idCategorieObj instanceof Integer)
                    ? (Integer) idCategorieObj
                    : 0;
            Log.e("ReservationAdapter", "idreser  " + idServ);
            if (idServ == 0) {
                Log.e("ReservationAdapter", "idreser is null or invalid for reservation at position " + position);
            }

            Log.e("ReservationAdapter", "idreser  " + idServ);
            viewModel.deleteService(idServ,idCateg);
        });

    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public static class ServicesViewHolder extends RecyclerView.ViewHolder {
        TextView serviceName;
        TextView categoryName;
        TextView description;
        TextView price;
        ImageView deleteIcon;

        public ServicesViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.serviceNameTextView);
            categoryName= itemView.findViewById(R.id.categoryNameTextView);
            description = itemView.findViewById(R.id.descriptionTextView);
            price =itemView.findViewById(R.id.priceTextView);
            deleteIcon= itemView.findViewById(R.id.deleteIcon);
        }
    }
}

