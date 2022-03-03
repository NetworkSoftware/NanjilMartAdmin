package pro.network.nanjilmartadmin.festival;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pro.network.nanjilmartadmin.R;
import pro.network.nanjilmartadmin.app.GlideApp;


public class FestivalAdapter extends RecyclerView.Adapter<FestivalAdapter.MyViewHolder> {

    private final Context mainActivityUser;
    FestivalClick festivalClick;
    private List<Festival> festivals;

    public FestivalAdapter(Context mainActivityUser, List<Festival> festivals,
                           FestivalClick festivalClick) {
        this.mainActivityUser = mainActivityUser;
        this.festivals = festivals;
        this.festivalClick = festivalClick;
    }

    public void notifyData(List<Festival> myList) {
        Log.d("notifyData ", myList.size() + "");
        this.festivals = myList;
        notifyDataSetChanged();
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.festivval_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Festival festival = festivals.get(position);
        GlideApp.with(mainActivityUser).load(festival.gifUrl)
                .placeholder(R.drawable.ic_add_a_photo_black_24dp)
                .into(holder.gifUrl);


        holder.enable.setText(festival.getEnable());

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                festivalClick.onEditClick(festival);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                festivalClick.onDeleteClick(festival);
            }
        });

    }

    public int getItemCount() {
        return festivals.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView  enable;
        LinearLayout edit;
        ImageView delete;
        ImageView gifUrl;

        public MyViewHolder(View view) {
            super((view));
            gifUrl = view.findViewById(R.id.gifUrl);
            enable = view.findViewById(R.id.enable);
            edit = view.findViewById(R.id.edit);
            delete = view.findViewById(R.id.delete);

        }
    }

}


