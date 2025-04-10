package entity.list;

import entity.user.Officer;

public class OfficerList extends ModelList<Officer> {
  private static String filePath;
  
  public OfficerList(String filePath) {
    super(filePath);
  }

  public static OfficerList getInstance() {
    return new OfficerList(filePath);
  }

  public String getFilePath() {
    return filePath;
  }
  
}
