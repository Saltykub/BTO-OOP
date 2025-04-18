package entity.list;

import entity.project.Project;


public class ProjectList extends ModelList<Project> {
    private static final String FILE_PATH = "data_csv/ProjectList.csv";

    public ProjectList() {
        super(FILE_PATH,Project.class);
    }

    public static ProjectList getInstance() {
        return new ProjectList();
    }

    public String getFilePath() {
        return FILE_PATH;
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
