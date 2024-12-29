package ma.ensaj.skillshare_front.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ma.ensaj.skillshare_front.R;
import ma.ensaj.skillshare_front.model.Category;

import java.util.List;

public class CategorieAdapter extends RecyclerView.Adapter<CategorieAdapter.CategorieViewHolder> {

    private List<Category> categories;
    private OnCategorieClickListener listener;

    public interface OnCategorieClickListener {
        void onCategorieClick(Category categorie);
    }

    public CategorieAdapter(List<Category> categories, OnCategorieClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategorieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_categorie, parent, false);
        return new CategorieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategorieViewHolder holder, int position) {
        Category categorie = categories.get(position);
        holder.bind(categorie, listener);
    }

    @Override
    public int getItemCount() {
        return categories != null ? categories.size() : 0;
    }

    public void updateCategories(List<Category> newCategories) {
        this.categories = newCategories;
        notifyDataSetChanged();
    }

    static class CategorieViewHolder extends RecyclerView.ViewHolder {
        TextView categorieNameTextView;

        CategorieViewHolder(View itemView) {
            super(itemView);
            categorieNameTextView = itemView.findViewById(R.id.categorieNameTextView);
        }

        void bind(final Category categorie, final OnCategorieClickListener listener) {
            categorieNameTextView.setText(categorie.getCategorie());
            itemView.setOnClickListener(v -> listener.onCategorieClick(categorie));
        }
    }
}