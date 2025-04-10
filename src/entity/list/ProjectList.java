package entity.list;

import entity.project.Project;


public class ProjectList extends ModelList<Project> {
    private static String filePath;

    public ProjectList(String filePath) {
        super(filePath);
    }

    public static ProjectList getInstance() {
        return new ProjectList(filePath);
    }

    public String getFilePath() {
        return filePath;
    }

    public Project getByID(String id) {
        for (Project project : this.getAll()) {
            if (project.getProjectID().equals(id)) {
                return project;
            }
        }
        return null;
    }
}
