package com.example.alex.stargazer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>  {


    @NonNull
    private ClickListener mClickListener;
    private List<Spot> spots;
    private Context mContext;
    private String fileName;
    public RecyclerViewAdapter(List<Spot> spots, Context context) {
        this.spots = spots;
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return spots.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.spotName.setText(spots.get(position).getName());
        holder.spotLongitude.setText("" +spots.get(position).getLongitude());
        holder.spotLatitude.setText("" + spots.get(position).getLatitude());
        holder.spotInfo.setText(spots.get(position).getInfo());
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        TextView spotName;
        TextView spotLongitude;
        TextView spotLatitude;
        TextView spotInfo;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.spotName = itemView.findViewById(R.id.spotName);
            this.spotLongitude = itemView.findViewById(R.id.spotLongitude);
            this.spotLatitude = itemView.findViewById(R.id.spotLatitude);
            this.spotInfo = itemView.findViewById(R.id.spotInfo);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }
        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            if (mClickListener != null) mClickListener.onLongClick(view, getAdapterPosition());
            return false;
        }
    }
    void setClickListener(ClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }
}
