package yuvallevy.finalproject_correct.JavaObjects;

import java.io.Serializable;

/**
 * Class contain the data for every student that registered in
 * *the remote DB. holding his email , name , phoneNumber , age , *realm
 */
public class Student implements Serializable {

    private String email;
    private String name;
    private int phoneNumber;
    private int age;
    private String realm;

    public Student() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String toString() {
        return email + " , " + name + " , " + phoneNumber + " , " + age + " , " + realm;
    }
}
