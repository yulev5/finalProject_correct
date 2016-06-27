package yuvallevy.finalproject_correct.UIActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

import yuvallevy.finalproject_correct.DATABase.DB_SQLiteManipulation;
import yuvallevy.finalproject_correct.JavaObjects.NewSearchingRecord;
import yuvallevy.finalproject_correct.R;

/**
 * Created by UValerX073037 on 3/18/2016.
 */
public class CourseActivity extends Activity {

    private Spinner spinnerForDeg;
    private Spinner spinnerForCourse;
    private Button btnProceed;
    private NewSearchingRecord curSearchRecord;
    private boolean first = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_layout);

        Intent i = getIntent();
        //Get new searching data object until now
        curSearchRecord = (NewSearchingRecord) i.getSerializableExtra("course");

        btnProceed = (Button) findViewById(R.id.btnProceed2);
        spinnerForDeg = (Spinner) findViewById(R.id.spinnerForDeg);
        spinnerForCourse = (Spinner) findViewById(R.id.spinnerForCourse);
        spinnerForCourse.setVisibility(View.GONE);

        //Create spinner for study degree
        ArrayAdapter<CharSequence> adapterForDeg = ArrayAdapter.createFromResource(this, R.array.spinnerarrayForDeg, android.R.layout.simple_spinner_item);
        adapterForDeg.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerForDeg.setAdapter(adapterForDeg);


        //When user select degree - search in SQLite all courses for specific degree
        spinnerForDeg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!first) {
                    first = true;
                    return;
                }

                //Get selected degree - for getting related courses
                String selectedRealm = spinnerForDeg.getSelectedItem().toString();

                //Get relevant courses by selected degree
                DB_SQLiteManipulation SQLiteDB = new DB_SQLiteManipulation(getApplicationContext());
                ArrayList<String> arr = SQLiteDB.getCoursesByDegree(selectedRealm);

                //Display courses spinner
                ArrayAdapter<String> adapterForCourse = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arr);
                adapterForCourse.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerForCourse.setAdapter(adapterForCourse);
                spinnerForCourse.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (spinnerForDeg.getSelectedItemPosition() > 0) {
            spinnerForCourse.setVisibility(View.VISIBLE);
        }


        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curSearchRecord.setName(spinnerForCourse.getSelectedItem().toString());
                Intent cityActivity = new Intent("yuvallevy.finalproject_correct.CITYACTIVITY");
                cityActivity.putExtra("searchingRecord", curSearchRecord);
                startActivity(cityActivity);
            }
        });

    }


}