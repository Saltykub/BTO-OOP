package entity.user;

import java.util.ArrayList;
import java.util.List;

public class Manager implements User {
    private String managerID;
    private String name;
    private String hashedPassword;
    private int age;
    private MaritalStatus maritalStatus;
    private List<String> project;

    public Manager() {
        super();
        this.project = new ArrayList<>();
    }

    public Manager(String userID, String name, String hashedPassword, int age, MaritalStatus maritalStatus) {
        this.managerID = userID;
        this.name = name;
        this.hashedPassword = hashedPassword;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.project = new ArrayList<>();
    }

    public String getUserID() {
        return managerID;
    }

    public void setUserID(String userID) {
        this.managerID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassoword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public List<String> getProject() {
        return project;
    }

    public void setProject(List<String> project) {
        this.project = project;
    }
}
