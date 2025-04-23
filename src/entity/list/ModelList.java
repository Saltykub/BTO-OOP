package entity.list;

import java.util.List;

import utils.Converter;

import java.util.ArrayList;
import java.io.*;

/**
 * An abstract generic base class for managing lists of model objects of type {@code T}.
 * Provides common functionality including an internal list storage, basic CRUD operations
 * (add, update, delete, get all), size management, and persistence mechanisms
 * for loading from and saving to a file (presumably CSV format, utilizing a {@link Converter} utility).
 * This class implements the {@link Saveable} interface.
 * <p>
 * Subclasses must implement {@link #getFilePath()} to define the data storage location
 * and {@link #getByID(String)} to provide type-specific retrieval by a unique ID.
 *
 * @param <T> The type of the model objects managed by this list.
 */
public abstract class ModelList<T> implements Saveable {
    private Class<T> clazz;
    private List<T> list;

    // --- Constructors ---

    /**
     * Initializes the ModelList.
     * Creates an empty internal list, stores the class type {@code T} for potential reflection use,
     * and immediately attempts to load existing data from the specified file path.
     *
     * @param filePath The path to the file used for loading and saving the list data.
     * @param clazz    The {@code Class} object corresponding to the generic type {@code T},
     * required for type-specific operations like CSV conversion.
     */
    public ModelList(String filePath, Class<T> clazz) {
        this.clazz = clazz;
        this.list = new ArrayList<>();
        this.load(filePath, true);
    }

    // --- Abstract methods ---

    /**
     * Gets the specific file path used by the concrete subclass for data persistence.
     * Subclasses must implement this method to define where their data is stored.
     *
     * @return The file path string for loading and saving data.
     */
    public abstract String getFilePath();

    /**
     * Retrieves an item of type {@code T} from the list based on its unique String identifier.
     * Subclasses must implement the specific logic for searching based on their ID structure.
     *
     * @param ID The unique identifier of the item to retrieve.
     * @return The item of type {@code T} if found, otherwise {@code null}.
     */
    public abstract T getByID(String ID);

    // --- Public Accessor and Mutator Methods ---

    /**
     * Returns a defensive copy of the internal list of items.
     * Modifications to the returned list will not affect the internal list managed by this class.
     *
     * @return A new {@link ArrayList} containing all items currently in the list.
     */
    public List<T> getAll() {
        return new ArrayList<>(list); // Return a copy to protect internal list
    }

    /**
     * Deletes an item from the list based on its ID.
     * Uses the subclass's {@link #getByID(String)} implementation to find the item.
     * If the item is found, it is removed from the list, and the list is saved to the file.
     *
     * @param ID The unique identifier of the item to delete.
     */
    public void delete(String ID) {
        T item = getByID(ID);
        if (item != null) {
            list.remove(item);
        }
        save(getFilePath());
    }

    /**
     * Updates an item in the list.
     * This implementation performs an update by first removing the old item identified by {@code ID}
     * and then adding the {@code newItem}. The list is saved after the operation.
     * Note: This ensures the item is replaced but might affect order if the list is ordered.
     *
     * @param ID      The unique identifier of the item to replace.
     * @param newItem The new item object to add in place of the old one.
     */
    public void update(String ID, T newItem) {
        delete(ID); // Remove old version
        add(newItem); // Add new version
        save(getFilePath());
    }

    /**
     * Replaces the entire contents of the current list with the provided list of new items.
     * The internal list is cleared, the new items are added, and the list is saved.
     *
     * @param newItems The list of items that will replace the current contents.
     */
    public void updateAll(List<T> newItems) {
        clear();
        list.addAll(newItems);
        save(getFilePath());
    }

    /**
     * Adds a single item to the end of the list and saves the list to the file.
     *
     * @param item The item of type {@code T} to add.
     */
    public void add(T item) {
        list.add(item);
        save(getFilePath());
    }

    /**
     * Returns the current number of items in the list.
     *
     * @return The size of the internal list.
     */
    public int size() {
        return list.size();
    }

    /**
     * Removes all items from the internal list.
     * Note: This method only clears the in-memory list; it does not automatically save the empty state to the file.
     * A subsequent save operation (e.g., via add, delete, update) is required to persist the cleared state.
     */
    public void clear() {
        list.clear();
    }

    // --- Persistence Methods ---

    /**
     * Loads list data from the specified file path.
     * If the file exists, it reads each line (optionally skipping a header),
     * converts the line to an object of type {@code T} using {@link Converter#stringtoObj(String, Class)},
     * and adds it to the internal list.
     * If the file does not exist, it attempts to create it.
     * Errors during file reading or creation are printed to standard error.
     *
     * @param filePath  The path to the file from which to load data.
     * @param hasHeader If true, the first line of the file is skipped as a header.
     */
    public void load(String filePath, boolean hasHeader){
        List<String> data = new ArrayList<>();
        File file = new File(filePath);
        // load data if exist
        if(file.exists()){
            try ( BufferedReader br = new BufferedReader(new FileReader(filePath))){
                if(hasHeader){
                    br.readLine();
                }
                String line;
                while((line = br.readLine()) != null){;
                    data.add(line);
                }
                for(String d: data){
                    T val = Converter.stringtoObj(d, clazz);
                    list.add(val);
                }
    
            } catch(IOException e){
                System.err.println("Error loading the CSV file: " + e.getMessage());
                e.printStackTrace();
            }
        }
        else {
            // create file if it doesn't exist
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Error creating the file: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Saves the current state of the internal list to the specified file path, overwriting existing content.
     * Implements the {@link Saveable#save(String)} method.
     * If the list is not empty, it generates a header line using {@link Converter#getField(Object)}
     * based on the first item, then writes the header followed by each item converted to a string
     * using {@link Converter#objToString(Object)}.
     *
     * @param filePath The path to the file where the data should be saved.
     * @throws RuntimeException if an {@link IOException} occurs during file writing.
     */
    public void save(String filePath) {
        try (PrintWriter printWriter = new PrintWriter(new FileWriter(filePath))) {
            if (list.isEmpty()) return;
            T val = list.get(0);
            String header = Converter.getField(val);
            printWriter.println(header);
            for(T item: list){
                String line = Converter.objToString(item);
                printWriter.println(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("Data could not be saved to file: " + filePath);
        }

    }
}
