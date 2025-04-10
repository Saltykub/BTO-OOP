package entity.list;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.project.Project;
import entity.user.User;



public class Test {
    // const for seperator
    public static final String LIST_SEPARATOR = "::LIST::";
    public static final String DATE_SEPARATOR = "::DATE::";
    public static final String MAP_SEPARATOR = "::MAP::";
    public static void main(String[] args){
        ProjectList p = new ProjectList("data_csv\\test.csv");
        for(Project pp: p.getAll()){
            pp.print();
        }
    }
    
    public static void load(String filePath, boolean hasHeader){
        List<List<String>> data = new ArrayList<>();
        try ( BufferedReader br = new BufferedReader(new FileReader(filePath))){
            if(hasHeader){
                br.readLine();
            }
            String line;
            while((line = br.readLine()) != null){
                String[] values = line.split(",");
                List<String> lineData = Arrays.asList(values);
                data.add(lineData);
            }
            for (int i = 0; i < data.size(); i++) {
                for(int j = 0; j < data.get(i).size(); j++){
                    // Read List
                    
                }
            }

        } catch(IOException e){
            System.err.println("Error reading the CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }
     
}
