package controller;

import java.time.LocalDate;
import java.util.List;

import entity.project.FlatType;

public class FilterController {
    
    public static List<String> location;
    public static Integer priceLowerBound;
    public static Integer priceUpperBound;
    public static FlatType flatType;
    public static LocalDate startDate;
    public static LocalDate endDate;

    public static void setLocation(List<String> l) { location = l; } 
    public static void setPriceLb(Integer lb) { priceLowerBound = lb; } 
    public static void setPriceUb(Integer ub) { priceUpperBound = ub; } 
    public static void setFlatType(FlatType ft) { flatType = ft; } 
    public static void setStartDate(LocalDate st) { startDate = st; }
    public static void setEndDate(LocalDate ed) { endDate = ed; } 

}
