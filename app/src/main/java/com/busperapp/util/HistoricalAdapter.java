package com.busperapp.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.busperapp.MainActivity;
import com.busperapp.R;
import com.busperapp.entities.ObjectLost;
import com.busperapp.object.ui.AddObjectActivity;
import com.busperapp.object.ui.DetailObjectActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by agrajava on 23/06/2016.
 */
public class HistoricalAdapter extends RecyclerView.Adapter<HistoricalAdapter.ViewHolder> {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mRef = database.getReference();
    android.support.v4.app.FragmentManager mFramgmentManager = MainActivity.mFramgmentManager;
    private Context context;
    private String ruta;
    private ArrayList<ObjectLost> mHistoricalObjects;
    private FirebaseHelper helper = FirebaseHelper.getInstance();
    final StorageReference storageReference = helper.getmStorage().getReferenceFromUrl("gs://luminous-fire-2940.appspot.com");

    public HistoricalAdapter(ArrayList<ObjectLost> HistoricalObjects){
        this.mHistoricalObjects = HistoricalObjects;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private TextView descripcion;
        private ImageView imagen;
        private ImageView overflow;
        private TextView ruta;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            descripcion =(TextView) v.findViewById(R.id.descripcion);
            imagen = (ImageView) v.findViewById(R.id.imagen);
            overflow = (ImageView) v.findViewById(R.id.overflow);
            ruta = (TextView) v.findViewById(R.id.ruta);
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ObjectLost ObjHistorical = mHistoricalObjects.get(position);
        //final StorageReference objectLostPhoto =
            storageReference.child("images/" + ObjHistorical.getKey()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    //ruta = uri.toString();
                    holder.ruta.setText(uri.toString());
                    Glide.with(holder.itemView.getContext())
                            //.using(new FirebaseImageLoader())
                            .load(uri).listener(new RequestListener<Uri, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    }).into(holder.imagen);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.getMessage();
                }
            });

        //Toast.makeText(context,ruta,Toast.LENGTH_LONG).show();
        /*Glide.with(holder.itemView.getContext())
                .using(new FirebaseImageLoader())
                .load(objectLostPhoto)
                .into(holder.imagen);*/
        holder.title.setText(ObjHistorical.getTitle());
        holder.descripcion.setText(ObjHistorical.getDescription());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent I = new Intent(context, DetailObjectActivity.class);
                I.putExtra("key", ObjHistorical.getKey());
                context.startActivity(I);
                return false;
            }
        });

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // showPopupMenu(holder.overflow, ObjHistorical, objectLostPhoto);
                showPopupMenu(holder.overflow, ObjHistorical, holder.ruta.getText().toString(),position);
            }
        });
    }

    private void showPopupMenu(View view, ObjectLost o, String r,int position) {

        // inflate menu
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_historical, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(o, r,position));
        popup.show();

    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        ObjectLost mObjectLost;
        //StorageReference mStoreReference;
        int position;
        String ruta;

        public MyMenuItemClickListener(ObjectLost o,String r,int position) {
            this.mObjectLost = o;
            this.ruta = r;
            this.position = position;

        }

        public Intent initIntent(Context c,ObjectLost o,String ruta){
            Intent i = new Intent(c, AddObjectActivity.class);
            i.putExtra("action", "edit");
            i.putExtra("mTitle", o.getTitle());
            i.putExtra("mDescription", o.getDescription());
            i.putExtra("mCategory", o.getCategory());
            i.putExtra("mImage",ruta);
            i.putExtra("mLatitude", o.getUbicationLatLang().get("latitude"));
            i.putExtra("mLongitude", o.getUbicationLatLang().get("longitude"));
            i.putExtra("mKey", o.getKey());
            i.putExtra("mPostalCode", o.getPostalCode());
            i.putExtra("mAddress", o.getAddress());
            return i;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.edit:

                    if(mObjectLost != null) {
                        Intent i = initIntent(context,mObjectLost,this.ruta);
                        context.startActivity(i);
                        /*
                        storageReference.child("images/"+ mObjectLost.getKey()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Intent i = initIntent(context,mObjectLost,uri.toString());
                                context.startActivity(i);
                                //Toast.makeText(context,uri.toString(),Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Util.showMessage(context, exception.getMessage());
                            }
                        });*/
                        //Toast.makeText(context,this.direccion, Toast.LENGTH_SHORT).show();
                    }

                    return true;
                case R.id.delete:
                    final AlertDialog.Builder Alertdialog = new AlertDialog.Builder(context);
                    Alertdialog.setMessage("Estas seguro que deseas eliminar el objeto ?")
                            .setCancelable(false)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mRef.child(FirebaseHelper.OBJECT_LOST_PATH).child(mObjectLost.getKey()).removeValue();
                                    storageReference.child("images/" + mObjectLost.getKey()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(context,"El objeto se elimino correctamente",Toast.LENGTH_LONG).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            exception.getMessage();
                                        }
                                    });
                                    /*mFramgmentManager
                                            .beginTransaction()
                                            .replace(R.id.main_content, new HistorialFragment())
                                            .commit();*/
                                    mHistoricalObjects.remove(position);


                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }

                    });
                    Alertdialog.show();
                    return true;
                default:
            }
            return false;
        }
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
