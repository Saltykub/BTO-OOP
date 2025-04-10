package entity.list;

import entity.user.Officer;

public class OfficerList extends ModelList<Officer> {
  private static String filePath;
  
  public OfficerList(String filePath) {
    super(filePath,Officer.class);
  }

  public static OfficerList getInstance() {
    return new OfficerList(filePath);
  }

  public String getFilePath() {
    return filePath;
  }
  
  public Officer getByID(String ID) {
      for (Officer officer : this.getAll()) {
          if (officer .getUserID().equals(ID)) {
              return officer;
          }
      }
      return null;
  }
}
