package entity.list;

import entity.project.Project;

/**
 * Manages a list of {@link Project} objects, handling data persistence and retrieval.
 * This class extends the generic {@link ModelList} to specialize in managing project data,
 * likely loaded from and saved to a CSV file specified by the internal {@code FILE_PATH}.
 * It provides methods to access the list instance and retrieve projects by their ID.
 * Uses a static factory method {@code getInstance()} for convenient access.
 */
public class ProjectList extends ModelList<Project> {

    /**
     * The file path for storing and retrieving project data (CSV format).
     */
    private static final String FILE_PATH = "data_csv/ProjectList.csv";

    /**
     * Constructs a ProjectList instance.
     * Calls the superclass constructor using the default {@code FILE_PATH}
     * and passing the {@code Project.class} type for CSV data mapping.
     * Typically accessed via the static {@link #getInstance()} method.
     */
    public ProjectList() {
        super(FILE_PATH,Project.class);
    }

    /**
     * Provides a static factory method to get an instance of ProjectList.
     * This method creates a new instance using the default {@code FILE_PATH}.
     * Note: This implementation creates a new instance on each call, potentially reloading data.
     * Consider implementing a true Singleton pattern if a single shared instance is desired.
     *
     * @return A new instance of {@code ProjectList} initialized with the default file path.
     */
    public static ProjectList getInstance() {
        return new ProjectList();
    }

    public String getFilePath() {
        return FILE_PATH;
    }

    /**
     * Retrieves a {@link Project} from the list based on its unique project ID.
     * Iterates through the list maintained by the superclass ({@link #getAll()})
     * and returns the first project matching the provided ID.
     *
     * @param ID The project ID (e.g., "PRJ001") of the project to find.
     * @return The {@link Project} object if found, otherwise {@code null}.
     */
    public Project getByID(String ID) {
        for (Project project : this.getAll()) {
            if (project.getProjectID().equals(ID)) {
                return project;
            }
        }
        return null;
    }
}
