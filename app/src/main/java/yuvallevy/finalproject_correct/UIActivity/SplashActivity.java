package yuvallevy.finalproject_correct.UIActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import yuvallevy.finalproject_correct.DATABase.DB_SQLiteManipulation;
import yuvallevy.finalproject_correct.DATABase.DBmanipulation;
import yuvallevy.finalproject_correct.DATABase.MySharedPreferences;
import yuvallevy.finalproject_correct.R;

public class SplashActivity extends Activity {

    private DBmanipulation dbOfCourses = new DBmanipulation();      //Our Database Object
    private boolean isRegistered;
    private Context context;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        //Save application context in parameter
        this.context = getApplicationContext();

        //Create Database object
        DBExcuteForDrawCourses curDB = new DBExcuteForDrawCourses();
        //Start database connection of different thread
        curDB.execute();
    }

    //-----------------------AsyncTask-----------------------
    //First parameter - AsyncTask input parameter
    //Second Parameter - On progress prameter
    //Third parameter - passing parameter from doInBackground to onPost
    class DBExcuteForDrawCourses extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            //Create SQLite on device DB
            DB_SQLiteManipulation sqliteDB = new DB_SQLiteManipulation(context);

            //For Debug - uncomment if you want to delete SQLite DB
            sqliteDB.deleteDB();

            //Download, check version and save courses table if necessary
            sqliteDB.downloadTable("courses", dbOfCourses);

            //Debug - check print all courses in SQLite
            //sqliteDB.printAllCourses();

            return null;
        }

        protected void onPostExecute(Void result) {
            MySharedPreferences mySharedPreferences = new MySharedPreferences(context);

            //For Debug - Uncomment to check unregistered user
            //mySharedPreferences.changeRegisterUserStatus(true);

            isRegistered = mySharedPreferences.getIsRegistered();

            Intent intent;
            if (!isRegistered) {
                intent = new Intent("yuvallevy.finalproject_correct.FIRSTACTIVITY");
            } else {
                intent = new Intent("yuvallevy.finalproject_correct.REGISTEREDUSERPROFILE");
            }
            startActivity(intent);
        }

    }

}
