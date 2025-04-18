package entity.list;

import entity.user.Applicant;

public class ApplicantList extends ModelList<Applicant> {
    private static String FILE_PATH = "data_csv/ApplicantList.csv";

    public ApplicantList(String FILE_PATH) {
        super(FILE_PATH,Applicant.class);
    }

    public static ApplicantList getInstance() {
        return new ApplicantList(FILE_PATH);
    }

    public String getFilePath() {
        return FILE_PATH;
    }

    public Applicant getByID(String ID) {
        for (Applicant applicant : getAll()) {
            if (applicant.getUserID().equals(ID)) {
                return applicant;
            }
        }
        return null;
    }

}
