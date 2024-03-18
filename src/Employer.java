import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import  org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Employer {

    static JSONParser parser = new JSONParser();
    File file;

    private boolean checkFileExist (String fileName) throws IOException {
        file = new File(fileName);
        return file.createNewFile() ;
    }

    private void createFile(String fileName) throws IOException {
        file = new File(fileName);
    }

    private void writeFile(String fileName,String content) throws IOException {
        FileWriter fileWriter = new FileWriter(fileName);
        fileWriter.write("{\"employers\":" + content + "}");
        fileWriter.close();
    }

    private  JSONArray readFile(String fileName) throws IOException, ParseException {
        // read file
        FileReader fileReader = new FileReader(fileName);
        JSONObject  obj = (JSONObject) parser.parse(fileReader);
        JSONArray  items = (JSONArray) obj.get("employers");

        return items;
    }

    private void showEmployers(String fileName , JSONArray items) throws IOException {
        if(!checkFileExist(fileName)) {

            for(Object item: items) {
                JSONObject i = (JSONObject) item;

                for(Object key : i.keySet()) {
                    Object value = i.get(key);
                    System.out.println(key+":"+value);
                }
                System.out.println("");
            }
        }
    }

    public void save(String fileName, String employers ) throws IOException, ParseException {
        if(checkFileExist(fileName)) createFile(fileName);

        writeFile(fileName,employers);

        JSONArray  items =  readFile(fileName);

        showEmployers(fileName, items);
    }


    public void printEmployers(String fileName) throws IOException, ParseException {
        if(!checkFileExist(fileName)) {
            JSONArray items = (JSONArray) readFile(fileName);
            showEmployers(fileName, items);
        }
    }


    public void findEmployerByCode(String fileName ,  String code) throws IOException, ParseException {
        if(!checkFileExist(fileName)) {
            JSONArray items =  (JSONArray) readFile(fileName);

            for(Object item: items) {
                JSONObject i = (JSONObject) item;

                if(i.get("code").equals(code)) {
                    for (Object key : i.keySet()) {
                        Object value = i.get(key);
                        System.out.println(key+":"+value);
                    }
                    break;
                }
            }

        }
    }

    public void deleteEmployerByCode(String fileName, String code) throws IOException, ParseException {
        JSONArray newItems = new JSONArray();
        if(!checkFileExist(fileName)) {
            JSONArray items = (JSONArray) readFile(fileName);

            for(Object item: items) {
                JSONObject i = (JSONObject) item;

                if(!i.get("code").equals(code)) {
                    JSONObject newItem = new JSONObject();
                    for(Object key: i.keySet()) {
                        Object value = i.get(key);
                        newItem.put(key, value);
                    }
                    newItems.add(newItem);
                }
            }

            writeFile("employer.json", newItems.toJSONString());

            System.out.println("delete success");
        }

    }


    public void updateEmployerByCode(String fileName , String code  , String fullname, float salary  ) throws IOException, ParseException {
        if(!checkFileExist(fileName)) {
            JSONArray items = (JSONArray) readFile(fileName);

            for(Object item: items) {
                JSONObject i = (JSONObject) item;

                if(i.get("code").equals(code)) {
                    for(Object key: i.keySet()) {
                        Object updateFullName =  key.equals("fullname") ? i.put("fullname", fullname) : "";
                        Object updateSalary  = key.equals("salary") ? i.put("salary" , salary == 0 ? i.get("salary") : salary) : "";
                    }
                    break;
                }
            }
            writeFile(fileName, items.toJSONString());
            System.out.println("update success");
        }
    }

    public void findEmployerBySalary(String fileName , float fromSalary ,float toSalary) throws IOException, ParseException {
        if(!checkFileExist(fileName)) {
            JSONArray items =  (JSONArray) readFile(fileName);

            for(Object item: items) {
                JSONObject i = (JSONObject) item;
                float salary =  Float.parseFloat(i.get("salary").toString());
                if(salary >= fromSalary && salary <= toSalary ) {
                    for (Object key: i.keySet()) {
                        System.out.println(key+":" + i.get(key));
                    }
                }
                System.out.println("");
            }


        }
    }


    /**
     * @param fileName
     * @throws IOException
     * @throws ParseException
     * @return items sorted follow fullname from a-z
     */
    public void sortEmployerByFullName(String fileName) throws IOException, ParseException {
        if(!checkFileExist(fileName)) {
            JSONArray items = (JSONArray) readFile(fileName);

            for(int i = 0 ; i < items.size(); i++) {
                for (int j = i + 1 ; j < items.size() ; j++) {
                    JSONObject item1 = (JSONObject) items.get(i);
                    String fullName  = item1.get("fullname").toString();
                    String[] splitFullName = fullName.split(" ");
                    int charCode =  splitFullName[splitFullName.length - 1].toLowerCase().codePointAt(0);


                    JSONObject item2 = (JSONObject) items.get(j);
                    String fullName1  = item2.get("fullname").toString();
                    String[]  splitFullName1 = fullName1.split(" ");
                    int charCode1 =  splitFullName1[splitFullName1.length - 1].toLowerCase().codePointAt(0);

                    if(charCode1 < charCode) {
                        items.set(j, item1);
                       items.set(i, item2);
                    }

                }
            }

            showEmployers(fileName, items);
        }
    }


    /**
     * @param fileName
     * @throws IOException
     * @throws ParseException
     * @return items sorted from a-z
     */
    public void sortEmployerBySalary(String fileName) throws IOException, ParseException {
        if(!checkFileExist(fileName)) {
            JSONArray items = (JSONArray) readFile(fileName);

            for(int i = 0 ; i < items.size(); i++) {
                for (int j = i + 1 ; j < items.size() ; j++) {
                    JSONObject item1 = (JSONObject) items.get(i);
                    float currentItemSalary = Float.parseFloat( item1.get("salary").toString());


                    JSONObject item2 = (JSONObject) items.get(j);
                    float nextItemSalary = Float.parseFloat( item2.get("salary").toString());

                    if(nextItemSalary < currentItemSalary) {
                        items.set(j, item1);
                        items.set(i, item2);
                    }
                }
            }

            showEmployers(fileName, items);
        }
    }


    public void findFiveEmployersMostSalary(String fileName) throws IOException, ParseException {
        JSONArray newItems =  new JSONArray();
        if(!checkFileExist(fileName)) {
            JSONArray items = (JSONArray) readFile(fileName);

            for(int i = 0 ; i < items.size(); i++) {
                for (int j = i + 1 ; j < items.size() ; j++) {
                    JSONObject item1 = (JSONObject) items.get(i);
                    float currentItemSalary = Float.parseFloat( item1.get("salary").toString());


                    JSONObject item2 = (JSONObject) items.get(j);
                    float nextItemSalary = Float.parseFloat( item2.get("salary").toString());

                    if(nextItemSalary > currentItemSalary) {
                        items.set(j, item1);
                        items.set(i, item2);
                    }
                }

                if(i < 5) {
                    newItems.add(items.get(i));
                }
            }

            showEmployers(fileName, newItems);
        }
    }

    public static void main(String[] args) throws IOException, ParseException {
        Employer  employer = new Employer();
        String data = "[\n" +
                "  { \"id\": 1, \"fullname\": \"ABC Industries\", \"salary\": 60000, \"code\": \"ABC123\" },\n" +
                "  { \"id\": 2, \"fullname\": \"XYZ Corporation\", \"salary\": 70000, \"code\": \"XYZ456\" },\n" +
                "  { \"id\": 3, \"fullname\": \"Tech Innovations Ltd.\", \"salary\": 80000, \"code\": \"TECH789\" },\n" +
                "  { \"id\": 4, \"fullname\": \"Global Solutions Inc.\", \"salary\": 65000, \"code\": \"GSOL222\" },\n" +
                "  { \"id\": 5, \"fullname\": \"Dynamic Enterprises\", \"salary\": 75000, \"code\": \"DYNE333\" },\n" +
                "  { \"id\": 6, \"fullname\": \"Innovate Labs\", \"salary\": 72000, \"code\": \"INNO777\" },\n" +
                "  { \"id\": 7, \"fullname\": \"Visionary Ventures\", \"salary\": 68000, \"code\": \"VV111\" },\n" +
                "  { \"id\": 8, \"fullname\": \"Strategic Solutions Ltd.\", \"salary\": 78000, \"code\": \"SSL999\" },\n" +
                "  { \"id\": 9, \"fullname\": \"NexGen Technologies\", \"salary\": 70000, \"code\": \"NXT202\" },\n" +
                "  { \"id\": 10, \"fullname\": \"Alpha Innovations\", \"salary\": 82000, \"code\": \"ALPHA101\" }\n" +
                "]";

        String fileName = "employer.json";
//        employer.save("employer.json", data);
//        employer.deleteEmployerByCode("employer.json", "DYNE333");
//        employer.findEmployerByCode("employer.json", "SSL999");
//        employer.updateEmployerByCode("employer.json","INNO777",  "Jack100" , 0  );
//        employer.findEmployerBySalary(fileName ,5000, 100000);
//        employer.sortEmployerByFullName(fileName);
//        employer.sortEmployerBySalary(fileName);
        employer.findFiveEmployersMostSalary(fileName);
    }
}