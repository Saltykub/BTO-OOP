package entity.user;

public class User {
    private String userID;
    private String name;
    private String hashedPassword;
    private int age;
    private MaritalStatus maritalStatus;

    public User(String userID, String name, String hashedPassword, int age, MaritalStatus maritalStatus) {
        this.userID = userID;
        this.name = name;
        this.hashedPassword = hashedPassword;
        this.age = age;
        this.maritalStatus = maritalStatus;
    }

    public String getUserId() {
        return userID;
    }

    public void setUserId(String userID) {
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
}
