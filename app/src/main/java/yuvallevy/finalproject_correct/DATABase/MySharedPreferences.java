package yuvallevy.finalproject_correct.DATABase;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import yuvallevy.finalproject_correct.JavaObjects.Student;

public class MySharedPreferences {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private final String PREF_NAME = "sharedPreferences";
    private final String KEY_IS_REGISTERED = "isRegistered";
    private final String KEY_EMAIL = "email";
    private final String KEY_NAME = "name";
    private final String KEY_PHONE_NUMBER = "phoneNumber";
    private final String KEY_AGE = "age";
    private final String KEY_REALM = "realm";

    public MySharedPreferences(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    //Store personal user details in shared preferences
    public void storePersonalDetails(Student student) {
        editor = sharedPreferences.edit();
        editor.putString(KEY_EMAIL, student.getEmail());
        editor.putString(KEY_NAME, student.getName());
        editor.putInt(KEY_PHONE_NUMBER, student.getPhoneNumber());
        editor.putInt(KEY_AGE, student.getAge());
        editor.putString(KEY_REALM, student.getRealm());
        editor.apply();
    }

    //Change user status - after user registration - change status to isRegistered = true
    public void changeRegisterUserStatus(boolean status) {
        editor.putBoolean(KEY_IS_REGISTERED, status);
        editor.apply();
    }

    public void setNewValue(String whatToUpdate, String newValue) {
        switch (whatToUpdate) {
            case "אי-מייל":
                changeRegisterUserEmail(newValue);
                break;
            case "שם מלא":
                changeRegisterUserName(newValue);
                break;
            case "מספר טלפון":
                changeRegisterUserPhoneNumber(Integer.parseInt(newValue));
                break;
            case "גיל":
                changeRegisterUserAge(Integer.parseInt(newValue));
                break;
            case "תחום לימודים":
                changeRegisterUserRealm(newValue);
        }
    }

    //Set user email
    private void changeRegisterUserEmail(String newEmail) {
        editor.putString(KEY_EMAIL, newEmail);
        editor.apply();
    }

    //Set user phone number
    private void changeRegisterUserName(String newUserName) {
        editor.putString(KEY_NAME, newUserName);
        editor.apply();
    }

    //Set user phone number
    private void changeRegisterUserPhoneNumber(int newPhoneNumber) {
        editor.putInt(KEY_PHONE_NUMBER, newPhoneNumber);
        editor.apply();
    }

    //Set user age
    private void changeRegisterUserAge(int newAge) {
        editor.putInt(KEY_AGE, newAge);
        editor.apply();
    }

    //Set user realm
    private void changeRegisterUserRealm(String newRealm) {
        editor.putString(KEY_REALM, newRealm);
        editor.apply();
    }

    //Get user name from saved shared preferences
    public String getMyName() {
        return sharedPreferences.getString(KEY_NAME, "");
    }

    //Get Email from saved shared preferences
    public String getMyEmail() {
        return sharedPreferences.getString(KEY_EMAIL, "");
    }

    //Get user Age from saved shared preferences
    public int getMyAge() {
        return sharedPreferences.getInt(KEY_AGE, 0);
    }

    //Get user Realm from saved shared preferences
    public String getMyRealm() {
        return sharedPreferences.getString(KEY_REALM, "");
    }

    //Get user Phone number from saved shared preferences
    public int getPhoneNumber() {
        return sharedPreferences.getInt(KEY_PHONE_NUMBER, 0);
    }

    //Check in shared preferences if the user is registered or not
    public boolean getIsRegistered() {
        if (sharedPreferences.contains(KEY_IS_REGISTERED)) {
            //shared preferences exist
            return sharedPreferences.getBoolean(KEY_IS_REGISTERED, false);
        }
        //First time - shared preferences not exist
        //Add shared preferences and return false - user not registered
        editor.putBoolean(KEY_IS_REGISTERED, false);
        editor.apply();
        return false;
    }

    //Return personal details saved on device shared preferences
    //Returning Student object with all saved details
    public Student getPersonalSavedDetails() {
        Student student = new Student();
        student.setEmail(sharedPreferences.getString(KEY_EMAIL, ""));
        student.setName(sharedPreferences.getString(KEY_NAME, ""));
        student.setPhoneNumber(sharedPreferences.getInt(KEY_PHONE_NUMBER, 0));
        student.setAge(sharedPreferences.getInt(KEY_AGE, 0));
        student.setRealm(sharedPreferences.getString(KEY_REALM, ""));

        return student;
    }
}
