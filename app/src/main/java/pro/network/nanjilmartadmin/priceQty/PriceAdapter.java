package pro.network.nanjilmartadmin.priceQty;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pro.network.nanjilmartadmin.R;
import pro.network.nanjilmartadmin.product.ImageClick;


public class PriceAdapter extends RecyclerView.Adapter<PriceAdapter.MyViewHolder> {

    ImageClick imageClick;
    private final Context mainActivityUser;
    private ArrayList<QuantityPrice> quantityPrices;
    boolean isEdit;
    int selectedPosition = 0;

    public PriceAdapter(Context mainActivityUser, ArrayList<QuantityPrice> quantityPrices,
                        ImageClick imageClick, boolean isEdit) {
        this.mainActivityUser = mainActivityUser;
        this.quantityPrices = quantityPrices;
        this.imageClick = imageClick;
        this.isEdit = isEdit;
    }

    public void notifyData(ArrayList<QuantityPrice> myList) {
        Log.d("notifyData ", myList.size() + "");
        this.quantityPrices = myList;
        notifyDataSetChanged();
    }

    public void notifyData(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.per_price_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        QuantityPrice price = quantityPrices.get(position);
        holder.day.setText(price.getQuantity());
        holder.price.setText(price.getPrice());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClick.onDeleteClick(position);
            }
        });
        if (isEdit) {
            holder.delete.setVisibility(View.VISIBLE);
        } else {
            holder.delete.setVisibility(View.GONE);
        }

        if (!isEdit && selectedPosition == position) {
            holder.sizeLinear.setBackgroundResource(R.drawable.rectangle_box_select);
            holder.day.setTextColor(Color.WHITE);
            holder.price.setTextColor(Color.WHITE);
        } else {
            holder.sizeLinear.setBackgroundResource(R.drawable.rectangle_box);
            holder.day.setTextColor(mainActivityUser.getResources().getColor(R.color.colorPrimary));
            holder.price.setTextColor(mainActivityUser.getResources().getColor(R.color.colorPrimary));
        }

        holder.sizeLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClick.onImageClick(position);
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClick.onEditClick(position);
            }
        });

    }

    public int getItemCount() {
        return quantityPrices.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView day, price;
        LinearLayout delete, sizeLinear,edit;

        public MyViewHolder(View view) {
            super((view));
            day = view.findViewById(R.id.day);
            price = view.findViewById(R.id.price);
            delete = view.findViewById(R.id.delete);
            sizeLinear = view.findViewById(R.id.sizeLinear);
            edit = view.findViewById(R.id.edit);

        }
    }

}


