package com.example.hgjunior.pratorasoproject.connections;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.hgjunior.pratorasoproject.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ConnectionPreferences {

    private Context context;
    private SharedPreferences sharedPreferences;
    private String file_name = "LOGIN_USER.preferences";
    private SharedPreferences.Editor editor;

    private final String KEY_IDENTIFIER = "id_user_login";
    private final String KEY_EMAIL = "email_user_login";

    public ConnectionPreferences(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(file_name, context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUserPreferences (String idUser, String emailUser){
        editor.putString(KEY_IDENTIFIER, idUser);
        editor.putString(KEY_EMAIL, emailUser);
        editor.apply();
    }

    public String getIdPreferences(){
        return sharedPreferences.getString(KEY_IDENTIFIER, null);
    }

    public String getEmailPreferences(){
        return sharedPreferences.getString(KEY_EMAIL, null);
    }

    public void logOut(){
        editor.clear();
        editor.commit();
    }

}
