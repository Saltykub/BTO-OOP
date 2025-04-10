package entity.user;

public class User {
    private String userID;
    private String name;
    private String hashedPassword;
    private int age;
    private MaritalStatus maritalStatus;

    public User() {
        this.userID = "";
        this.name = "";
        this.hashedPassword = "";
        this.age = 0;
        this.maritalStatus = MaritalStatus.SINGLE; // or null if you prefer
    }

    public User(String userID, String name, String hashedPassword, int age, MaritalStatus maritalStatus) {
        this.userID = userID;
        this.name = name;
        this.hashedPassword = hashedPassword;
        this.age = age;
        this.maritalStatus = maritalStatus;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassoword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public void print(){
        System.out.println("UserID: " + userID);
        System.out.println("Name: " + name);
        System.out.println("HashedPassword: " + hashedPassword);
        System.out.println("Age: " + age);
        System.out.println("Marital Status: " + maritalStatus);
    }
}
