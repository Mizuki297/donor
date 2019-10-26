package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {
    private SharedPreferences presf;
    public Session(Context cntx){
        presf = PreferenceManager.getDefaultSharedPreferences(cntx);
    }
    public void setUserId (String val){
        presf.edit().putString("user_id",val).commit();
    }
    public String getUserId (){
        String user_id = presf.getString("user_id","");
        return user_id;
    }
    public void clearUserId(){
        presf.edit().clear().commit();
    }
}
