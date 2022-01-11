package pro.network.nanjilmartadmin.shopreg;

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


public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.MyViewHolder> {

    private final Context mainActivityUser;
    ImageClick imageClick;
    boolean isEdit;
    int selectedPosition = 0;
    private ArrayList<Time> samplesbean;

    public TimeAdapter(Context mainActivityUser, ArrayList<Time> samplesbean, ImageClick imageClick, boolean isEdit) {
        this.mainActivityUser = mainActivityUser;
        this.samplesbean = samplesbean;
        this.imageClick = imageClick;
        this.isEdit = isEdit;
    }

    public void notifyData(ArrayList<Time> myList) {
        Log.d("notifyData ", myList.size() + "");
        this.samplesbean = myList;
        notifyDataSetChanged();
    }

    public void notifyData(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.size_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Time samples = samplesbean.get(position);

        holder.intime.setText(samples.getOpenHours());
        holder.out_time.setText(samples.getCloseHours());
        holder.isopen.setText(samples.isOpen?"Open":"Close");
        holder.day.setText(samples.getDayInWeek());
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
            holder.intime.setTextColor(Color.WHITE);
            holder.out_time.setTextColor(Color.WHITE);
        } else {
            holder.sizeLinear.setBackgroundResource(R.drawable.rectangle_box);
            holder.intime.setTextColor(mainActivityUser.getResources().getColor(R.color.colorPrimary));
            holder.out_time.setTextColor(mainActivityUser.getResources().getColor(R.color.colorPrimary));
        }

        holder.sizeLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClick.onImageClick(position);
            }
        });

    }

    public int getItemCount() {
        return samplesbean.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView intime;
        private final TextView out_time;
        private TextView isopen;
        private final TextView day;
        LinearLayout delete, sizeLinear;

        public MyViewHolder(View view) {
            super((view));
            intime = view.findViewById(R.id.intime);
            out_time = view.findViewById(R.id.out_time);
            delete = view.findViewById(R.id.delete);
            isopen = view.findViewById(R.id.isopen);
            sizeLinear = view.findViewById(R.id.sizeLinear);
            day = view.findViewById(R.id.day);

        }
    }

}


