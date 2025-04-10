package controller;

import java.util.List;

import entity.list.RequestList;
import entity.request.ApprovedStatus;
import entity.request.BTOApplication;
import entity.request.BTOWithdrawal;
import entity.request.OfficerRegistration;
import entity.request.Request;
import entity.request.RequestStatus;
import entity.request.RequestType;

public class ManagerRequestController {
    private static String managerID;
    
    public static void setManagerID(String ID) {
        managerID = ID;
    }
    
    public static void viewRequest() {
        List<Request> list = RequestList.getInstance().getAll();
        for (Request request : list) {
            if (request.getRequestType() != RequestType.ENQUIRY) {
                System.out.println(request);
            }
        }
    }
    
    public static void viewRequest(boolean applicant) {
        List<Request> list = RequestList.getInstance().getAll();
        for (Request request : list) {
            if ((applicant && (request.getRequestType() == RequestType.BTO_APPLICATION || request.getRequestType() == RequestType.BTO_WITHDRAWAL)) || (!applicant && request.getRequestType() == RequestType.REGISTRATION)) {
                System.out.println(request);
            }
        }
    }
    
    public static void changeRequestStatus(String requestID, RequestStatus status) {
        Request request = RequestList.getInstance().getByID(requestID);
        request.setRequestStatus(status);
        RequestList.getInstance().update(requestID, request);
    }

    public static void changeApplicationStatus(String requestID, ApprovedStatus status) {
        Request request = RequestList.getInstance().getByID(requestID);
        if (request instanceof BTOApplication application) {
            application.setApplicationStatus(status);
            RequestList.getInstance().update(requestID, application);
        }
        else if (request instanceof BTOWithdrawal application) {
            application.setWithdrawalStatus(status);
            RequestList.getInstance().update(requestID, application);
        }
        else if (request instanceof OfficerRegistration application) {
            application.setRegistrationStatus(status);
            RequestList.getInstance().update(requestID, application);
        }
        if (status != ApprovedStatus.PENDING) changeRequestStatus(requestID, RequestStatus.DONE);
        else changeRequestStatus(requestID, RequestStatus.PENDING);
    }

    public static void viewAllEnquiries() {
        List<Request> list = RequestList.getInstance().getAll();
        for (Request request : list) {
            if (request.getRequestType() == RequestType.ENQUIRY) {
                System.out.println(request);
            }
        }
    }
}
