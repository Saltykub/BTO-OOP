package entity.list;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {
    public static void main(String[] args){
        load("data_csv\\ProjectList.csv");
        
    }
    public static void display(ApplicantList applicantList){
        System.out.println(applicantList.getAll());
    }
    public static void load(String filePath){
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
}
