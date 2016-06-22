package com.busperapp.object.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.busperapp.R;

public class DetailObjectActivity extends AppCompatActivity {

    private TextView txtKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_object);


        Intent i = getIntent();

        txtKey = (TextView) findViewById(R.id.txtKey);

        if(i.hasExtra("key")) {

            txtKey.setText("Estes es el Key para Consultar del Objeto Perdido: "+i.getExtras().getString("key"));

        }



    }
}
