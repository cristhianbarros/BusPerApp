package com.busperapp.util;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.busperapp.R;
import com.busperapp.login.ui.LoginActivity;

public class SplashScreen extends AppCompatActivity {

    public static final int segundos = 8;
    public static final int milisegundos =segundos * 1000;
    private ProgressBar mProgressBar;
    public static final int delay = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mProgressBar.setMax(maxProgress());
        Inicializar();



        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    public void Inicializar(){

        new CountDownTimer(milisegundos,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mProgressBar.setProgress(progress(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                Intent I = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(I);
                finish();

            }
        }.start();
    }

    public int progress(long param){
        return (int)((milisegundos-param)/1000);
    }

    public int maxProgress (){
        return (segundos-delay);
    }

}
