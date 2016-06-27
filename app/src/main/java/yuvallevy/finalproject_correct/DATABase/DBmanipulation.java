package yuvallevy.finalproject_correct.DATABase;

import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import yuvallevy.finalproject_correct.JavaObjects.Student;

public class DBmanipulation {

    private Connection connection;
    private Statement statement;
    private PreparedStatement pareStatement;
    private ResultSet res;
    private ArrayList<String> courseName = new ArrayList<>();
    private ArrayList<String> realm = new ArrayList<>();

    public DBmanipulation() {

    }

    public void setConnectionOn() {
        try {
            Class.forName("com.mysql.jdbc.Driver"); //10.0.2.2:3306 //192.168.43.70
            connection = DriverManager.getConnection("jdbc:mysql://10.0.2.2:3306/final_project?useUnicode=true&characterEncoding=UTF-8", "root", "");

        } catch (Exception error) {
            System.out.println("Error:" + error.getMessage());
        }
    }


    /**
     * Insert new record holds all the data of the student
     *
     * @param currentStudent The Student we want to add to database
     */
    public void insertNewStudent(Student currentStudent) {
        setConnectionOn();
        String SQL_INSERT = "INSERT INTO students" + " VALUES(?, ?, ? , ? , ?)";
        try {
            pareStatement = (PreparedStatement) connection.prepareStatement(SQL_INSERT);
            pareStatement.setString(1, currentStudent.getEmail());
            pareStatement.setString(2, currentStudent.getName());
            pareStatement.setInt(3, currentStudent.getPhoneNumber());
            pareStatement.setInt(4, currentStudent.getAge());
            pareStatement.setString(5, currentStudent.getRealm());

            pareStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pareStatement != null)
                    pareStatement.close();
                setConnectionOff();
            } catch (Exception e) {
            }
        }
    }


    public void updateInfo(String studentMail, String subject, String value) {
        switch (subject) {
            case "אי-מייל":
                subject = "email";
                break;
            case "שם מלא":
                subject = "name";
                break;
            case "מספר טלפון":
                subject = "phoneNumber";
                break;
            case "גיל":
                subject = "age";
                break;
            case "תחום לימודים":
                subject = "realm";
        }
        try {
            this.setConnectionOn();
            String currentQuery = "UPDATE students set " + subject + " = '" + value + "' where email = '" + studentMail + "'";

            statement = connection.createStatement();
            statement.executeUpdate(currentQuery);
            setConnectionOff();

        } catch (Exception e) {
            e.printStackTrace();
            setConnectionOff();
        }
    }

    public void insertNewSearchingRecord(String email, String semester, String course, String city) {
        setConnectionOn();
        String exist = null;
        //Query for checking if the current searching data exist already
        String checkIfExist = "SELECT email FROM searching " + "WHERE email = '" + email + "' AND course = " + "'" + course + "' AND semester = '" + semester + "' AND city = '" + city + "'";
        try {
            statement = connection.createStatement();
            res = statement.executeQuery(checkIfExist);
            while (res.next()) {
                exist = res.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Only if the new searching not exist - add the new searching
        if (exist == null) {
            String SQL_INSERT = "INSERT INTO searching" + " VALUES(?, ?, ? , ?)";
            System.out.println("SQL IS: " + SQL_INSERT);
            try {
                pareStatement = (PreparedStatement) connection.prepareStatement(SQL_INSERT);
                pareStatement.setString(1, email);
                pareStatement.setString(2, semester);
                pareStatement.setString(3, course);
                pareStatement.setString(4, city);

                pareStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (pareStatement != null)
                        pareStatement.close();
                    setConnectionOff();
                } catch (Exception e) {
                }
            }
        }
    }

    //find partners by the same semester, course, and city
    public ArrayList<Student> findPartnersFast(String email, String semester, String course, String city) {
        setConnectionOn();
        String currentQuery;

        //If the user is registered dont show himself in the search result
        if (email != null) {
            currentQuery = "SELECT distinct students.email, name , phoneNumber, age , realm\n" +
                    "FROM students , searching\n" +
                    "WHERE '" + email + "' " + " <> students.email AND students.email in (SELECT email FROM searching " + "WHERE course = " + "'" + course + "' AND semester = '" + semester + "' AND city = '" + city + "')";
            System.out.println("ccc currentquery not in else: " + currentQuery);
        } else {
            currentQuery = "SELECT distinct students.email, name , phoneNumber, age , realm\n" +
                    "FROM students , searching\n" +
                    "WHERE students.email = students.email AND students.email in (SELECT email FROM searching " + "WHERE course = " + "'" + course + "' AND semester = '" + semester + "' AND city = '" + city + "')";
            System.out.println("ccc currentquery in else: " + currentQuery);
        }
        //Prepare Array for list of result
        ArrayList<Student> arrayOfResults = new ArrayList<>();
        try {
            statement = connection.createStatement();
            //Execute the query - and get result
            res = statement.executeQuery(currentQuery);

            //As long there are more results - add them to the list
            while (res.next()) {
                //Get student data of the current record result
                String curName = res.getString("name");
                int curAge = res.getInt("age");
                String curRealm = res.getString("realm");
                int curPhoneNumber = res.getInt("phoneNumber");
                String curEmail = res.getString("email");

                //Create Student with data and add it to the array
                Student curStudent = new Student();
                curStudent.setName(curName);
                curStudent.setAge(curAge);
                curStudent.setRealm(curRealm);
                curStudent.setPhoneNumber(curPhoneNumber);
                curStudent.setEmail(curEmail);

                //Add the student
                arrayOfResults.add(curStudent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (Exception e) {
            }
        }
        setConnectionOff();
        //Return the result - array list of student
        return arrayOfResults;
    }

    //Download the table of available courses from remote DB
    public void downloadCourses() {
        this.setConnectionOn();
        String currentQuery = "SELECT * from courses";
        try {
            statement = connection.createStatement();
            res = statement.executeQuery(currentQuery);

            while (res.next()) {
                String curName = res.getString("name");
                String curRealm = res.getString("realm");
                courseName.add(curName);
                realm.add(curRealm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
                this.setConnectionOff();
            } catch (Exception e) {
            }
        }
    }

    //Get the current version from remote DB
    public int getVersionNumber() {
        setConnectionOn();
        int currentVersion = 0;
        try {
            statement = connection.createStatement();
            res = statement.executeQuery("select * from version");
            while (res.next()) {
                currentVersion = res.getInt(1);
            }
        } catch (Exception e) {
        } finally {
            try {
                if (res != null) {
                }
            } catch (Exception e) {
            }
            return currentVersion;
        }
    }

    //Return the size of Course table - amount of courses in DB
    public int getCoursesTableSize() {
        return this.courseName.size();
    }

    //Return specific record from courses table as a string
    public String[] getRowFromCourses(int row) {
        String[] curRow = new String[2];
        curRow[0] = courseName.get(row).toString();
        curRow[1] = realm.get(row).toString();

        return curRow;
    }

    //If needed to delete student student - for future use
    public void deleteStudnt(Student currentStudent) {
        try {
            statement = connection.createStatement();
            String currentQuery = "DELETE FROM students " + "WHERE name = " + "'" + currentStudent.getName() + "'";
            System.out.println(currentQuery);
            statement.executeUpdate(currentQuery);
        } catch (Exception e) {
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException ignore) {
            }
        }
    }

    //Close the connection to the DB
    public void setConnectionOff() {
        if (connection != null)
            try {
                connection.close();
                System.out.println("connection close properly");
            } catch (SQLException ignore) {
            }
    }
}
