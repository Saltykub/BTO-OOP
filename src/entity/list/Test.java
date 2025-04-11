package entity.list;

import java.util.ArrayList;
import java.util.List;

import controller.AccountController;
import entity.user.Applicant;
import entity.user.MaritalStatus;
import entity.user.Officer;


public class Test {
    public static void main(String[] args){

       
       Applicant a1 = new Applicant("S1234567A", "John", "password", 35, MaritalStatus.SINGLE);
       Applicant a2 = new Applicant("T7654321B", "Sarah", "password", 40, MaritalStatus.MARRIED);
       Applicant a3 = new Applicant("S9876543C", "Grace", "password", 37, MaritalStatus.MARRIED);
       Applicant a4 = new Applicant("T2345678D", "James", "password", 30, MaritalStatus.MARRIED);
       Applicant a5 = new Applicant("S3456789E", "Rachel", "password", 25, MaritalStatus.SINGLE);
    }
    
     
}
