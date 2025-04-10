package entity.list;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.*;

public abstract class ModelList<T> {
    private List<T> list;

    // Constructors
    public ModelList() {
        this.list = new ArrayList<>();
    }

    public ModelList(String filePath) {
        this();
        this.load(filePath);
    }

    public abstract String getFilePath();

    // Public methods
    public T getById(String ID) {
        // Implementation depends on how T identifies itself (e.g., via an interface
        // like Identifiable)
        // This is a placeholder - you'll need to adapt it to your specific model class
        for (T item : list) {
            if (item.toString().contains(ID)) { // Simple example
                return item;
            }
        }
        return null;
    }

    public List<T> getAll() {
        return new ArrayList<>(list); // Return a copy to protect internal list
    }

    public void delete(String ID) {
        T item = getById(ID);
        if (item != null) {
            list.remove(item);
        }
        save(getFilePath());
    }

    public void update(String ID, T newItem) {
        delete(ID); // Remove old version
        add(newItem); // Add new version
        save(getFilePath());
    }

    public void updateAll(List<T> newItems) {
        clear();
        list.addAll(newItems);
        save(getFilePath());
    }

    public void add(T item) {
        list.add(item);
        save(getFilePath());
    }

    public int size() {
        return list.size();
    }

    public void clear() {
        list.clear();
    }

    // Protected methods (for persistence)
    // protected void load(String filePath) {
    //     try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
    //         this.list = (List<T>) ois.readObject();
    //     } catch (Exception e) {
    //         System.err.println("Error loading data: " + e.getMessage());
    //     }
    // }
    // new load
     protected void load(String filePath){
        List<List<String>> data = new ArrayList<>();
        try ( BufferedReader br = new BufferedReader(new FileReader(filePath))){
            String line;
            while((line = br.readLine()) != null){
                System.out.println(line);
                String[] values = line.split(",");
                List<String> lineData = Arrays.asList(values);
                data.add(lineData);
            }
            for (int i = 0; i < data.size(); i++) {
                for(int j = 0; j < data.get(i).size(); j++){
                    System.out.print(data.get(i).get(j) + " ");
                }
                System.out.println();
            }

        } catch(IOException e){
            System.err.println("Error reading the CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    protected void save(String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(list);
        } catch (Exception e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }
}
