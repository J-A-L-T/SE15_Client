package de.jalt.studeasy.client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jalt.studeasy.client.R;

import de.jalt.studeasy.client.tasks.LessonByDateTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.jalt.studeasy.common.LessonResponse;
import de.jalt.studeasy.common.LessonTO;

/**
 * Startansich der Applikation
 * Portraitansicht: Stundenplan für den heutigen Tag mit Vor- und Zurück-Button für Tage
 * Queransicht: Stundenplan für die aktuelle Woche mit Vor- und Zurück-Button für Wochen
 * @author Jan Mußenbrock und Lukas Erfkämper
 */
public class MainActivity extends ActionBarActivity {

    SharedPreferences sharedPreferences;
    Calendar dateTo;
    Calendar date;
    long dateInMillis;
    SparseArray<TableLayout> cellMap;
    SparseArray<TextView> teacherMap;
    SparseArray<TextView> roomMap;
    SparseArray<TextView> subjectMap;
    SparseArray<TextView> textMap;
    SparseArray<String> dateMap;

    int sessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        StudeasyScheduleApplication myApp = (StudeasyScheduleApplication) getApplication();

        // Festlegung des genormten Datestring zur Kommunikation mit dem Server
        DateFormat ttmmjjjj = new SimpleDateFormat("ddMMyyyy", Locale.GERMAN);

        // Abfrage der gespeicherten SessionID. Wird für einige Anfragen an den Server benötigt.
        if (!sharedPreferences.getString("SESSIONID", "").equals(""))
        {
            sessionId = Integer.parseInt(sharedPreferences.getString("SESSIONID", ""));
        }

        // Logik zum Überspringen des Wochenendes für das HEUTIGE DATUM
        dateTo = Calendar.getInstance();
        date = Calendar.getInstance();
        if (date.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
            date.add(Calendar.DATE, 2);
        }
        if (date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            date.add(Calendar.DATE, 1);
        }

        // Abfrage zu Herkunfts-Activities
        Intent intent = getIntent();
        if (intent.getExtras() != null)
        {
            // Woher kommt der Activity-Aufruf? Davon hängt ab, welche extras mitgesandt wurden
            if (intent.getExtras().getString("origin") != null) {
                // Wenn man von Main im Hochformat kommt kann man ein neues Datum erwarten. (Vorheriger oder folgender Tag wie in der Herkunfts-Aktivity)
                if (intent.getExtras().getString("origin").equals("main")) {
                    dateInMillis = intent.getExtras().getLong("dateInMillis");
                    date.setTimeInMillis(dateInMillis);
                    // Übergabe der Animation abhängig von Next- oder Previous-Methode
                    if (intent.getExtras().getString("direction").equals("right")) {
                        this.overridePendingTransition(R.anim.slide_in_right, 17432577);
                    }
                    // Übergabe der Animation abhängig von Next- oder Previous-Methode
                    if (intent.getExtras().getString("direction").equals("left")) {
                        this.overridePendingTransition(R.anim.slide_in_left, 17432577);
                    }
                }
                if (intent.getExtras().getString("origin").equals("subject")) {
                    dateInMillis = intent.getExtras().getLong("dateInMillis");
                    date.setTimeInMillis(dateInMillis);
                }
            }
        }

        //-----------------------------------           Logik für die PORTRAITANSICHT           -----------------------------------
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            // Hier wird das Datum für die Anzeige im Kopf der Tabelle aufbereitet und blau gefärbt, wenn es das heutige Datum ist.
            DateFormat dfmt = new SimpleDateFormat("E dd.MM.yy", Locale.GERMAN);
            final TextView dateText = (TextView) findViewById(R.id.daytoday);
            dateText.setText(dfmt.format(date.getTime()).toString());
            if (date.get(Calendar.DAY_OF_YEAR) == dateTo.get(Calendar.DAY_OF_YEAR)){
                dateText.setBackgroundResource(R.color.Light_Blue);
            }

            // Map/Arrays zum einfachen Addressieren der Verschiedenen TextViews in unserem Tabellenlayout
            cellMap = new SparseArray<>();
            teacherMap = new SparseArray<>();
            subjectMap = new SparseArray<>();
            roomMap = new SparseArray<>();

            // Zuweisen der Zellen und TextVIews
            cellMap.put(1, (TableLayout) findViewById(R.id.dayclass1));
            teacherMap.put(1, (TextView) findViewById(R.id.dayclass1teacher));
            subjectMap.put(1, (TextView) findViewById(R.id.dayclass1subject));
            roomMap.put(1, (TextView) findViewById(R.id.dayclass1room));

            cellMap.put(2, (TableLayout) findViewById(R.id.dayclass2));
            teacherMap.put(2, (TextView) findViewById(R.id.dayclass2teacher));
            subjectMap.put(2, (TextView) findViewById(R.id.dayclass2subject));
            roomMap.put(2, (TextView) findViewById(R.id.dayclass2room));

            cellMap.put(3, (TableLayout) findViewById(R.id.dayclass3));
            teacherMap.put(3, (TextView) findViewById(R.id.dayclass3teacher));
            subjectMap.put(3, (TextView) findViewById(R.id.dayclass3subject));
            roomMap.put(3, (TextView) findViewById(R.id.dayclass3room));

            cellMap.put(4, (TableLayout) findViewById(R.id.dayclass4));
            teacherMap.put(4, (TextView) findViewById(R.id.dayclass4teacher));
            subjectMap.put(4, (TextView) findViewById(R.id.dayclass4subject));
            roomMap.put(4, (TextView) findViewById(R.id.dayclass4room));

            cellMap.put(5, (TableLayout) findViewById(R.id.dayclass5));
            teacherMap.put(5, (TextView) findViewById(R.id.dayclass5teacher));
            subjectMap.put(5, (TextView) findViewById(R.id.dayclass5subject));
            roomMap.put(5, (TextView) findViewById(R.id.dayclass5room));

            cellMap.put(6, (TableLayout) findViewById(R.id.dayclass6));
            teacherMap.put(6, (TextView) findViewById(R.id.dayclass6teacher));
            subjectMap.put(6, (TextView) findViewById(R.id.dayclass6subject));
            roomMap.put(6, (TextView) findViewById(R.id.dayclass6room));

            String dateString = ttmmjjjj.format(date.getTime());

            //Fächerfarben, Lehrernamen, Fachnamen und Raumnummer werden per Schleife anhand der empfangenen LessonObjekte befüllt.
            // Ist überhaupt ein User eingeloggt?
            if (!sharedPreferences.getString("SESSIONID", "").equals(""))
            {
                for (int i=1; i<7; i++) {
                    final int j = i;
                    final String dateStringF = dateString;
                    new LessonByDateTask(this, myApp) {
                        @Override
                        public void onPostExecute(LessonResponse result) {
                            if(result != null)
                            {
                            Log.i("LessonByDateTask","( " + sessionId + ", " + dateStringF + ", " + j + " ) ");
                            Log.i("LessonByDateTask", "erfolgreich");
                            final LessonTO lesson = result.getLesson();
                            cellMap.get(j).setBackgroundResource(ColorChooser.getColorFromId(lesson.getSubject().getSubjectID()));
                            subjectMap.get(j).setText(lesson.getSubject().getDescription());
                            SavePreferences("LESSONROW" + j, lesson.getSubject().getDescription());
                            SavePreferences("LESSONCOLOR" + j, String.valueOf(ColorChooser.getColorFromId(lesson.getSubject().getSubjectID())));
                            teacherMap.get(j).setText(GenderChooser.getTitleByGender(lesson.getTeacher().getGender()) + " " + lesson.getTeacher().getName());
                            roomMap.get(j).setText(lesson.getRoom());
                            cellMap.get(j).setOnClickListener(new View.OnClickListener(){public void onClick(View v) {onSubjectClick(v, lesson.getLessonID(), String.valueOf(lesson.getTeacher().getPersonID()));}});
                            }
                            else
                            {
                                subjectMap.get(j).setText("Freistunde");
                                Log.i("LessonByDateTask", "Freistunde");
                                SavePreferences("LESSONROW" + j, "Freistunde");
                            }
                        }
                    }.execute(sessionId, dateString, i);
                }
            }
        }

        //-----------------------------------           Logik für die QUERANSICHT           -----------------------------------
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Es werden alle Wochentage der Woche des genannten Datums ermittelt
            dateMap = new SparseArray<>();
            Calendar dateMo = (Calendar) date.clone();
            dateMo.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            dateMap.put(1, ttmmjjjj.format(dateMo.getTime()));
            Calendar dateTu = (Calendar) date.clone();
            dateTu.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
            dateMap.put(2, ttmmjjjj.format(dateTu.getTime()));
            Calendar dateWe = (Calendar) date.clone();
            dateWe.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
            dateMap.put(3, ttmmjjjj.format(dateWe.getTime()));
            Calendar dateTh = (Calendar) date.clone();
            dateTh.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
            dateMap.put(4, ttmmjjjj.format(dateTh.getTime()));
            Calendar dateFr = (Calendar) date.clone();
            dateFr.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
            dateMap.put(5, ttmmjjjj.format(dateFr.getTime()));

            // Overlay-Info zur Woche (Datum: Von - Bis)
            DateFormat dfmt = new SimpleDateFormat("E dd.MM.yy", Locale.GERMAN);
            Toast.makeText(this, dfmt.format(dateMo.getTime()) + " - " + dfmt.format(dateFr.getTime()), Toast.LENGTH_SHORT).show();

            // Array zum einfachen Adressieren
            textMap = new SparseArray();
            // Befüllen der Map mit allen Zellen: Einerstelle Tag (Mo = 1, Di = 2, ...), Zehnerstelle Stunde (1, 2, ...)
            textMap.put(10, (TextView) findViewById(R.id.mo0));
            textMap.put(11, (TextView) findViewById(R.id.mo1));
            textMap.put(12, (TextView) findViewById(R.id.mo2));
            textMap.put(13, (TextView) findViewById(R.id.mo3));
            textMap.put(14, (TextView) findViewById(R.id.mo4));
            textMap.put(15, (TextView) findViewById(R.id.mo5));
            textMap.put(16, (TextView) findViewById(R.id.mo6));

            textMap.put(20, (TextView) findViewById(R.id.tu0));
            textMap.put(21, (TextView) findViewById(R.id.tu1));
            textMap.put(22, (TextView) findViewById(R.id.tu2));
            textMap.put(23, (TextView) findViewById(R.id.tu3));
            textMap.put(24, (TextView) findViewById(R.id.tu4));
            textMap.put(25, (TextView) findViewById(R.id.tu5));
            textMap.put(26, (TextView) findViewById(R.id.tu6));

            textMap.put(30, (TextView) findViewById(R.id.we0));
            textMap.put(31, (TextView) findViewById(R.id.we1));
            textMap.put(32, (TextView) findViewById(R.id.we2));
            textMap.put(33, (TextView) findViewById(R.id.we3));
            textMap.put(34, (TextView) findViewById(R.id.we4));
            textMap.put(35, (TextView) findViewById(R.id.we5));
            textMap.put(36, (TextView) findViewById(R.id.we6));

            textMap.put(40, (TextView) findViewById(R.id.th0));
            textMap.put(41, (TextView) findViewById(R.id.th1));
            textMap.put(42, (TextView) findViewById(R.id.th2));
            textMap.put(43, (TextView) findViewById(R.id.th3));
            textMap.put(44, (TextView) findViewById(R.id.th4));
            textMap.put(45, (TextView) findViewById(R.id.th5));
            textMap.put(46, (TextView) findViewById(R.id.th6));

            textMap.put(50, (TextView) findViewById(R.id.fr0));
            textMap.put(51, (TextView) findViewById(R.id.fr1));
            textMap.put(52, (TextView) findViewById(R.id.fr2));
            textMap.put(53, (TextView) findViewById(R.id.fr3));
            textMap.put(54, (TextView) findViewById(R.id.fr4));
            textMap.put(55, (TextView) findViewById(R.id.fr5));
            textMap.put(56, (TextView) findViewById(R.id.fr6));

            // Färben des Spaltenkopfs wenn das Datum HEUTE ist.
            if (dateMo.get(Calendar.DAY_OF_YEAR) == dateTo.get(Calendar.DAY_OF_YEAR)) {
                textMap.get(10).setBackgroundResource(R.color.Light_Blue);
            }
            if (dateTu.get(Calendar.DAY_OF_YEAR) == dateTo.get(Calendar.DAY_OF_YEAR)) {
                textMap.get(20).setBackgroundResource(R.color.Light_Blue);
            }
            if (dateWe.get(Calendar.DAY_OF_YEAR) == dateTo.get(Calendar.DAY_OF_YEAR)) {
                textMap.get(30).setBackgroundResource(R.color.Light_Blue);
            }
            if (dateTh.get(Calendar.DAY_OF_YEAR) == dateTo.get(Calendar.DAY_OF_YEAR)) {
                textMap.get(40).setBackgroundResource(R.color.Light_Blue);
            }
            if (dateFr.get(Calendar.DAY_OF_YEAR) == dateTo.get(Calendar.DAY_OF_YEAR)) {
                textMap.get(50).setBackgroundResource(R.color.Light_Blue);
            }

            //Fächerfarben, Lehrernamen, Fachnamen und Raumnummer werden per Schleife anhand der empfangenen LessonObjekte befüllt.
            // Ist überhaupt ein User eingeloggt?
            if (!sharedPreferences.getString("SESSIONID", "").equals("")) {
                for (int i = 10; i < 60; i = i + 10) {
                    for (int j = 1; j < 7; j++) {
                        final int l = i;
                        final int m = j;
                        final int k = i + j;
                        new LessonByDateTask(this, myApp) {
                            @Override
                            public void onPostExecute(LessonResponse result) {
                                if (result != null) {
                                    final LessonTO lesson = result.getLesson();
                                    textMap.get(k).setBackgroundResource(BorderChooser.getBorderFromId(lesson.getSubject().getSubjectID()));
                                    textMap.get(k).setText(lesson.getSubject().getDescription());
                                    textMap.get(k).setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {onSubjectClick(v, lesson.getLessonID(), lesson.getTeacher().getName());}});
                                    Log.i(" Zelle: " + k + " : ", " ( " + sessionId + ", " + dateMap.get(l / 10) + ", " + m + " ) ");
                                } else {
                                    Log.i(" Zelle: " + k + " : ", " ( " + sessionId + ", " + dateMap.get(l / 10) + ", " + m + " ) ");
                                    Log.i("LessonByDateTask", "Freistunde");
                                    textMap.get(k).setText("Frei");
                                }
                            }
                        }.execute(sessionId, dateMap.get(l / 10), m); // <-------- hier muss später der getLessonBydate()-task hin mit ttmmjjjj und hour als i
                    }
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSettingsClick(MenuItem item) {
        Intent getSettingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(getSettingsIntent);
    }

    /**
     * Wahl eines Fachs und Weiterleitung zur SubjectActivity
     * @param view
     * @param lessonId
     * @param teacherId die gespeicherte ID muss mitgeliefert werden,
     * da vor dem befüllen des Fachs, schon feststehen muss, ob der benutzer der Lehrer dieses Fachs ist.
     */
    public void onSubjectClick(View view, int lessonId, String teacherId) {
        Intent getSubjectIntent = new Intent(this, SubjectActivity.class);
        getSubjectIntent.putExtra("lessonId", lessonId);
        getSubjectIntent.putExtra("teacherId", teacherId);
        getSubjectIntent.putExtra("dateInMillis", date.getTimeInMillis());
        startActivity(getSubjectIntent);
    }

    /**
     * Aufruf des nächsten Tages im Hochformat
     * @param view
     */
    public void onNextClickP(View view){
        Intent getNextIntent = new Intent(this, MainActivity.class);
        // Datum der aktuellen Activity wird um einen Tag erhöht. Falls dies ein Wochenende ist wird zum Montag gesprungen
        date.add(Calendar.DATE, 1);
        if (date.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
            date.add(Calendar.DATE, 2);
        }
        if (date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            date.add(Calendar.DATE, 1);
        }
        // Datum wird in Millisekunden umgewandelt, da per Intent kein Date-Objekt mitgeliefert werden kann.
        getNextIntent.putExtra("dateInMillis", date.getTimeInMillis());
        // Angabe der Herkunt zum Handeln der Extras
        getNextIntent.putExtra("origin","main");
        // Angabe in welche Richtung die Animation ablaufen soll.
        getNextIntent.putExtra("direction","right");
        // Unterdrücken der Standardanimation
        getNextIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(getNextIntent);
    }
    /**
     * Aufruf des vorherigen Tages im Hochformat
     * (Kommentare wie bei "nächster TAG")
     * @param view
     */
    public void onPreviousClickP(View view){
        Intent getPreviousIntent = new Intent(this, MainActivity.class);
        date.add(Calendar.DATE, -1);
        if (date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            date.add(Calendar.DATE, -2);
        }
        if (date.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
            date.add(Calendar.DATE, -1);
        }
        getPreviousIntent.putExtra("dateInMillis", date.getTimeInMillis());
        getPreviousIntent.putExtra("origin","main");
        getPreviousIntent.putExtra("direction","left");

        getPreviousIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(getPreviousIntent);
    }
    /**
     * Aufruf der nächsten Woche im Querformat
     * (ähnlich wie "nächster TAG" jedoch mit 7 Tagen)
     * @param view
     */
    public void onNextClickL(View view){
        Intent getNextIntent = new Intent(this, MainActivity.class);
        date.add(Calendar.DATE, 7);
        getNextIntent.putExtra("dateInMillis", date.getTimeInMillis());
        getNextIntent.putExtra("origin","main");
        getNextIntent.putExtra("direction","right");
        getNextIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(getNextIntent);
    }
    /**
     * Aufruf der nächsten Woche im Querformat
     * (ähnlich wie "nächste WOCHE")
     * @param view
     */
    public void onPreviousClickL(View view){
        Intent getPreviousIntent = new Intent(this, MainActivity.class);
        date.add(Calendar.DATE, -7);
        getPreviousIntent.putExtra("dateInMillis", date.getTimeInMillis());
        getPreviousIntent.putExtra("origin","main");
        getPreviousIntent.putExtra("direction","left");
        getPreviousIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(getPreviousIntent);
    }

    /**
     * Hilfmethode zum Speichern in die SavePreferences
     * @param key
     * @param value
     */
    private void SavePreferences(String key, String value)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
}