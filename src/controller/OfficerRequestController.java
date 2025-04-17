package controller;

import java.util.List;

import boundary.Display;
import entity.list.OfficerList;
import entity.list.ProjectList;
import entity.list.RequestList;
import entity.project.Project;
import entity.request.Enquiry;
import entity.request.OfficerRegistration;
import entity.request.Request;
import entity.request.RequestStatus;
import entity.request.RequestType;
import entity.user.Officer;
import entity.user.UserType;

public class OfficerRequestController {
    private static String officerID;

    public static void setOfficerID(String ID) {
        officerID = ID;
    }

    public static void registerProject(String projectID) {
        RequestList.getInstance().add(new OfficerRegistration(IDController.newRequestID(), RequestType.REGISTRATION, officerID, projectID, RequestStatus.PENDING));
    }

    public static void viewRegisteredProject() {
        List<Project> list = ProjectList.getInstance().getAll();
        for (Project project : list) {
            if (project.getOfficerID().contains(officerID)) {
                System.out.println(project);
            }
        }
    }

    public static void viewEnquiries(){
        List<Request> list = RequestList.getInstance().getAll();
        Officer o = OfficerList.getInstance().getByID(officerID);
        List<String> projectID = o.getOfficerProject();
        for(Request request : list){
            for(String id: projectID){
                if(request.getProjectID().equals(id) && request.getRequestType() == RequestType.ENQUIRY){
                    Display.displayRequest(request, UserType.OFFICER);
                    break;
                }
            }
        }

    }
    public static void viewEnquiries(String projectID) {
        List<Request> list = RequestList.getInstance().getAll();
        for (Request request : list) {
            if (request.getProjectID().equals(projectID) && request.getRequestType() == RequestType.ENQUIRY) {
                Display.displayRequest(request, UserType.OFFICER);
            }
        }
    }

    public static void answerEnquiry(String requestID, String text) {
        Enquiry enquiry = (Enquiry) RequestList.getInstance().getByID(requestID);
        enquiry.setAnswer(text);
        RequestList.getInstance().update(requestID, enquiry);
    }
}