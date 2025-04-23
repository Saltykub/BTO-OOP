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

/**
 * Controller responsible for managing filter and sort criteria for Project lists.
 * It holds the current filter settings as static variables and provides methods
 * to initialize, set up (via user input), display, and apply these filters to lists of Projects.
 */
public class FilterController {

    /** List of desired neighbourhood locations for filtering. Null or empty means no location filter. */
    public static List<String> location;
    /** The minimum desired price for filtering. Null means no lower price bound. */
    public static Integer priceLowerBound;
    /** The maximum desired price for filtering. Null means no upper price bound. */
    public static Integer priceUpperBound;
    /** The specific {@link FlatType} to filter by. Null means no flat type filter (show projects with any available units). */
    public static FlatType flatType;
    /** The earliest acceptable project open date for filtering. Null means no start date filter. */
    public static LocalDate startDate;
    /** The latest acceptable project close date for filtering. Null means no end date filter. */
    public static LocalDate endDate;
    /** The criteria used for sorting the filtered list (e.g., by NAME, PRICE, DATE). Defaults to NAME. */
    public static SortType sortType;

    /**
     * Initializes or resets all filter criteria to their default state.
     * Sets location, price bounds, flat type, and dates to null.
     * Sets the default sort type to {@link SortType#NAME}.
     * Typically called at the start of a session or when resetting filters.
     */
    public static void init() {
        location = null;
        priceLowerBound = null;
        priceUpperBound = null;
        startDate = null;
        endDate = null;
        sortType = SortType.NAME;
    }    

    /**
     * Displays the currently active filter and sort settings to the console.
     * Only non-null filter values are displayed.
     */
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

    /**
     * Retrieves Project objects based on a list of project IDs and then applies the current filters.
     *
     * @param IDList A list of project IDs to retrieve and filter.
     * @return A list of {@link Project} objects corresponding to the IDs, after applying the filters defined in this controller.
     */
    public static List<Project> filteredListFromID(List<String> IDList) {
        List<Project> ret = new ArrayList<>();
        for (String id : IDList) {
            ret.add(ProjectList.getInstance().getByID(id));
        }
        return filteredList(ret);
    }

    /**
     * Applies the currently configured filters and sorting to a given list of Projects.
     * Filters applied include:
     * - Flat Type: Removes projects that have 0 units of the specified {@code flatType}. If {@code flatType} is null, it removes projects with 0 units of BOTH TWO_ROOM and THREE_ROOM.
     * - Location: Keeps projects where at least one specified location name is contained within the project's neighbourhood list.
     * - Price: Keeps projects where both TWO_ROOM and THREE_ROOM prices fall within the {@code priceLowerBound} and {@code priceUpperBound} (if bounds are set).
     * - Date: Keeps projects where the open date is after {@code startDate} and the close date is before {@code endDate} (if dates are set).
     * Sorts the filtered list based on the current {@code sortType}.
     *
     * @param project The initial list of {@link Project} objects to filter and sort.
     * @return A new list containing the filtered and sorted Projects.
     */
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

    /**
     * Provides a console interface for the user to set up or modify the filter criteria.
     * Prompts the user for desired locations, price range, flat type, date range, and sort type.
     * Updates the static filter variables in this controller based on user input.
     * Allows skipping filters by pressing ENTER.
     */
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
