package entity.user;

import java.util.ArrayList;
import java.util.List;

public class Manager extends User {
    private List<String> project;

    public Manager() {
        super();
        this.project = new ArrayList<>();
    }

    public Manager(String userID, String name, String hashedPassword, int age, MaritalStatus maritalStatus) {
        super(userID, name, hashedPassword, age, maritalStatus);
        this.project = new ArrayList<>();
    }

    public List<String> getProject() {
        return project;
    }

    public void setProject(List<String> project) {
        this.project = project;
    }
}
