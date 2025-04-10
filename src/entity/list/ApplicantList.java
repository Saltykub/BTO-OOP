package entity.list;

import entity.user.Applicant;

public class ApplicantList extends ModelList<Applicant> {
  private static String filePath;

  public ApplicantList(String filePath) {
    super(filePath);
  }

  public static ApplicantList getInstance() {
    return new ApplicantList(filePath);
  }

  public String getFilePath() {
    return filePath;
  }

}
