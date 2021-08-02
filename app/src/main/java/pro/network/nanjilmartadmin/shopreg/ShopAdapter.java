package pro.network.nanjilmartadmin.shopreg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pro.network.nanjilmartadmin.R;
public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.MyViewHolder> {
    private Context context;
    private List<Shop> categoriesList;
    private ShopClick bannerClick;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout linear;
        public ImageView  cancel;
        public TextView shop_name,phone,stock_update;

        public MyViewHolder(View view) {
            super(view);



            cancel = view.findViewById(R.id.cancel);
            shop_name = view.findViewById(R.id.shop_name);
            phone = view.findViewById(R.id.phone);
            stock_update = view.findViewById(R.id.stock_update);
            linear=view.findViewById(R.id.linear);
        }
    }


    public ShopAdapter(Context context, List<Shop> categoriesList, ShopClick bannerClick) {
        this.context = context;
        this.categoriesList = categoriesList;
        this.bannerClick =  bannerClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_row_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Shop categories = categoriesList.get(position);

        holder.shop_name.setText(categories.shop_name);
        holder.phone.setText(categories.phone);
        holder.stock_update.setText(categories.stock_update);

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bannerClick.onDeleteClick(position);
            }
        });

        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bannerClick.onItemClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }


    public void notifyData(List<Shop> categoriesList) {
        this.categoriesList = categoriesList;
        notifyDataSetChanged();
    }
}
