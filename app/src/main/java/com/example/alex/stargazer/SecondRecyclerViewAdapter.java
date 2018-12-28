package com.example.alex.stargazer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.List;

public class SecondRecyclerViewAdapter extends RecyclerView.Adapter<SecondRecyclerViewAdapter.ViewHolder> {


    @NonNull
    private RecyclerViewAdapter.ClickListener mClickListener;

    private Context mContext;
    private List<Constellation> constellations;

    public SecondRecyclerViewAdapter(List<Constellation> constellations, Context context) {
        this.constellations = constellations;
        this.mContext = context;
    }


    @Override
    public int getItemCount() {
        return constellations.size();
    }

    @NonNull
    @Override
    public SecondRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.second_layout_listitem, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.constName.setText(constellations.get(i).getName());

    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        TextView constName;

        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.constName = itemView.findViewById(R.id.spotName);

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
    void setClickListener(RecyclerViewAdapter.ClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }
}


