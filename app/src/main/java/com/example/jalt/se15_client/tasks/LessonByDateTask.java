package com.example.jalt.se15_client.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.jalt.se15_client.StudeasyScheduleApplication;

import common.LessonResponse;

/**
 * Created by ErfiMac on 20.06.15.
 * @author Lukas Erfkämper
 */
public class LessonByDateTask extends AsyncTask<Object, Void, LessonResponse> {
    private Context context;
    private StudeasyScheduleApplication myApp;
    private int sessionID;
    private String date;
    private int hour;

    public LessonByDateTask(Context context, StudeasyScheduleApplication myApp) {
        this.context = context;
        this.myApp = myApp;
    }

    @Override
    /**
     * myResponse vorbereiten
     */
    protected LessonResponse doInBackground(Object... params){
        if(params.length != 3)
            return null;
        sessionID = (int) params[0];
        date = (String) params[1];
        hour = (int) params[2];
        try {
            LessonResponse myResponse = myApp.getStudeasyScheduleService().getLessonByDate(sessionID, date, hour);
            return myResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onProgessUpdate(Integer... values)
    {

    }

    /**
     * result Auswertung, bei Erfolg werden die Lessons angezeigt, ansonsten Fehlermeldung
     * @param result
     */
    protected void onPostExecute(LessonResponse result)
    {
        if(result != null)
        {
            Toast.makeText(context, "LessonByDate Abfrage erfolgreich.", Toast.LENGTH_LONG).show();

        }
        else
        {
            //Toast anzeigen
            Toast.makeText(context, "LessonByDate Abfrage fehlgeschlagen.", Toast.LENGTH_LONG).show();
        }
    }
}
