package com.busperapp.util;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Util {

    public static void showMessage(Context c, String msj) {
        Toast.makeText(c, msj, Toast.LENGTH_SHORT).show();
    }

    public static void showSnackbar(View v, String msj) {
        Snackbar.make(v, msj, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();

    }

    public static void UnderlineText(View param, String text){
        TextView v =(TextView) param;
        v.setText(Html.fromHtml("<u>" + text + "</u>"));
    }

}
