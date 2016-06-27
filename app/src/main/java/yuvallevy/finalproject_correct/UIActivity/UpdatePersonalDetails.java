package yuvallevy.finalproject_correct.UIActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import yuvallevy.finalproject_correct.DATABase.DBmanipulation;
import yuvallevy.finalproject_correct.DATABase.MySharedPreferences;
import yuvallevy.finalproject_correct.R;
import yuvallevy.finalproject_correct.JavaObjects.Student;

public class UpdatePersonalDetails extends Activity {
    private Button update;
    private Spinner updateSubject;
    private EditText value;
    private ProgressBar progressBar;
    private Student currentStudent;
    private TextView viewCurrSelectedDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_personal_details);

        //initialize all components
        update = (Button) findViewById(R.id.btnUpdate);
        updateSubject = (Spinner) findViewById(R.id.spinnerForUpdate);
        value = (EditText) findViewById(R.id.txtToEdit);
        viewCurrSelectedDetail = (TextView) findViewById(R.id.viewCurrSelectedDetail);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        //get the student data from shared preferences
        MySharedPreferences mySharedPreferences = new MySharedPreferences(getApplicationContext());
        currentStudent = mySharedPreferences.getPersonalSavedDetails();

        //initialize spinner
        ArrayAdapter<CharSequence> adapterForSubject = ArrayAdapter.createFromResource(this, R.array.spinnerarrayForUpdate, android.R.layout.simple_spinner_item);
        adapterForSubject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        updateSubject.setAdapter(adapterForSubject);

        //Listener for spinner
        updateSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Get shared preferences updated object
                MySharedPreferences mySharedPreferences = new MySharedPreferences(getApplicationContext());
                //Get value from spinner
                String selectedDetailToCheck = updateSubject.getSelectedItem().toString();

                //Set the text view with current saved value on device - before update
                TextView temp = (TextView) findViewById(R.id.viewCurrSelectedDetail);
                switch (selectedDetailToCheck) {
                    case "אי-מייל":
                        String email = mySharedPreferences.getMyEmail();
                        temp.setText(email);
                        break;
                    case "שם מלא":
                        String fullName = mySharedPreferences.getMyName();
                        temp.setText(fullName);
                        break;
                    case "מספר טלפון":
                        int phone = mySharedPreferences.getPhoneNumber();
                        temp.setText(String.valueOf(phone));
                        break;
                    case "גיל":
                        int age = mySharedPreferences.getMyAge();
                        temp.setText(String.valueOf(age));
                        break;
                    case "תחום לימודים":
                        String realm = mySharedPreferences.getMyRealm();
                        temp.setText(realm);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //On click on update button
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show progress abr
                progressBar.setVisibility(View.VISIBLE);

                //execute Async task
                //Get selected value to update from spinner
                String spinnerItemSelected = updateSubject.getSelectedItem().toString();
                String newValue = value.getText().toString();
                //Start Async Task
                DBExcuteForUpdate remoteDB = new DBExcuteForUpdate();
                remoteDB.execute(spinnerItemSelected, newValue);
            }
        });
    }


    class DBExcuteForUpdate extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... params) {
            String selectedToUpdate = params[0];
            String newValue = params[1];

            //Update info in remote DB
            DBmanipulation DB = new DBmanipulation();
            DB.updateInfo(currentStudent.getEmail(), selectedToUpdate, newValue);

            //Update info in local saved shared preferences
            MySharedPreferences mySharedPreferences = new MySharedPreferences(getApplicationContext());
            mySharedPreferences.setNewValue(selectedToUpdate, newValue);
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            //After the update finish - go back to main screen
            Intent backToProfile = new Intent("yuvallevy.finalproject_correct.REGISTEREDUSERPROFILE");
            backToProfile.putExtra("buildProfile", currentStudent);
            startActivity(backToProfile);
        }
    }
}