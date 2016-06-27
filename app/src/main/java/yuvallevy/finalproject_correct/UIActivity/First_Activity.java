package yuvallevy.finalproject_correct.UIActivity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import yuvallevy.finalproject_correct.R;

/**
 * Activity for unregister user.
 * Let him decide if he want to navigate to start register in normal way or just find partner fast
 */

public class First_Activity extends AppCompatActivity {

    private Button btnRegister;
    private Button btnFindPartner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity);

        btnRegister = (Button) findViewById(R.id.btnBuildProfile);
        btnFindPartner = (Button) findViewById(R.id.btnFindFast);

        // User pressed on find partner fast button
        btnFindPartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent findFast = new Intent("yuvallevy.finalproject_correct.SEMESTERACTIVITY");
                startActivity(findFast);
            }
        });

        // User pressed on create profile button
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent("yuvallevy.finalproject_correct.REGISTER");
                registerIntent.putExtra("CurrentStatus", "User_NotRegister");
                startActivity(registerIntent);
            }
        });


    }
}
