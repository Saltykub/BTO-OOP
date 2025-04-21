package controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import entity.list.ProjectList;
import entity.project.FlatType;
import entity.project.Project;
import utils.IOController;
import utils.SortType;
import utils.UIController;

public class FilterController {
    
    public static List<String> location;
    public static Integer priceLowerBound;
    public static Integer priceUpperBound;
    public static FlatType flatType;
    public static LocalDate startDate;
    public static LocalDate endDate;
    public static SortType sortType;

    public static void init() {
        location = null;
        priceLowerBound = null;
        priceUpperBound = null;
        startDate = null;
        endDate = null;
        sortType = SortType.NAME;
    }    

    public static void displayFilter() {
        System.out.println(UIController.LINE_SEPARATOR);
        System.out.println("Your Current Filter:");
        if (location != null) System.out.println("Location: " + String.join(", ", location));
        if (priceLowerBound != null) System.out.println("Lowest Price: " + priceLowerBound);
        if (priceUpperBound != null) System.out.println("Highest Price: " + priceUpperBound);
        if (flatType != null) System.out.println("Flat Type: " + flatType); 
        if (startDate != null) System.out.println("Start Date: " + startDate); 
        if (endDate != null) System.out.println("Close Date: " + endDate);
        if (sortType != null) System.out.println("Sort By: " + sortType);
        System.out.println(UIController.LINE_SEPARATOR);
    }

    public static List<Project> filteredListFromID(List<String> IDList) {
        List<Project> ret = new ArrayList<>();
        for (String id : IDList) {
            ret.add(ProjectList.getInstance().getByID(id));
        }
        return filteredList(ret);
    }

    public static List<Project> filteredList(List<Project> project) {
        // filter flatType
        List<Project> temp = new ArrayList<>(project);
        for(Project p: temp){
            Map<FlatType,Integer> mp = p.getAvailableUnit();
            if(flatType == null) {
                if(mp.get(FlatType.THREE_ROOM) == 0 && mp.get(FlatType.TWO_ROOM) == 0) project.remove(p);
            }
            else if(mp.get(flatType) == 0) project.remove(p); // no desire flat Type  

        }
        // other filter
        return project.stream()
            .filter(p -> location == null || location.isEmpty() || location.stream().anyMatch(p.getNeighborhood()::contains))
            .filter(p -> priceLowerBound == null || 
                (p.getPrice().get(FlatType.THREE_ROOM) >= priceLowerBound) && p.getPrice().get(FlatType.TWO_ROOM) >= priceLowerBound)
            .filter(p -> priceUpperBound == null || 
                (p.getPrice().get(FlatType.THREE_ROOM) <= priceUpperBound) && p.getPrice().get(FlatType.TWO_ROOM) <= priceUpperBound)
            .filter(p -> startDate == null || (p.getOpenDate().isAfter(startDate)))
            .filter(p -> endDate == null || (p.getCloseDate().isBefore(endDate)))
            .sorted((p1, p2) -> {
                switch (sortType) {
                    case PRICE:
                        return Integer.compare(
                            p1.getPrice().getOrDefault(flatType, Integer.MAX_VALUE),
                            p2.getPrice().getOrDefault(flatType, Integer.MAX_VALUE)
                        );
                    case DATE:
                        return p1.getOpenDate().compareTo(p2.getOpenDate());
                    case NAME:
                        String loc1 = p1.getName();
                        String loc2 = p2.getName();
                        return loc1.compareTo(loc2);
                    default:
                        return 0;
                }
            })
        .collect(Collectors.toList());
    }
    
    public static void setup(){
        System.out.print("Number of location near by: ");
        List<String> l = new ArrayList<>();
        String tmp;
        int tmpint = IOController.nextInt();
        while (tmpint < 0) {
            System.out.print("Please enter valid number: ");
            tmpint = IOController.nextInt();
        }
        if (tmpint != 0) System.out.println("List of location near by:");
        else l = null;
        while (tmpint > 0) {
            System.out.print("\t: ");
            tmp = IOController.nextLine();        
            l.add(tmp);
            tmpint--;
        }
        location = l;
        System.out.print("Please enter lowest price (ENTER to skip): ");
        tmp = IOController.nextLine();
        if (!tmp.isEmpty()) {
            priceLowerBound = Integer.parseInt(tmp);
            while (priceLowerBound < 0) {
                System.out.print("Please enter valid number: ");
                priceLowerBound = IOController.nextInt();
            }
        }
        else priceLowerBound = null;
        System.out.print("Please enter highest price (ENTER to skip): ");
        tmp = IOController.nextLine();
        if (!tmp.isEmpty()) {
            priceUpperBound = Integer.parseInt(tmp);
            while (priceUpperBound < 0) {
                System.out.print("Please enter valid number: ");
                priceUpperBound = IOController.nextInt();
            }
        }
        else priceUpperBound = null;
        System.out.println("Enter flat type:");
        System.out.println("\t1. " + "Two Room");
        System.out.println("\t2. " + "Three Room");
        System.out.println("\t3. " + "No Filter");
        tmpint = IOController.nextInt();
        while (tmpint < 0) {
            System.out.print("Please enter valid number: ");
            tmpint = IOController.nextInt();
        }
        switch(tmpint){
            case 1 -> flatType = FlatType.TWO_ROOM;
            case 2 -> flatType = FlatType.THREE_ROOM;
            case 3 -> flatType = null;
        }
        System.out.print("Do you want to filter start date? (Y/N): ");
        tmp = IOController.nextLine();
        while (!tmp.equals("Y") && !tmp.equals("N")) {
            System.out.print("Please enter valid option (Y/N): ");
            tmp = IOController.nextLine();
        }
        if (tmp.equals("Y")) {
            System.out.println("Enter start date:");
            startDate = IOController.nextDate();
        }
        else startDate = null;
        System.out.print("Do you want to filter close date? (Y/N): ");
        tmp = IOController.nextLine();
        while (!tmp.equals("Y") && !tmp.equals("N")) {
            System.out.print("Please enter valid option (Y/N): ");
            tmp = IOController.nextLine();
        }
        if (tmp.equals("Y")) {
            System.out.println("Enter close date:");
            endDate = IOController.nextDate();
        }
        else endDate = null;
        System.out.println("Enter sort type:");
        System.out.println("\t1. " + "Name");
        System.out.println("\t2. " + "Price");
        System.out.println("\t3. " + "Date");
        tmpint = IOController.nextInt();
        while (tmpint < 0) {
            System.out.print("Please enter valid number: ");
            tmpint = IOController.nextInt();
        }
        switch(tmpint){
            case 1 -> sortType = SortType.NAME;
            case 2 -> sortType = SortType.PRICE;
            case 3 -> sortType = SortType.DATE;
        }
    }
}
