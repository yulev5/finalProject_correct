package yuvallevy.finalproject_correct.UIActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import yuvallevy.finalproject_correct.DATABase.MySharedPreferences;
import yuvallevy.finalproject_correct.R;

//Home screen for registered users
public class RegisteredUserProfile extends Activity {
    private TextView studentNameInBar;
    private TextView studentEmailInSideBar;
    private Button btnAddCourse;
    private Button btnEditInfo;
    private Button btnMyCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        System.out.println("aaa in onCreate");

        studentNameInBar = (TextView) findViewById(R.id.studentNameTxt);
        studentEmailInSideBar = (TextView) findViewById(R.id.studentEmailTxt);
        btnAddCourse = (Button) findViewById(R.id.btnAddCourse);
        btnEditInfo = (Button) findViewById(R.id.btnEditInfo);
        btnMyCourses = (Button) findViewById(R.id.btnMyCourses);

        //Get registered user name and Email from shared preferences - for displaying on screen
        MySharedPreferences mySharedPreferences = new MySharedPreferences(getApplicationContext());
        studentNameInBar.setText(mySharedPreferences.getMyName());
        studentEmailInSideBar.setText(mySharedPreferences.getMyEmail());

        //Add course button listener
        btnAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addCourse = new Intent("yuvallevy.finalproject_correct.SEMESTERACTIVITY");
                startActivity(addCourse);
            }
        });

        //start show my courses List activity
        btnMyCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myCourses = new Intent("yuvallevy.finalproject_correct.MYCOURSESLIST");
                startActivity(myCourses);
            }
        });

        //Edit user saved info
        btnEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myCourses = new Intent("yuvallevy.finalproject_correct.UPDATEPERSONALDETAILS");
                startActivity(myCourses);
            }
        });
    }

    @Override
    protected void onDestroy() {
        System.out.println("aaa in onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        System.out.println("aaa in onStop");
        super.onStop();
    }

    @Override
    protected void onPause() {
        System.out.println("aaa in onPause");
        super.onPause();
    }

}