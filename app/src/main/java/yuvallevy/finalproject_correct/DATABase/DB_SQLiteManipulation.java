package yuvallevy.finalproject_correct.DATABase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import yuvallevy.finalproject_correct.JavaObjects.NewSearchingRecord;

public class DB_SQLiteManipulation extends SQLiteOpenHelper {

    private static String DB_NAME = "SQLite_final_project.db";  // Database Name
    private static final int DB_VERSION = 1;                    // Database Version

    //MyDetails Table Parameters:
    private static final String TABLE_MY_DETAILS = "myDetails";      // My Details table name
    private static final String KEY_EMAIL = "email";
    private static final String KEY_MY_NAME = "name";
    private static final String KEY_PHONE_NUMBER = "phoneNumber";
    private static final String KEY_AGE = "age";
    private static final String KEY_MY_REALM = "realm";

    //Version Table Parameters:
    private static final String TABLE_VERSION = "version";           // Version table name
    private static final String KEY_VERSION = "version_number";

    //Courses Table Parameters names
    private static final String TABLE_COURSES = "courses";           // Courses table name
    private static final String TABLE_MY_COURSES = "myCourses";      // My Courses table name

    //Courses and MyCourses Table Parameters names
    private static final String KEY_NAME = "name";
    private static final String KEY_REALM = "realm";

    private SQLiteDatabase myDataBase;
    private final Context myContext;

    public DB_SQLiteManipulation(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create myDetails table
        String CREATE_MY_DETAILS_TABLE = "CREATE TABLE " + TABLE_MY_DETAILS + "("
                + KEY_EMAIL + " TEXT,"
                + KEY_MY_NAME + " TEXT,"
                + KEY_PHONE_NUMBER + " TEXT,"
                + KEY_AGE + " INTEGER,"
                + KEY_MY_REALM + " TEXT" + ")";
        db.execSQL(CREATE_MY_DETAILS_TABLE);

        //Create courses table
        String CREATE_COURSES_TABLE = "CREATE TABLE " + TABLE_COURSES + "("
                + KEY_NAME + " TEXT,"
                + KEY_REALM + " TEXT" + ")";
        db.execSQL(CREATE_COURSES_TABLE);

        //Create myCourses Table
        String CREATE_MY_COURSES_TABLE = "CREATE TABLE " + TABLE_MY_COURSES + "("
                + KEY_NAME + " TEXT" + ")";
        db.execSQL(CREATE_MY_COURSES_TABLE);

        //Create Version Table
        String CREATE_VERSION_TABLE = "CREATE TABLE " + TABLE_VERSION + "("
                + KEY_VERSION + " INTEGER" + ")";
        db.execSQL(CREATE_VERSION_TABLE);

        System.out.println();
        //save Database object reference
        this.myDataBase = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXIST " + TABLE_COURSES);
        db.execSQL("DROP TABLE IF EXIST " + TABLE_VERSION);

        //Create new tables
        onCreate(db);
    }


    //Download generic table to device SQLite database
    public void downloadTable(String tableName, DBmanipulation currentDB) {
        //Check if download is necessary or not
        boolean toDownload = checkVersion(currentDB);

        //toDownload = true
        //If needed to version is not updated and needed to download data
        if (toDownload) {
            if (tableName.equals("courses")) {              //Download courses table
                downloadCoursesTable(currentDB);
            }

            //Set Version number to be the latest version
            String query = "DELETE FROM " + TABLE_VERSION;
            getReadableDatabase().execSQL(query);

            int latestVersion = currentDB.getVersionNumber();
            query = "INSERT INTO " + TABLE_VERSION
                    + " ('version_number') VALUES ("
                    + latestVersion + ")";
            System.out.println("query is:" + query);
            getReadableDatabase().execSQL(query);
        } else {
        }

    }

    //Return true - if Download is needed
    //Return false - if download in unnecessary
    private boolean checkVersion(DBmanipulation currentDB) {
        //Get version table from SQLite DATABASE
        String[] column = new String[]{KEY_VERSION};
        Cursor c = getReadableDatabase().query(TABLE_VERSION, column, null, null, null, null, null);
        int iVersion = c.getColumnIndex(KEY_VERSION);

        //Default Returning value
        boolean newerVersionExist = false;

        if (c.moveToFirst()) {
            int SQLiteVersion = c.getInt(iVersion);             //Version value in device
            int remoteVersion = currentDB.getVersionNumber();   //Version value in remote database

            if (remoteVersion > SQLiteVersion) {
                newerVersionExist = true;
            }
            //Close SQLite connection
            c.close();
            return newerVersionExist;
        }

        newerVersionExist = true;
        return newerVersionExist;
    }

    //Private Method - Download courses table to device SQLite database
    private void downloadCoursesTable(DBmanipulation db) {
        //Empty courses table
        String query = "DELETE FROM " + TABLE_COURSES;
        getReadableDatabase().execSQL(query);

        //Download courses table from server
        db.downloadCourses();

        //get size of table - number of rows
        int size = db.getCoursesTableSize();

        for (int i = 0; i < size; i++) {
            String[] curRow;
            curRow = db.getRowFromCourses(i);
            //Add the current course row to courses table
            addCourseByRow(curRow, TABLE_COURSES);
        }
    }

    //Add new course to My Courses table - user active courses table
    public void addCourseToMyCourses(NewSearchingRecord searchingRecord) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, searchingRecord.getName());

        //insert row
        db.insert(TABLE_MY_COURSES, null, values);
        db.close();
    }


    public void addCourseByRow(String[] curRow, String tableName) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, curRow[0]);
        values.put(KEY_REALM, curRow[1]);

        //insert row
        db.insert(tableName, null, values);
        db.close();
    }


    //SQL query - get specific realm
    //return ArrayList with all courses in that realm
    public ArrayList getCoursesByDegree(String degree) {
        String[] columns = new String[]{KEY_NAME};
        String whereClause = KEY_REALM + " = '" + degree + "'";

        Cursor c = getReadableDatabase().query(TABLE_COURSES, columns, whereClause, null, null, null, null);

        int iName = c.getColumnIndex(KEY_NAME);

        ArrayList<String> result = new ArrayList<>();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            result.add(c.getString(iName));
        }

        c.close();
        return result;
    }


    public ArrayList<String> getAllUserCourses() {
        String[] columns = new String[]{KEY_NAME};
        Cursor c = getReadableDatabase().query(TABLE_MY_COURSES, columns, null, null, null, null, null);

        int iName = c.getColumnIndex(KEY_NAME);

        ArrayList<String> result = new ArrayList<>();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String str = c.getString(iName) + "\n";
            result.add(str);
        }

        c.close();
        return result;
    }

    //For Debug - print all courses saved on SQLite
    public void printAllCourses() {
        String[] columns = new String[]{KEY_NAME, KEY_REALM};
        Cursor c = getReadableDatabase().query(TABLE_COURSES, columns, null, null, null, null, null);
        System.out.println("Cursor is: " + c.toString());

        int iName = c.getColumnIndex(KEY_NAME);
        int iRealm = c.getColumnIndex(KEY_REALM);

        String result = "";
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            result = result + c.getString(iName) + " " + c.getString(iRealm) + "\n";
        }
        c.close();
    }


    //In case we need to delete SQLite database from device
    public void deleteDB() {
        myContext.deleteDatabase(DB_NAME);
    }

    //Add personal details to MY_DETAILS table
    public void addPersonalDetails(String email, String name, String phoneNumber, int age, String realm) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EMAIL, email);
        values.put(KEY_MY_NAME, name);
        values.put(KEY_PHONE_NUMBER, phoneNumber);
        values.put(KEY_AGE, age);
        values.put(KEY_MY_REALM, realm);

        //insert row
        db.insert(TABLE_MY_DETAILS, null, values);
        db.close();
    }
}
