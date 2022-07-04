package pro.network.nanjilmartadmin.categories;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pro.network.nanjilmartadmin.R;
import pro.network.nanjilmartadmin.app.Appconfig;
import pro.network.nanjilmartadmin.app.GlideApp;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyViewHolder> {
    private Context context;
    private List<Categories> categoriesList;
    private CategoriesClick bannerClick;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail, cancel;
        public TextView title,position;

        public MyViewHolder(View view) {
            super(view);

            position= view.findViewById(R.id.position);
            thumbnail = view.findViewById(R.id.thumbnail);
            cancel = view.findViewById(R.id.cancel);
            title = view.findViewById(R.id.title);

        }
    }


    public CategoriesAdapter(Context context, List<Categories> categoriesList, MainActivityCategories bannerClick) {
        this.context = context;
        this.categoriesList = categoriesList;
        this.bannerClick = bannerClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categories_row_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Categories categories = categoriesList.get(position);

        holder.position.setText("Category Position: "+categories.row);
        holder.title.setText(categories.title);
        GlideApp.with(context)
                .load(Appconfig.getResizedImage(categories.getImage(), true))
                .placeholder(R.drawable.vivo)
                .into(holder.thumbnail);
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bannerClick.onItemClick(position);
            }
        });
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bannerClick.onDeleteClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }


    public void notifyData(List<Categories> categoriesList) {
        this.categoriesList = categoriesList;
        notifyDataSetChanged();
    }
}
