package entity.user;

import java.util.ArrayList;
import java.util.List;

public class OfficerList extends ModelList {
  private String filePath;
  private List<Officer> officers;

  public OfficerList() {
    this.officers = new ArrayList<>();
  }

  public String getFilePath() {
    return filePath;
  }
}
