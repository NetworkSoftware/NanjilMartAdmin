package pro.network.nanjilmartadmin.order;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import pro.network.nanjilmartadmin.R;
import pro.network.nanjilmartadmin.app.Appconfig;
import pro.network.nanjilmartadmin.app.GlideApp;
import pro.network.nanjilmartadmin.product.Product;


public class OrderListSubAdapter extends RecyclerView.Adapter<OrderListSubAdapter.MyViewHolder> {

    SharedPreferences preferences;
    int selectedPosition = 0;
    private final Context mainActivityUser;
    private ArrayList<Product> myorderBeans;

    public OrderListSubAdapter(Context mainActivityUser, ArrayList<Product> myorderBeans) {
        this.mainActivityUser = mainActivityUser;
        this.myorderBeans = myorderBeans;
    }

    public void notifyData(ArrayList<Product> myorderBeans) {
        this.myorderBeans = myorderBeans;
        notifyDataSetChanged();
    }

    public void notifyData(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    public OrderListSubAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myorders_list_sub, parent, false);

        return new OrderListSubAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Product myorderBean = myorderBeans.get(position);
        holder.qty.setText("Qty: " + myorderBean.getQty());
        holder.productName.setText(myorderBean.getBrand() + " - " + myorderBean.getModel());
        holder.shopName.setText("Shop:" + myorderBean.getShopname());
        GlideApp.with(mainActivityUser)
                .load(Appconfig.getResizedImage(myorderBean.getImage(), true))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
                .placeholder(R.drawable.vivo)
                .into(holder.product_image);

    }

    public int getItemCount() {
        return myorderBeans.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView product_image;
        private final TextView qty;
        private final TextView productName;
        private final TextView shopName;


        public MyViewHolder(View view) {
            super((view));
            product_image = view.findViewById(R.id.product_image);
            qty = view.findViewById(R.id.qty);
            productName = view.findViewById(R.id.productName);
            shopName = view.findViewById(R.id.shopName);
        }
    }

}


