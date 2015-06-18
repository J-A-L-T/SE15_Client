package com.example.jalt.se15_client.tasks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.example.jalt.se15_client.MainActivity;
import com.example.jalt.se15_client.StudeasyScheduleApplication;

import java.util.Objects;

import common.UserLoginResponse;

/**
 * Logout as AsyncTask
 */
public class LoginTask extends AsyncTask<Object, Void, UserLoginResponse> {

    private Context context;
    private StudeasyScheduleApplication myApp;
    private int personid;
    private String password;
    SharedPreferences sharedPreferences;

    public LoginTask(Context context, StudeasyScheduleApplication myApp) {
        this.context = context;
        this.myApp = myApp;
    }

    @Override
    protected UserLoginResponse doInBackground(Object... params){
        if(params.length != 2)
            return null;
        personid = (int) params[0];
        password = (String) params[1];
        try {
            UserLoginResponse myResponse = myApp.getStudeasyScheduleService().login(personid, password);
            return myResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onProgessUpdate(Integer... values)
    {
        //wird in diesem Beispiel nicht verwendet
    }

    protected void onPostExecute(UserLoginResponse result)
    {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String User = sharedPreferences.getString("USER", "");
        String Password = sharedPreferences.getString("PASSWORD", "");
        if(result != null)
        {
            SavePreferences("USER", "" + personid);
            SavePreferences("PASSWORD", password);
            //Toast anzeigen
            CharSequence text = "Willkommen User " + personid;
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            context.startActivity(new Intent(context, MainActivity.class));
        }
        else
        {
            //Toast anzeigen
            CharSequence text = "Login fehlgeschlagen!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }
    private void SavePreferences(String key, String value)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
}