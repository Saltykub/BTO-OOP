package entity.list;

import entity.user.Officer;

/**
 * Manages a list of {@link Officer} objects, handling data persistence and retrieval.
 * This class extends the generic {@link ModelList} to specialize in managing officer data,
 * likely loaded from and saved to a CSV file specified by {@code FILE_PATH}.
 * It provides methods to access the list instance and retrieve officers by their ID.
 * Uses a static factory method {@code getInstance()} for convenient access.
 */
public class OfficerList extends ModelList<Officer> {

    /**
     * The default file path for storing and retrieving officer data (CSV format).
     */
    private static final String FILE_PATH = "data_csv/OfficerList.csv";

    /**
     * Constructs an OfficerList instance associated with a specific file path.
     * Calls the superclass constructor to initialize the list, providing the file path
     * and the {@code Officer.class} type for CSV data mapping.
     * Typically accessed via the static {@link #getInstance()} method using the default path.
     *
     * @param filePath The path to the CSV file used for data persistence.
     */
    public OfficerList(String filePath) { // Changed parameter name for clarity
        super(filePath, Officer.class); // Pass Officer class for reflection/CSV handling
    }
    /**
     * Provides a static factory method to get an instance of OfficerList.
     * This method creates a new instance using the default {@code FILE_PATH}.
     * Note: This implementation creates a new instance on each call, potentially reloading data.
     * Consider implementing a true Singleton pattern if a single shared instance is desired.
     *
     * @return A new instance of {@code OfficerList} initialized with the default file path.
     */
  public static OfficerList getInstance() {
    return new OfficerList(FILE_PATH);
  }
    /**
     * Gets the file path associated with this OfficerList instance,
     * indicating where the officer data is persisted.
     *
     * @return The file path string (e.g., "data_csv/OfficerList.csv").
     */
  public String getFilePath() {
    return FILE_PATH;
  }
    /**
     * Retrieves an {@link Officer} from the list based on their unique user ID.
     * Iterates through the list maintained by the superclass ({@link #getAll()})
     * and returns the first officer matching the provided ID.
     *
     * @param ID The user ID of the officer to find.
     * @return The {@link Officer} object if found, otherwise {@code null}.
     */
  public Officer getByID(String ID) {
      for (Officer officer : this.getAll()) {
          if (officer.getUserID().equals(ID)) {
              return officer;
          }
      }
      return null;
  }
}
