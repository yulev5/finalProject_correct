package yuvallevy.finalproject_correct.UIActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

import yuvallevy.finalproject_correct.DATABase.DB_SQLiteManipulation;
import yuvallevy.finalproject_correct.DATABase.DBmanipulation;
import yuvallevy.finalproject_correct.DATABase.MySharedPreferences;
import yuvallevy.finalproject_correct.JavaObjects.NewSearchingRecord;
import yuvallevy.finalproject_correct.R;
import yuvallevy.finalproject_correct.JavaObjects.Student;

/**
 * Created by UValerX073037 on 3/19/2016.
 */
public class CityActivity extends Activity {

    final Context context = this;
    private Spinner spinner;
    private NewSearchingRecord curSearchRecord;
    private Button btnProceed;
    private DBmanipulation myDB = new DBmanipulation();
    private ArrayList<Student> studentsFound = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_layout);

        //Get new searching data object until now - until now - semester + course
        Intent i = getIntent();
        curSearchRecord = (NewSearchingRecord) i.getSerializableExtra("searchingRecord");

        btnProceed = (Button) findViewById(R.id.btnProceed3);
        spinner = (Spinner) findViewById(R.id.spinnerForCity);

        // setting spinner data with cities data from strings.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinnerarrayForCity, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curSearchRecord.setPlace(spinner.getSelectedItem().toString());
                //Async Task used for short operations in different thread
                DBExecuteForFindFast myDB = new DBExecuteForFindFast();
                myDB.execute();
            }
        });
    }


    //-------------------------AsyncTask-------------------------
    //Start new thread for DB connection and continue of the flow
    class DBExecuteForFindFast extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            MySharedPreferences mySharedPreferences = new MySharedPreferences(getApplicationContext());
            //If the user is already registered - add new course to his course list - by selected searching values

            //Search for partners for selected values in newSearchingRecord object
            if (mySharedPreferences.getIsRegistered()) {
                studentsFound = myDB.findPartnersFast(mySharedPreferences.getMyEmail(), curSearchRecord.getSemester(), curSearchRecord.getName(), curSearchRecord.getPlace());
                myDB.insertNewSearchingRecord(mySharedPreferences.getMyEmail(), curSearchRecord.getSemester(), curSearchRecord.getName(), curSearchRecord.getPlace());
                //Insert new course record to personal courses in SQLite
                DB_SQLiteManipulation SQLiteDB = new DB_SQLiteManipulation(getApplicationContext());
                SQLiteDB.addCourseToMyCourses(curSearchRecord);
            } else {
                //Student is not registered - find partner fast for non registered users
                studentsFound = myDB.findPartnersFast(null, curSearchRecord.getSemester(), curSearchRecord.getName(), curSearchRecord.getPlace());
            }

            //Number of students found in partner search
            Integer numberOfStudentFound = studentsFound.size();
            return numberOfStudentFound;
        }

        @Override
        protected void onPostExecute(final Integer numberOfStudent) {
            super.onPostExecute(numberOfStudent);
            //First option- no partners found
            if (numberOfStudent == 0) {
                MySharedPreferences mySharedPreferences = new MySharedPreferences(getApplicationContext());
                //No match for the current search
                if (mySharedPreferences.getIsRegistered()) {
                    //User already registered
                    new AlertDialog.Builder(context)
                            .setTitle("חיפוש שותף")
                            .setMessage(getString(R.string.firstStudent) + "\n" + getString(R.string.firstStudentRegistered))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent backToProfile = new Intent("yuvallevy.finalproject_correct.REGISTEREDUSERPROFILE");
                                    startActivity(backToProfile);
                                }
                            }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                            .setIcon(android.R.drawable.gallery_thumb)
                            .show();
                } else {
                    //User not registered - 0 matches found
                    new AlertDialog.Builder(context)
                            .setTitle("חיפוש שותף")
                            .setMessage(getString(R.string.firstStudent) + "\n" + getString(R.string.firstStudentNotRegistered))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent toRegister = new Intent("yuvallevy.finalproject_correct.REGISTER");
                                    toRegister.putExtra("CurrentStatus", "FirstUser_NotRegister");
                                    toRegister.putExtra("searchingRecord", curSearchRecord);
                                    startActivity(toRegister);
                                }
                            }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                            .setIcon(android.R.drawable.gallery_thumb)
                            .show();
                }
            } else {
                //Found some other student match
                //User already registered
                new AlertDialog.Builder(context)
                        .setTitle("חיפוש שותף")
                        .setMessage(getString(R.string.NotFirstStart) + " " + numberOfStudent + " " + getString(R.string.NotFirstEnd))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent searchResultActivity = new Intent("yuvallevy.finalproject_correct.SEARCHRESULT");
                                MySharedPreferences mySharedPreferences = new MySharedPreferences(getApplicationContext());
                                if (!mySharedPreferences.getIsRegistered()) {
                                    searchResultActivity.putExtra("searchingRecord", curSearchRecord);
                                }
                                searchResultActivity.putExtra("studentsFound", studentsFound);
                                startActivity(searchResultActivity);
                            }
                        }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                        .setIcon(android.R.drawable.gallery_thumb)
                        .show();
            }
        }
    }
}

