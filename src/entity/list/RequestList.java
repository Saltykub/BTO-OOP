package entity.list;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import entity.request.Request;
import utils.Converter;

public class RequestList extends ModelList<Request> {
    private static String filePath = "data_csv/RequestList.csv";

    public RequestList(String filePath) {
        super(filePath, Request.class);
    }

    public static RequestList getInstance() {
        return new RequestList(filePath);
    }
    
    public String getFilePath() {
        return filePath;
    }

    public Request getByID(String requestID) {
        for (Request request : this.getAll()) {
            if (request.getRequestID().equals(requestID)) {
                return request;
            }
        }
        return null;
    }

    @Override
    protected void load(String filePath, boolean hasHeader) {
        List<String> data = new ArrayList<>();
        File file = new File(filePath);

        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                if (hasHeader) {
                    br.readLine(); // Skip header
                }

                String line;
                while ((line = br.readLine()) != null) {
                    data.add(line);
                }

                for (String d : data) {
                    Class<? extends Request> clazzR = Converter.getRequestClass(d);
                    Request val = Converter.stringtoObj(d, clazzR);
                    super.add(val);
                }

            } catch (IOException e) {
                System.err.println("Error loading the CSV file: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Error creating the file: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

}
