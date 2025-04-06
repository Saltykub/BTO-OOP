package entity.project;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class Project {
    private String projectID;
    private String name;
    private String neighborhood;
    private Map<FlatType, Integer> availableUnit;
    private Map<FlatType, Integer> price;
    private Date openDate;
    private Date closeDate;
    private String managerID;
    private int availableOfficer;
    private List<String> officerID;
    private List<String> applicantID;
    private boolean visibility;

    public Project() {
        this.projectID = "";
        this.name = "";
        this.neighborhood = "";
        this.availableUnit = new HashMap<>();
        this.price = new HashMap<>();
        this.openDate = new Date();
        this.closeDate = new Date();
        this.managerID = "";
        this.availableOfficer = 0;
        this.officerID = new ArrayList<>();
        this.applicantID = new ArrayList<>();
        this.visibility = false;
    }

    public Project(String projectID, String name, String neighbourhood, Map<FlatType, Integer> availableUnit,
            Map<FlatType, Integer> price,
            Date openDate, Date closeDate, String managerID, int availableOfficer, List<String> officerID,
            List<String> applicantID, boolean visibility) {
        this.projectID = projectID;
        this.name = name;
        this.neighborhood = neighbourhood;
        this.availableUnit = availableUnit;
        this.price = price;
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.managerID = managerID;
        this.availableOfficer = availableOfficer;
        this.officerID = officerID;
        this.applicantID = applicantID;
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

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
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

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
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
}
