package entity.list;

import entity.user.Applicant;

public class ApplicantList extends ModelList<Applicant> {
  private String filePath;

  public ApplicantList(String filePath) {
    super(filePath);
  }

  public String getFilePath() {
    return filePath;
  }

}
