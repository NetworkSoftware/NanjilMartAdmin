package pro.network.nanjilmartadmin.product;

import android.content.Context;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import pro.network.nanjilmartadmin.R;
import pro.network.nanjilmartadmin.app.GlideApp;

public class AddImageAdapter extends RecyclerView.Adapter<AddImageAdapter.MyViewHolder> {
    private final Context mainActivityUser;
    ImageClick imageClick;
    private ArrayList<String> samplesbean;


    public AddImageAdapter(Context mainActivityUser, ArrayList<String> samplesList, ImageClick imageClick) {
        this.mainActivityUser = mainActivityUser;
        this.samplesbean = samplesList;
        this.imageClick = imageClick;
    }


    public void notifyData(ArrayList<String> myList) {
        Log.d("notifyData ", myList.size() + "");
        this.samplesbean = myList;
        notifyDataSetChanged();
    }

    public AddImageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_list_row, parent, false);

        return new AddImageAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        String samples = samplesbean.get(position);

        holder.itemsCard.setVisibility(View.VISIBLE);
        holder.itemsAdd.setVisibility(View.GONE);
        /*holder.categoryimage.setText(samples.category);
        holder.nameimage.setText(samples.name);
*/
        try {
            GlideApp.with(mainActivityUser).load(samples)
                    .into(holder.itemsImage);
        } catch (Exception e) {
            Toast.makeText(mainActivityUser, "Something went wrong", Toast.LENGTH_SHORT).show();
        }


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageClick.onDeleteClick(position);
            }
        });
        holder.itemsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageClick.onImageClick(position);
            }
        });
    }

    public int getItemCount() {
        return samplesbean.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final CardView itemsCard, itemsAdd;
        private final ImageView itemsImage;
        private final TextView nameimage, categoryimage;
        LinearLayout delete;

        public MyViewHolder(View view) {
            super((view));
            itemsCard = (CardView) view.findViewById(R.id.itemsCard);
            itemsAdd = (CardView) view.findViewById(R.id.itemsAdd);
            itemsImage = (ImageView) view.findViewById(R.id.itemsImage);
            categoryimage = (TextView) view.findViewById(R.id.categoryimage);
            nameimage = (TextView) view.findViewById(R.id.nameimage);
            delete = view.findViewById(R.id.delete);

        }
    }

}


