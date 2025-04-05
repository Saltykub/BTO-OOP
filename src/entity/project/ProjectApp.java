package entity.project;

import java.util.*;

public class ProjectApp {
    public static void main(String[] args) {
        // Create a sample map for available units and prices
        Map<FlatType, Integer> availableUnits = new HashMap<>();
        availableUnits.put(FlatType.TWO_ROOM, 10);
        availableUnits.put(FlatType.THREE_ROOM, 5);

        Map<FlatType, Integer> prices = new HashMap<>();
        prices.put(FlatType.TWO_ROOM, 100000);
        prices.put(FlatType.THREE_ROOM, 150000);

        // Create a list of officers and applicants
        List<String> officers = new ArrayList<>(Arrays.asList("O123", "O124"));
        List<String> applicants = new ArrayList<>(Arrays.asList("A123", "A124"));

        // Create project instance
        Project project1 = new Project(
                "P001",
                "Punggol Apartments",
                "Punggol",
                availableUnits,
                prices,
                new Date(),
                new Date(System.currentTimeMillis() + 86400000), // 1 day later
                "M001",
                3,
                officers,
                applicants,
                true);

        // Display project details
        System.out.println("Project ID: " + project1.getProjectID());
        System.out.println("Project Name: " + project1.getName());
        System.out.println("Available Units (1BHK): " + project1.getAvailableUnit().get(FlatType.TWO_ROOM));
        System.out.println("Price for 2BHK: " + project1.getPrice().get(FlatType.THREE_ROOM));
        System.out.println("Manager ID: " + project1.getManagerID());
    }
}
