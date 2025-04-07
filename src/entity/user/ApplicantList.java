package entity.user;

import java.util.ArrayList;
import java.util.List;

public class ApplicantList extends ModelList {
  private String filePath;
  private List<Applicant> applicants;

  public ApplicantList() {
    this.applicants = new ArrayList<>();
  }

  public String getFilePath() {
    return filePath;
  }

}
