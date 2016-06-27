package yuvallevy.finalproject_correct.UIActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

import yuvallevy.finalproject_correct.DATABase.MySharedPreferences;
import yuvallevy.finalproject_correct.JavaObjects.NewSearchingRecord;
import yuvallevy.finalproject_correct.R;
import yuvallevy.finalproject_correct.JavaObjects.Student;

//Activity for showing the result list for last search
public class SearchResult extends Activity {

    final Context context = this;
    private ArrayList<Student> studentFound;
    private ListView list;
    private MySharedPreferences mySharedPreferences;
    private boolean isRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_layout);

        //Get value if user is registered or not
        mySharedPreferences = new MySharedPreferences(getApplicationContext());
        isRegistered = mySharedPreferences.getIsRegistered();

        //Get from extra to array the students found list
        getStudentFound();

        //Put the result searching students in list view
        populateListView();
    }

    //Get from extra to array the students found list
    private void getStudentFound() {
        Intent i = getIntent();
        studentFound = (ArrayList<Student>) i.getSerializableExtra("studentsFound");
    }

    //Put the result searching students in list view
    private void populateListView() {
        ArrayAdapter<Student> adapter = new MyListAdapter();
        list = (ListView) findViewById(R.id.studentListView);
        list.setAdapter(adapter);

        //Listener for clicking one of the result
        //Different listener for register or unregister users
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Student studentClicked = studentFound.get(position);

                if (!isRegistered) {
                    //Listener for unregistered users
                    final Intent registerIntent = new Intent("yuvallevy.finalproject_correct.REGISTER");
                    Intent i = getIntent();

                    //Get searching data until now
                    NewSearchingRecord curSearchRecord = (NewSearchingRecord) i.getSerializableExtra("searchingRecord");
                    registerIntent.putExtra("searchingRecord", curSearchRecord);
                    registerIntent.putExtra("CurrentStatus", "Found_Match_NotRegister");
                    registerIntent.putExtra("student", studentClicked);

                    new AlertDialog.Builder(context)
                            .setTitle("מצאת שותף")
                            .setMessage(getString(R.string.NotFirstNotRegiseterd) + "  " + studentClicked.getName())
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(registerIntent);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.gallery_thumb)
                            .show();
                }
                //Listener for registered users
                else {
                    Intent makeContact = new Intent("yuvallevy.finalproject_correct.MAKECONTACT");
                    makeContact.putExtra("student", studentClicked);
                    startActivity(makeContact);
                }
            }
        });
    }

    //Adapter for list view
    private class MyListAdapter extends ArrayAdapter<Student> {
        public MyListAdapter() {
            super(SearchResult.this, R.layout.item_view, studentFound);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);
            }

            Student currentStudent = studentFound.get(position);

            // Name:
            TextView studentName = (TextView) itemView.findViewById(R.id.item_txtName);
            studentName.setText(currentStudent.getName());

            // Age:
            TextView studentAge = (TextView) itemView.findViewById(R.id.item_txtAge);
            studentAge.setText("" + currentStudent.getAge());

            // Realm:
            TextView studentRealm = (TextView) itemView.findViewById(R.id.item_txtRealm);
            studentRealm.setText(currentStudent.getRealm());

            return itemView;
        }
    }
}