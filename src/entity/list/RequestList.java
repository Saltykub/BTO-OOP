package entity.list;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import entity.request.Request;
import utils.Converter;

/**
 * Manages a list of {@link Request} objects and their potential subclasses (e.g., Enquiry, BTOApplication),
 * handling data persistence and retrieval.
 * This class extends the generic {@link ModelList} to specialize in managing request data,
 * likely loaded from and saved to a CSV file specified by {@code FILE_PATH}.
 * It overrides the {@code load} method to handle the polymorphic nature of Request objects during deserialization.
 * Provides methods to access the list instance and retrieve requests by their ID.
 * Uses a static factory method {@code getInstance()} for convenient access.
 */
public class RequestList extends ModelList<Request> {

    /**
     * The default file path for storing and retrieving request data (CSV format).
     */
    private static final String FILE_PATH = "data_csv/RequestList.csv";

    /**
     * Constructs a RequestList instance associated with a specific file path.
     * Calls the superclass constructor to initialize the list, providing the file path
     * and the base {@code Request.class} type. Note that the overridden {@link #load} method
     * handles determining specific subclasses during loading.
     * Typically accessed via the static {@link #getInstance()} method using the default path.
     *
     * @param filePath The path to the CSV file used for data persistence.
     */
    public RequestList(String filePath) { // Changed parameter name for clarity
        super(filePath, Request.class); // Pass Request base class
    }

    /**
     * Provides a static factory method to get an instance of RequestList.
     * This method creates a new instance using the default {@code FILE_PATH}.
     * Note: This implementation creates a new instance on each call, potentially reloading data.
     * Consider implementing a true Singleton pattern if a single shared instance is desired.
     *
     * @return A new instance of {@code RequestList} initialized with the default file path.
     */
    public static RequestList getInstance() {
        return new RequestList(FILE_PATH);
    }

    /**
     * Gets the file path associated with this RequestList instance,
     * indicating where the request data is persisted.
     *
     * @return The file path string (e.g., "data_csv/RequestList.csv").
     */
    public String getFilePath() {
        return FILE_PATH;
    }

    /**
     * Retrieves a {@link Request} (or one of its subclasses) from the list based on its unique request ID.
     * Iterates through the list maintained by the superclass ({@link #getAll()})
     * and returns the first request matching the provided ID.
     *
     * @param requestID The unique ID of the request to find.
     * @return The {@link Request} object (or subclass instance) if found, otherwise {@code null}.
     */
    public Request getByID(String requestID) {
        for (Request request : this.getAll()) {
            if (request.getRequestID().equals(requestID)) {
                return request;
            }
        }
        return null;
    }

    /**
     * Overrides the load method to handle polymorphism within the Request hierarchy.
     * Reads data from the specified CSV file, determines the specific subclass of {@link Request}
     * for each line using {@link Converter#getRequestClass(String)}, deserializes the line
     * into an object of that specific subclass using {@link Converter#stringtoObj(String, Class)},
     * and adds the resulting object to the internal list using the superclass's add method.
     * Note: Calling {@code super.add(val)} might trigger the superclass's save mechanism
     * repeatedly during load, which could be inefficient. Consider loading all objects first,
     * then adding them to the internal list directly if performance is critical.
     *
     * @param FILE_PATH  The path to the file from which to load data.
     * @param hasHeader If true, the first line of the file is skipped as a header.
     */
    @Override
    public void load(String FILE_PATH, boolean hasHeader) {
        List<String> data = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
                if (hasHeader) {
                    br.readLine(); // Skip header
                }

                String line;
                while ((line = br.readLine()) != null) {
                    data.add(line);
                }

                for (String d : data) {
                    Class<? extends Request> clazzR = Converter.getRequestClass(d);
                    Request val = Converter.stringtoObj(d, clazzR);
                    super.add(val);
                }

            } catch (IOException e) {
                System.err.println("Error loading the CSV file: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Error creating the file: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

}
