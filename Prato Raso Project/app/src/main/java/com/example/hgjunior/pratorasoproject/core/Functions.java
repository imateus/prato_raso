package com.example.hgjunior.pratorasoproject.core;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Base64;

public class Functions {

    private static ProgressDialog progressDialog;

    public static void pleaseWait(Context context, String title, String message){

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    public static void pleaseWaitClose(){
        progressDialog.dismiss();
    }

    public static String encodeBase64(String text){
        return Base64.encodeToString(text.getBytes(), Base64.DEFAULT).replaceAll( "(\\n|\\r)", "");
    }

    public static String dencodeBase64(String textDecode){
        return new String(Base64.decode(textDecode, Base64.DEFAULT));
    }

}
