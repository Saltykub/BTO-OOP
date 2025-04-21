package entity.list;

public interface Saveable {

    public void load(String filePath, boolean hasHeader);
    public void save(String filePath);
    
}
