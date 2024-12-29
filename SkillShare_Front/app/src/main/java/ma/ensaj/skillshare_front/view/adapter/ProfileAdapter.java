package ma.ensaj.skillshare_front.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import ma.ensaj.skillshare_front.R;
import ma.ensaj.skillshare_front.model.Category;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {

    private List<Category> categoryList;
    private Context context;
    private OnCategoryClickListener onCategoryClickListener;

    public ProfileAdapter(List<Category> categoryList,Context context, OnCategoryClickListener onCategoryClickListener) {
        this.categoryList = categoryList;
        this.context = context;
        this.onCategoryClickListener = onCategoryClickListener;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categorie, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.categoryName.setText(category.getCategorie());
//        holder.categoryIcon.setImageResource(category.getIconResId());

        holder.itemView.setOnClickListener(v -> {
            if (onCategoryClickListener != null) {
                onCategoryClickListener.onCategoryClick(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageView categoryIcon;

        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categorieNameTextView);
            categoryIcon = itemView.findViewById(R.id.categoryIcon);
        }
    }

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }
}



