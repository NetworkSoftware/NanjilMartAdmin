package pro.network.nanjilmartadmin.enquires;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pro.network.nanjilmartadmin.R;


public class EnquireAdapter extends RecyclerView.Adapter<EnquireAdapter.MyViewHolder> {

    private final Context mainActivityUser;
    OnEnquires onEnquires;
    private List<EnquireBean> couponList;


    public EnquireAdapter(Context mainActivityUser, List<EnquireBean> beans, OnEnquires onEnquires) {
        this.mainActivityUser = mainActivityUser;
        this.couponList = beans;
        this.onEnquires = onEnquires;
    }

    public void notifyData(List<EnquireBean> myList) {
        this.couponList = myList;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.enquire_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final EnquireBean bean = couponList.get(position);
        holder.enquire.setText(bean.enquire);
        holder.createdOn.setText(bean.createdOn);

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEnquires.onCAllClick(bean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return couponList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView enquire, createdOn;
        Button call;

        public MyViewHolder(View view) {
            super(view);
            createdOn = view.findViewById(R.id.createdOn);
            enquire = view.findViewById(R.id.enquire);
            call = view.findViewById(R.id.call);
        }
    }
}
