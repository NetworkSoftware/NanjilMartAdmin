package pro.network.nanjilmartadmin.coupon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pro.network.nanjilmartadmin.R;


public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.MyViewHolder> {
    private final Context context;
    private List<CouponProduct> productListFiltered;
    private final CouponsAdapterListener listener;

    public CouponAdapter(MainActivityCoupon mainActivityCoupon, List<CouponProduct> bannerList, CouponsAdapterListener listener) {
        this.context = mainActivityCoupon;
        this.listener = listener;
        this.productListFiltered = bannerList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.coupon_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final CouponProduct product = productListFiltered.get(position);
        holder.amt.setText(product.getAmt());
        holder.status.setText(product.getStatus());
        holder.description.setText(product.getDescription());
        holder.coupon.setText(product.getCoupon());

        if (product.isPercent == null || product.isPercent.equalsIgnoreCase("0")) {
            holder.percentage.setVisibility(View.GONE);
            holder.percentageHint.setVisibility(View.GONE);
            holder.isPercent.setText("No");
        } else {
            holder.percentage.setVisibility(View.VISIBLE);
            holder.percentageHint.setVisibility(View.VISIBLE);
            holder.isPercent.setText("Yes");
        }
        holder.percentage.setText(product.getPercentage());


    }

    public void notifyData(List<CouponProduct> productList) {
        this.productListFiltered = productList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return productListFiltered.size();
    }

    public interface CouponsAdapterListener {
        void onContactSelected(CouponProduct product);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView id, amt, description, status, coupon, percentageHint, percentage, isPercent;


        public MyViewHolder(View view) {
            super(view);

            amt = view.findViewById(R.id.amt);
            coupon = view.findViewById(R.id.coupon);
            status = view.findViewById(R.id.status);
            description = view.findViewById(R.id.description);
            percentageHint = view.findViewById(R.id.percentageHint);
            percentage = view.findViewById(R.id.percentage);
            isPercent = view.findViewById(R.id.isPercent);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(productListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
}
