package yuvallevy.finalproject_correct.JavaObjects;

import java.io.Serializable;
/**
 * Class contain all the searching data created by users in  the application.
 * every record of searching contain the name of the desired course name , semester , city  and email.
 */

public class NewSearchingRecord implements Serializable {

    private String name;
    private String semester;
    private String city;
    private String studentEmail;

    public NewSearchingRecord(){
        name = null;
        semester = null;
        city = null;
        studentEmail = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getPlace() {
        return city;
    }

    public void setPlace(String place) {
        this.city = place;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String toString(){
        return "name: " + name + " semester: " + semester + " city: " + city + " Email: " + studentEmail;
    }

}