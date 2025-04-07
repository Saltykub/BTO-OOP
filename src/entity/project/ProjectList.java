package entity.project;

import java.util.ArrayList;
import java.util.List;

public class ProjectList {
    private String filePath;
    private List<Project> projects;

    public ProjectList() {
        this.filePath = "ProjectList.txt";
        this.projects = new ArrayList<>();
    }

    public String getFilePath() {
        return filePath;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public Project getByID(String id) {
        for (Project project : projects) {
            if (project.getProjectID().equals(id)) {
                return project;
            }
        }
        return null;
    }
}
