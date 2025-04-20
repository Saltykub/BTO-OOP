package entity.list;

import entity.user.Officer;

public class OfficerList extends ModelList<Officer> {
  private static final String FILE_PATH = "data_csv/OfficerList.csv";
  
  public OfficerList(String FILE_PATH) {
    super(FILE_PATH,Officer.class);
  }

  public static OfficerList getInstance() {
    return new OfficerList(FILE_PATH);
  }

  public String getFilePath() {
    return FILE_PATH;
  }
  
  public Officer getByID(String ID) {
      for (Officer officer : this.getAll()) {
          if (officer.getUserID().equals(ID)) {
              return officer;
          }
      }
      return null;
  }
}
