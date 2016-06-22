package com.busperapp.fragment.historial;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.busperapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistorialFragment extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mRef = database.getReference();

    public HistorialFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {


//        Category c = new Category("DOCUMENTOS","", true);
//        Category c2 = new Category("ANIMAL","", true);
//        Category c3 = new Category("VEHICULO","", true);
//        Category c4 = new Category("OTROS","", true);
//
//
//        ArrayList<Category> mArrayList = new ArrayList<>();
//        mArrayList.add(c);
//        mArrayList.add(c2);
//        mArrayList.add(c3);
//        mArrayList.add(c4);
//
//        for(int i = 0; i < mArrayList.size(); i++) {
//            mRef.child("categories").push().setValue(mArrayList.get(i));
//        }

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_historial, container, false);

//        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
//        DatabaseReference mRef = mDatabase.getReference();
//
//
//        Query queryRef = mRef.child("ObjectLost").orderByChild("ubicationLatLang/latitude").startAt(6.233).endAt(6.235);
//
//        queryRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                ObjectLost o = dataSnapshot.getValue(ObjectLost.class);
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//
//
//        });


        return v;

    }

}
