package yuvallevy.finalproject_correct.UIActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import yuvallevy.finalproject_correct.R;
import yuvallevy.finalproject_correct.JavaObjects.Student;

/**
 * Activity for every time user click on some potential partner in order to contact him.
 * Activity represent to user the data of the partner by his name , his phone and his E-mail.
 */
public class MakeContact extends Activity {

    private Student partner;
    private Button btnBackToProfile;
    private TextView studentName;
    private TextView studentPhone;
    private TextView studentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_contact);

        // Get the data of the partner from earlier activity
        Intent i = getIntent();
        partner = (Student) i.getSerializableExtra("student");

        studentName = (TextView) findViewById(R.id.studentNameTxt);
        studentPhone = (TextView) findViewById(R.id.phoneNumber);
        studentEmail = (TextView) findViewById(R.id.mail);
        btnBackToProfile = (Button) findViewById(R.id.btnBackToProfile);

        //Present data of selected student to contact with
        studentName.setText(partner.getName());
        studentPhone.setText(String.valueOf(partner.getPhoneNumber()));
        studentEmail.setText(partner.getEmail());

        //After this page - go back to main profile page RegisteredUserProfile
        btnBackToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToProfile = new Intent("yuvallevy.finalproject_correct.REGISTEREDUSERPROFILE");
                startActivity(backToProfile);
            }
        });
    }
}