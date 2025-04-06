package entity.user;

import entity.project.Project;

import java.util.ArrayList;
import java.util.List;

public class Manager extends User {
    private List<Project> project;

    public Manager() {
        super();
        this.project = new ArrayList<>();
    }

    public List<Project> getProject() {
        return project;
    }

    public void setProject(List<Project> project) {
        this.project = project;
    }
}
