package entity.list;

import java.util.List;

import utils.Converter;

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

    // abstract method
    public abstract String getFilePath();

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
        save(getFilePath());
    }

    public int size() {
        return list.size();
    }

    public void clear() {
        list.clear();
    }

    // Protected methods (for persistence)
    protected void load(String filePath, boolean hasHeader){
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

    protected void save(String filePath) { 
        try (PrintWriter printWriter = new PrintWriter(new FileWriter(filePath))) {
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
