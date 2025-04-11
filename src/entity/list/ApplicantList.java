package entity.list;

import entity.user.Applicant;

public class ApplicantList extends ModelList<Applicant> {
  private static String filePath = "data_csv/ApplicantList.csv";

  public ApplicantList(String filePath) {
    super(filePath,Applicant.class);
  }

  public static ApplicantList getInstance() {
    return new ApplicantList(filePath);
  }

  public String getFilePath() {
    return filePath;
  }

  public Applicant getByID(String ID) {
      for (Applicant applicant : this.getAll()) {
          if (applicant.getUserID().equals(ID)) {
              return applicant;
          }
      }
      return null;
  }

}
