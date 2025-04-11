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
            String test = Converter.objToString(pp);
            System.out.println(test);
        }
    }
    
    public static void save(String filePath){
        
    }
     
}
