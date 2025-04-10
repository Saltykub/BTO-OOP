package entity.project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.util.ArrayList;

public class Project {
    private String projectID;
    private String name;
    private List<String> neighborhood;
    private Map<FlatType, Integer> availableUnit;
    private Map<FlatType, Integer> price;
    private LocalDate openDate;
    private LocalDate closeDate;
    private String managerID;
    private int availableOfficer;
    private List<String> officerID;
    private List<String> applicantID;
    private boolean visibility;

    public Project() {
        this.projectID = "";
        this.name = "";
        this.neighborhood = new ArrayList<>();
        this.availableUnit = new HashMap<>();
        this.price = new HashMap<>();
        this.openDate = LocalDate.now(); // default as current time
        this.closeDate = LocalDate.now(); // default as current time 
        this.managerID = "";
        this.availableOfficer = 0;
        this.officerID = new ArrayList<>();
        this.applicantID = new ArrayList<>();
        this.visibility = false;
    }

    public Project(String projectID, String name, List<String> neighbourhood, Map<FlatType, Integer> availableUnit,
            Map<FlatType, Integer> price,
            LocalDate openDate, LocalDate closeDate, String managerID, int availableOfficer, boolean visibility) {

        this.projectID = projectID;
        this.name = name;
        this.neighborhood = neighbourhood;
        this.availableUnit = availableUnit;
        this.price = price;
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.managerID = managerID;
        this.availableOfficer = availableOfficer;
        this.officerID = new ArrayList<>();
        this.applicantID = new ArrayList<>();
        this.visibility = visibility;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(List<String> neighborhood) {
        this.neighborhood = neighborhood;
    }

    public Map<FlatType, Integer> getAvailableUnit() {
        return availableUnit;
    }

    public void setAvailableUnit(Map<FlatType, Integer> availableUnit) {
        this.availableUnit = availableUnit;
    }

    public Map<FlatType, Integer> getPrice() {
        return price;
    }

    public void setPrice(Map<FlatType, Integer> price) {
        this.price = price;
    }

    public LocalDate getOpenDate() {
        return openDate;
    }

    public void setOpenDate(LocalDate openDate) {
        this.openDate = openDate;
    }

    public LocalDate getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(LocalDate closeDate) {
        this.closeDate = closeDate;
    }

    public String getManagerID() {
        return managerID;
    }

    public void setManagerID(String managerID) {
        this.managerID = managerID;
    }

    public int getAvailableOfficer() {
        return availableOfficer;
    }

    public void setAvailableOfficer(int availableOfficer) {
        this.availableOfficer = availableOfficer;
    }

    public List<String> getOfficerID() {
        return officerID;
    }

    public void setOfficerID(List<String> officerID) {
        this.officerID = officerID;
    }

    public List<String> getApplicantID() {
        return applicantID;
    }

    public void setApplicantID(List<String> applicantID) {
        this.applicantID = applicantID;
    }

    public boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public void setAll(Project project) {
        this.projectID = project.getProjectID();
        this.name = project.getName();
        this.neighborhood = project.getNeighborhood();
        this.availableUnit = project.getAvailableUnit();
        this.price = project.getPrice();
        this.openDate = project.getOpenDate();
        this.closeDate = project.getCloseDate();
        this.managerID = project.getManagerID();
        this.availableOfficer = project.getAvailableOfficer();
        this.officerID = project.getOfficerID();
        this.applicantID = project.getApplicantID();
        this.visibility = project.getVisibility();
    }

    public void print() {
        System.out.println("===== Project Info =====");
        System.out.println("Project ID: " + this.projectID);
        System.out.println("Name: " + this.name);
    
        System.out.println("Neighborhood: " + String.join(", ", this.neighborhood));
    
        System.out.println("Available Units:");
        for (Map.Entry<FlatType, Integer> entry : this.availableUnit.entrySet()) {
            System.out.println("  " + entry.getKey() + " = " + entry.getValue());
        }
    
        System.out.println("Price:");
        for (Map.Entry<FlatType, Integer> entry : this.price.entrySet()) {
            System.out.println("  " + entry.getKey() + " = $" + entry.getValue());
        }
    
        System.out.println("Open Date: " + this.openDate);
        System.out.println("Close Date: " + this.closeDate);
        System.out.println("Manager ID: " + this.managerID);
        System.out.println("Available Officers: " + this.availableOfficer);
    
        System.out.println("Officer IDs: " + String.join(", ", this.officerID));
        System.out.println("Applicant IDs: " + String.join(", ", this.applicantID));
        System.out.println("Visible to public? " + (this.visibility ? "Yes" : "No"));
        System.out.println("=========================");
    }
    
}
