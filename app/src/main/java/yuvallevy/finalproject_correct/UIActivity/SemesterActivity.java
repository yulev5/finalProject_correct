package yuvallevy.finalproject_correct.UIActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;

import yuvallevy.finalproject_correct.JavaObjects.NewSearchingRecord;
import yuvallevy.finalproject_correct.R;

public class SemesterActivity extends Activity {

    private Spinner spinner;
    private Button btnProceed;
    private ArrayList<String> relevantSemesters = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private NewSearchingRecord curSearchRecord = new NewSearchingRecord();      //New Searching object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.semester_layout);

        btnProceed = (Button) findViewById(R.id.btnProceed1);
        spinner = (Spinner) findViewById(R.id.spinner);

        //Create a list of correct semesters to display in a list - by date
        getRelevantSemester();

        //Fill spinner with correct data
        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, relevantSemesters);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get selected semester - and save into new searching object
                curSearchRecord.setSemester(spinner.getSelectedItem().toString());

                Intent courseActivity = new Intent("yuvallevy.finalproject_correct.COURSEACTIVITY");
                courseActivity.putExtra("course", curSearchRecord);
                startActivity(courseActivity);
            }
        });

    }

    private void getRelevantSemester() {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(cal.YEAR);
        int month = cal.get(cal.MONTH) + 1; //zero-based

        switch (month) {
            case 1:
            case 2:
            case 3:
            case 4:
                relevantSemesters.add(String.valueOf(year) + "א");
                relevantSemesters.add(String.valueOf(year) + "ב");
                relevantSemesters.add(String.valueOf(year) + "ג");
                relevantSemesters.add(String.valueOf(year + 1) + "א");
                break;
            case 5:
            case 6:
            case 7:
            case 8:
                relevantSemesters.add(String.valueOf(year) + "ב");
                relevantSemesters.add(String.valueOf(year) + "ג");
                relevantSemesters.add(String.valueOf(year + 1) + "א");
                relevantSemesters.add(String.valueOf(year + 1) + "ב");
                break;
            case 9:
            case 10:
            case 11:
            case 12:
                relevantSemesters.add(String.valueOf(year) + "ג");
                relevantSemesters.add(String.valueOf(year + 1) + "א");
                relevantSemesters.add(String.valueOf(year + 1) + "ב");
                relevantSemesters.add(String.valueOf(year + 1) + "ג");
                break;
        }

    }


}