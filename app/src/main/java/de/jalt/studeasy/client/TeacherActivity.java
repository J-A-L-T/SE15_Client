package de.jalt.studeasy.client;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jalt.studeasy.client.R;

import de.jalt.studeasy.client.tasks.AddHomeworkTask;

/**
 * Spezielle Teacher Ansicht zum Anlegen der Hausaufgaben
 * @author Jan Mußenbrock und Lukas Erfkämper
 */
public class TeacherActivity extends Activity {

    EditText homeworkText;
    Button finishButton;
    int lessonID;
    long dateInMillis;
    String teacherId;
    SharedPreferences sharedPreferences;


    /**
     * Spezielles PopUpWindow für den Teacher, wird über der SubjectActivity eingeblendet
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        homeworkText = (EditText) findViewById(R.id.teacher_editText);
        finishButton = (Button) findViewById(R.id.finish_button);
        Intent teacherIntent = getIntent();
        lessonID = teacherIntent.getExtras().getInt("lessonId");
        teacherId = teacherIntent.getExtras().getString("teacherId");
        dateInMillis = teacherIntent.getExtras().getLong("dateInMillis");
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int heigth = dm.heightPixels;
        getWindow().setLayout((int) (width*.8),(int) (heigth*.35));
    }

    /**
     * Abrufen der Lesson mit dazugehörigen ID und Methode addHomework
     * @param view
     */
    public void finishButtonPress(View view) {
        String homework = homeworkText.getText().toString();
        if (homework.equals(""))
        {
            Toast.makeText(this, getString(R.string.missingHomework), Toast.LENGTH_SHORT).show();
        }
        else
        {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            int sessionId = Integer.parseInt(sharedPreferences.getString("SESSIONID", ""));
            StudeasyScheduleApplication myApp = (StudeasyScheduleApplication) getApplication();
            AddHomeworkTask homeworkTask = new AddHomeworkTask(this, myApp);
            homeworkTask.execute(sessionId, lessonID, homework, teacherId, dateInMillis);
        }
    }
}
