public class OfficerList extends ModelList {
  private String filepPath;
  private List<Officer> officers;
  
  public OfficerList() {
    this.officers = new ArrayList<>();
  }

  public String getFilePath() {
        return filePath;
  }
}
