package pro.network.nanjilmartadmin.subcategory;

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
import pro.network.nanjilmartadmin.categories.Categories;
import pro.network.nanjilmartadmin.categories.CategoriesClick;
import pro.network.nanjilmartadmin.categories.MainActivityCategories;

public class SubCateAdapter extends RecyclerView.Adapter<SubCateAdapter.MyViewHolder> {
    private Context context;
    private List<SubCategory> subCategoryList;
    private CategoriesClick bannerClick;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail, cancel;
        public TextView name;

        public MyViewHolder(View view) {
            super(view);

            thumbnail = view.findViewById(R.id.thumbnail);
            cancel = view.findViewById(R.id.cancel);
            name = view.findViewById(R.id.name);

        }
    }


    public SubCateAdapter(Context context, List<SubCategory> subCategoryList, MainActivitySubCate bannerClick) {
        this.context = context;
        this.subCategoryList = subCategoryList;
        this.bannerClick = bannerClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subcate_row_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final SubCategory subCategory = subCategoryList.get(position);

        holder.name.setText(subCategory.name);
        GlideApp.with(context)
                .load(Appconfig.getResizedImage(subCategory.getImage(), true))
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
        return subCategoryList.size();
    }


    public void notifyData(List<SubCategory> categoriesList) {
        this.subCategoryList = categoriesList;
        notifyDataSetChanged();
    }
}
