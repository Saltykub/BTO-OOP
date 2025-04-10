package entity.list;

import entity.project.Project;


public class ProjectList extends ModelList<Project> {
    private static String filePath;

    public ProjectList(String filePath) {
        super(filePath,Project.class);
    }

    public static ProjectList getInstance() {
        return new ProjectList(filePath);
    }

    public String getFilePath() {
        return filePath;
    }

    public Project getByID(String ID) {
        for (Project project : this.getAll()) {
            if (project.getProjectID().equals(ID)) {
                return project;
            }
        }
        return null;
    }
}
