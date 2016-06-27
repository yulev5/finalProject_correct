package yuvallevy.finalproject_correct.UIActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import yuvallevy.finalproject_correct.DATABase.DB_SQLiteManipulation;
import yuvallevy.finalproject_correct.DATABase.MySharedPreferences;
import yuvallevy.finalproject_correct.R;

public class MyCoursesList extends Activity {
    private ListView list;
    private TextView studentNameInBar;
    private TextView studentEmailInSideBar;
    private ArrayList<String> myCourses = new ArrayList<>();
    private DB_SQLiteManipulation mySqLite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycourseslist);

        studentNameInBar = (TextView) findViewById(R.id.studentNameTxt);
        studentEmailInSideBar = (TextView) findViewById(R.id.studentEmailTxt);
        list = (ListView) findViewById(R.id.coursesList);

        //Get shared preferences object
        MySharedPreferences mySharedPreferences = new MySharedPreferences(getApplicationContext());
        studentNameInBar.setText(mySharedPreferences.getMyName());
        studentEmailInSideBar.setText(mySharedPreferences.getMyEmail());

        //Get saved courses on device from SQLite
        mySqLite = new DB_SQLiteManipulation(getApplicationContext());
        myCourses = mySqLite.getAllUserCourses();

        //Set view with saved user courses
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, myCourses);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // ListView Clicked item index
                int itemPosition = position;
                // ListView Clicked item value
                String itemValue = (String) list.getItemAtPosition(position);
            }
        });
    }
}
