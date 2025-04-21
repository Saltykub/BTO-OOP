package entity.user;

public interface User {
   
    public String getUserID();

    public void setUserID(String userID);

    public String getName();

    public void setName(String name);

    public String getHashedPassword();

    public void setHashedPassoword(String hashedPassword);

    public int getAge();

    public void setAge(int age);

    public MaritalStatus getMaritalStatus();

    public void setMaritalStatus(MaritalStatus maritalStatus);

}
