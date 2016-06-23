package com.busperapp.util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.busperapp.R;
import com.busperapp.entities.ObjectLost;

import java.util.ArrayList;

/**
 * Created by agrajava on 23/06/2016.
 */
public class HistoricalAdapter extends RecyclerView.Adapter<HistoricalAdapter.ViewHolder> {

    ArrayList<ObjectLost> mHistoricalObjects;

    public HistoricalAdapter(ArrayList<ObjectLost> HistoricalObjects){
        this.mHistoricalObjects = HistoricalObjects;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView title;
        public TextView descripcion;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            descripcion =(TextView) v.findViewById(R.id.descripcion);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mockup_object_historical, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ObjectLost ObjHistorical = mHistoricalObjects.get(position);
        holder.title.setText(ObjHistorical.getTitle());
        holder.descripcion.setText(ObjHistorical.getDescription());
    }

    @Override
    public int getItemCount() {
        return mHistoricalObjects.size();
    }

}
