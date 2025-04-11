package entity.list;

import entity.project.Project;


public class ProjectList extends ModelList<Project> {
    private static final String filePath = "data_csv\\ProjectList.csv";

    public ProjectList() {
        super(filePath,Project.class);
    }

    public static ProjectList getInstance() {
        return new ProjectList();
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
