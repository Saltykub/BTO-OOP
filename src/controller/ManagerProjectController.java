public class ManagerProjectController {
    private String managerID;
    
    public void setManagerID(String managerID) {
        this.managerID = managerID;
    }
    
    public void createProject(String projectID, String name, List<String> neighbourhood,Map<FlatType, Integer> availableUnit, 
                              Map<FlatType, Integer> price,Date openDate, Date closeDate, String officerID, int availableOfficer) {
      Project(projectID,name,neighbourhood,availableUnit,price,openDate,closeDate,officerID,managerID,availableOfficer);
    }
    
    public void editProject(String projectID, Project project) {
      // Implementation to be done
    }
    public void deleteProject(String projectID) {
      //projectID = null;
    }
    public void toggleVisibility(String projectID) {
      //projectID.setVisibility(not projectID.getVisibility());
    }
    public void viewOfficerRegistrationStatus() {
      //officerID
    }
    private void viewProjectList(String managerID) {
      //managerID.getProject();
    }
    public void generateReport(Project filtered) {
      //to be done
    }
}
