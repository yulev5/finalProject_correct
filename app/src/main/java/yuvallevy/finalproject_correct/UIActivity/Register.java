package yuvallevy.finalproject_correct.UIActivity;

import android.app.Activity;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import yuvallevy.finalproject_correct.DATABase.DB_SQLiteManipulation;
import yuvallevy.finalproject_correct.DATABase.DBmanipulation;
import yuvallevy.finalproject_correct.DATABase.MySharedPreferences;
import yuvallevy.finalproject_correct.JavaObjects.NewSearchingRecord;
import yuvallevy.finalproject_correct.R;
import yuvallevy.finalproject_correct.JavaObjects.Student;


public class Register extends Activity {

    private Student studentForReg;
    private EditText studentEmail;
    private EditText studentName;
    private EditText phoneNumber;
    private EditText age;
    private TextView firstTitleText;
    private TextView secondTitleText;
    private Spinner realm;
    private Button btnSignIn;
    private LayoutInflater layoutInflater;
    private View popupView;
    private PopupWindow popupWindow;
    private Button btnProceed;
    private View parent;
    private Boolean cameFromSearchResult = false;      //
    private Boolean addToMyCourses = false;            //


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        parent = this.findViewById(android.R.id.content);

        firstTitleText = (TextView) findViewById(R.id.firstTitleText);
        secondTitleText = (TextView) findViewById(R.id.secondTitleText);
        studentEmail = (EditText) findViewById(R.id.txtEmail);
        studentName = (EditText) findViewById(R.id.txtFullName);
        phoneNumber = (EditText) findViewById(R.id.txtPhoneNumber);
        age = (EditText) findViewById(R.id.txtAge);
        realm = (Spinner) findViewById(R.id.spRealm);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        String callerStatus = getIntent().getStringExtra("CurrentStatus");
        //Set the text title according to the activity the user arrived from
        if (callerStatus.equals("FirstUser_NotRegister")) {
            //You are the first - Enter your details
            firstTitleText.setText("תלמיד חרוץ! אתה הראשון");
            secondTitleText.setText("אנא מלא את הפרטים הבאים:\nואנו נודיע לך ברגע שנמצא עבורך סטודנט מתאים ללמוד איתו");
            addToMyCourses = true;
        } else if (callerStatus.equals("Found_Match_NotRegister")) {
            //Matches found - enter your details to contact
            firstTitleText.setText("נמצאו סטודנטים מתאימים!");
            secondTitleText.setText("אנא מלא את הפרטים הבאים כדי ליצור קשר עם הפרטנר שבחרת");
            cameFromSearchResult = true;
            addToMyCourses = true;
        } else { //Create new profile
            firstTitleText.setText("צור את פרופיל הסטודנט שלך");
            secondTitleText.setText("אנא מלא את הפרטים הבאים כדי למצוא שותפים נהדרים ללמוד איתם");
        }

        //Choose realm spinner adapter and view set
        ArrayAdapter<CharSequence> adapterForDeg = ArrayAdapter.createFromResource(this, R.array.spinnerarrayForDeg, android.R.layout.simple_spinner_item);
        adapterForDeg.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        realm.setAdapter(adapterForDeg);

        //Popup settings to be ready before display
        layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = layoutInflater.inflate(R.layout.register_popup, null);
        popupWindow = new PopupWindow(popupView, AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT);
        btnProceed = (Button) popupView.findViewById(R.id.goToProfile);

        //Register button listener
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create new Student object with all data
                studentForReg = new Student();
                studentForReg.setEmail(studentEmail.getText().toString());
                studentForReg.setName(studentName.getText().toString());
                studentForReg.setPhoneNumber(Integer.valueOf(phoneNumber.getText().toString()));
                studentForReg.setAge(Integer.valueOf(age.getText().toString()));
                studentForReg.setRealm(realm.getSelectedItem().toString());

                //Start Async task to register in remote DB 
                DBExcute dbEx = new DBExcute();
                dbEx.execute();
            }
        });

        //popup button listener - continue button
        btnProceed.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextActivity;
                if (cameFromSearchResult) {
                    nextActivity = new Intent("yuvallevy.finalproject_correct.MAKECONTACT");
                    Student studentToPass = (Student) getIntent().getSerializableExtra("student");
                    nextActivity.putExtra("student", studentToPass);
                } else {
                    nextActivity = new Intent("yuvallevy.finalproject_correct.REGISTEREDUSERPROFILE");
                }
                startActivity(nextActivity);
            }
        });
    }

    class DBExcute extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            DBmanipulation myDB = new DBmanipulation();
            myDB.insertNewStudent(studentForReg);

            //Store personal details on device shared preferences
            MySharedPreferences mySharedPreferences = new MySharedPreferences(getApplicationContext());
            mySharedPreferences.storePersonalDetails(studentForReg);
            mySharedPreferences.changeRegisterUserStatus(true);

            //check if new searching record exist add data to myCourse list in sharedPreferences
            if (addToMyCourses) {
                //Get new searching record details
                NewSearchingRecord newSearchingRecord = (NewSearchingRecord) getIntent().getSerializableExtra("searchingRecord");
                //Add new course details to sqlite db
                DB_SQLiteManipulation SQLiteDB = new DB_SQLiteManipulation(getApplicationContext());
                SQLiteDB.addCourseToMyCourses(newSearchingRecord);
                //add new searching record to server database
                myDB.insertNewSearchingRecord(mySharedPreferences.getMyEmail(), newSearchingRecord.getSemester(), newSearchingRecord.getName(), newSearchingRecord.getPlace());
            }
            return null;
        }

        //Make popup visible (popup proceed button listener is up here in the code)
        @Override
        protected void onPostExecute(Void curVoid) {
            super.onPostExecute(curVoid);
            popupWindow.showAtLocation(parent, Gravity.CENTER, 50, -30);
        }
    }
}

