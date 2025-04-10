package entity.list;

import java.util.List;
import java.util.ArrayList;
import java.io.*;

public abstract class ModelList<T> {
    private Class<T> clazz;
    private List<T> list;

    // Constructors
    public ModelList(String filePath, Class<T> clazz) {
        this.clazz = clazz;
        this.list = new ArrayList<>();
        this.load(filePath, true);
    }

    public abstract String getFilePath();

    // Public methods
    public abstract T getByID(String ID); 

    public List<T> getAll() {
        return new ArrayList<>(list); // Return a copy to protect internal list
    }

    public void delete(String ID) {
        T item = getByID(ID);
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
        //save(getFilePath());
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
    protected void load(String filePath, boolean hasHeader){
        List<String> data = new ArrayList<>();
        try ( BufferedReader br = new BufferedReader(new FileReader(filePath))){
            if(hasHeader){
                br.readLine();
            }
            String line;
            while((line = br.readLine()) != null){;
                data.add(line);
            }
            for(String d: data){
                T val = Converter.StringtoObj(d, clazz);
                list.add(val);
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
