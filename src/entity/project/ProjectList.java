package entity.project;

import entity.user.ModelList;
import java.util.ArrayList;
import java.util.List;

public class ProjectList extends ModelList {
    private String filePath;
    private List<Project> projects;

    public ProjectList() {
        this.projects = new ArrayList<>();
    }

    public String getFilePath() {
        return filePath;
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
