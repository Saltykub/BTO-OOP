package entity.user;

public class UsersApp {
    public static void main(String[] args) {
        // Create a new User
        User user1 = new User("S1234567B", "Alice Tan", "hashedpw123", 28, MaritalStatus.SINGLE);

        // Print details
        System.out.println("User ID: " + user1.getUserId());
        System.out.println("Name: " + user1.getName());
        System.out.println("Hashed Password: " + user1.getHashedPassword());
        System.out.println("Age: " + user1.getAge());
        System.out.println("Marital Status: " + user1.getMaritalStatus());

        // Update some fields
        user1.setName("Alice T.");
        user1.setAge(29);
        user1.setMaritalStatus(MaritalStatus.MARRIED);

        System.out.println("\n--- After Updates ---");
        System.out.println("Updated Name: " + user1.getName());
        System.out.println("Updated Age: " + user1.getAge());
        System.out.println("Updated Marital Status: " + user1.getMaritalStatus());
    }
}
