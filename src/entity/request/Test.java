package entity.request;

public class Test {
    public static void main(String[] args){

        BTOApplication test = new BTOApplication("001",RequestType.BTO_APPLICATION,"001","001",RequestStatus.PENDING);
        display(test);
    }
    public static void display(BTOApplication r){
        System.out.println(r.getRequestID());
        System.out.println(r.getProjectID());
        System.out.println(r.getRequestType());
        System.out.println(r.getUserID());
        System.out.println(r.getRequestStatus());
        System.out.println(r.getApplicationStatus());
    }
}
