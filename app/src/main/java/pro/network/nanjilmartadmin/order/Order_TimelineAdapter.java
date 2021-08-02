package pro.network.nanjilmartadmin.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.repsly.library.timelineview.LineType;
import com.repsly.library.timelineview.TimelineView;

import java.util.List;

import pro.network.nanjilmartadmin.R;

class Order_TimelineAdapter extends RecyclerView.Adapter<Order_TimelineAdapter.ViewHolder> {

    private final int orientation;
    private final List<TrackOrder_Sub> items;

    Order_TimelineAdapter(int orientation, List<TrackOrder_Sub> items) {
        this.orientation = orientation;
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.track_order_item;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvName.setText(items.get(position).getStatus());
        holder.tvAddress.setText(items.get(position).getDescription());
        holder.timelineView.setLineType(getLineType(position));
        holder.timelineView.setNumber(position + 1);
        holder.tv_created.setText(items.get(position).createdon);
        holder.timelineView.setFillMarker(true);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private LineType getLineType(int position) {
        if (getItemCount() == 1) {
            return LineType.ONLYONE;

        } else {
            if (position == 0) {
                return LineType.BEGIN;

            } else if (position == getItemCount() - 1) {
                return LineType.END;

            } else {
                return LineType.NORMAL;
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TimelineView timelineView;
        TextView tvName;
        TextView tvAddress, tv_created;

        ViewHolder(View view) {
            super(view);
            timelineView = view.findViewById(R.id.timeline);
            tvName = view.findViewById(R.id.tv_name);
            tvAddress = view.findViewById(R.id.tv_address);
            tv_created = view.findViewById(R.id.tv_created);

        }
    }

}