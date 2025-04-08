package entity.list;

import entity.user.Officer;

public class OfficerList extends ModelList<Officer> {
  private String filePath;
  
  public OfficerList(String filePath) {
    super(filePath);
  }

  public String getFilePath() {
    return filePath;
  }
  
}
