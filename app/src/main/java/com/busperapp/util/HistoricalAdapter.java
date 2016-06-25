package com.busperapp.util;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import com.busperapp.R;
import com.busperapp.entities.ObjectLost;
import com.busperapp.object.ui.DetailObjectActivity;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.StorageReference;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by agrajava on 23/06/2016.
 */
public class HistoricalAdapter extends RecyclerView.Adapter<HistoricalAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ObjectLost> mHistoricalObjects;
    private FirebaseHelper helper = new FirebaseHelper(FirebaseHelper.OBJECT_LOST_PATH);
    final StorageReference storageReference = helper.getmStorage().getReferenceFromUrl("gs://luminous-fire-2940.appspot.com");


    public HistoricalAdapter(ArrayList<ObjectLost> HistoricalObjects){
        this.mHistoricalObjects = HistoricalObjects;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private TextView descripcion;
        private ImageView imagen;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            descripcion =(TextView) v.findViewById(R.id.descripcion);
            imagen = (ImageView) v.findViewById(R.id.imagen);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mockup_object_historical, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ObjectLost ObjHistorical = mHistoricalObjects.get(position);
        StorageReference objectLostPhoto = storageReference.child("images/" + ObjHistorical.getKey());
        Glide.with(holder.itemView.getContext())
                .using(new FirebaseImageLoader())
                .load(objectLostPhoto)
                .into(holder.imagen);
        holder.title.setText(ObjHistorical.getTitle());
        holder.descripcion.setText(ObjHistorical.getDescription());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(context,DetailObjectActivity.class);
                I.putExtra("key", ObjHistorical.getKey());
                context.startActivity(I);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mHistoricalObjects.size();
    }

}

class FirebaseImageLoader implements StreamModelLoader<StorageReference> {

    @Override
    public DataFetcher<InputStream> getResourceFetcher(StorageReference model, int width, int height) {
        return new FirebaseStorageFetcher(model);
    }

    private class FirebaseStorageFetcher implements DataFetcher<InputStream> {

        private StorageReference storageReference;

        FirebaseStorageFetcher(StorageReference ref) {
            storageReference = ref;
        }

        @Override
        public InputStream loadData(Priority priority) throws Exception {
            return Tasks.await(storageReference.getStream()).getStream();
        }

        @Override
        public void cleanup() {
            // No cleanup possible, Task does not expose cancellation
        }

        @Override
        public String getId() {
            return storageReference.getPath();
        }

        @Override
        public void cancel() {
            // No cancellation possible, Task does not expose cancellation
        }
    }
}
