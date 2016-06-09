package com.busperapp.util;

import android.content.Context;
import android.widget.Toast;

public class Util {

    public static void showMessage(Context c, String msj) {
        Toast.makeText(c, msj, Toast.LENGTH_SHORT).show();
    }


}
