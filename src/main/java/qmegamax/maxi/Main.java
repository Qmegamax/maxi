package main.java.qmegamax.maxi;

import main.java.qmegamax.maxi.pages.errors.ConfigReadErrorPage;
import main.java.qmegamax.maxi.pages.LoginPage;
import main.java.qmegamax.maxi.pages.errors.ConnectionErrorPage;
import main.java.qmegamax.maxi.util.Credential;
import main.java.qmegamax.maxi.util.DatabaseRow;
import main.java.qmegamax.maxi.util.Reservation;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public static String PATH;
    public static Connection CONNECTION;
    public static int TABLEAMOUNT;
    public static int OCUPATIONTIME;
    public static int CHAIRAMOUNT;
    public static boolean AREGUESTSALLOWED;
    public static int CAPCHAAMOUNT;
    public static String DATABASEIP;
    public static String DATABASEUSER;
    public static String DATABASEPASSWORD;
    public static boolean USEENCRYPTION;
    public static String ENCRYPTIONKEY;
    public static int OPENINGHOUR;
    public static int CLOSINGHOUR;

    public static boolean getConfig(){
        try {
            File myObj = new File(PATH+"config.txt");
            Scanner sc = new Scanner(myObj);
            HashMap<String,String> configs = new HashMap<>();

            while (sc.hasNextLine()) {
                String data = sc.nextLine();
                if(!data.startsWith("#"))configs.put(data.split("=")[0],data.split("=")[1]);
            }

            TABLEAMOUNT = Integer.parseInt(configs.get("tableAmount"));
            OCUPATIONTIME = Integer.parseInt(configs.get("occupationTime"));
            CHAIRAMOUNT = Integer.parseInt(configs.get("chairAmount"));
            AREGUESTSALLOWED = Boolean.parseBoolean(configs.get("areGuestsAllowed"));
            CAPCHAAMOUNT = Integer.parseInt(configs.get("capchaAmount"));
            DATABASEIP = configs.get("databaseIp");
            DATABASEUSER = configs.get("databaseUser");
            DATABASEPASSWORD = configs.get("databasePassword");
            USEENCRYPTION = Boolean.parseBoolean(configs.get("useEncryption"));
            ENCRYPTIONKEY = configs.get("encryptionKey");
            OPENINGHOUR = Integer.parseInt(configs.get("openingHour"));
            CLOSINGHOUR = Integer.parseInt(configs.get("closingHour"));

            sc.close();
        } catch (Exception e) {
            new ConfigReadErrorPage();
            return false;
        }
        return true;
    }

    public static <T extends DatabaseRow> ArrayList<T> GetDataFromDatabase(String databaseName,T rowClass){
        ArrayList<DatabaseRow> arrayList = new ArrayList<>();

        try{
            Statement statement = CONNECTION.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from "+databaseName);

            while (resultSet.next()) {
                arrayList.add(rowClass.insert(resultSet));
            }
        }
        catch (Exception ignored) {}

        return (ArrayList<T>) arrayList;
    }

    public static ArrayList<Reservation> GetReservationsFromDatabase(){
        ArrayList<Reservation> reservations = new ArrayList<>();

        try{
            Statement statement = CONNECTION.createStatement();
            ResultSet resultSet = statement.executeQuery("select reservationId, time, name, notes, tableId from reservations");

            while (resultSet.next()) {
                reservations.add(new Reservation(resultSet.getInt(1),resultSet.getObject(2,LocalDateTime.class),resultSet.getString(3),resultSet.getString(4),resultSet.getInt(5)));
            }
        }
        catch (Exception ex) {System.out.println("uhoh");}

        return reservations;
    }

    public static String encryptPassword(String password,String key){
        String newPassword=password;

        int keyAsInt=0;
        for(int i=0;i<key.length();i++){
            keyAsInt+=key.charAt(i)+i;
        }

        char[] newPasswordChars = newPassword.toCharArray();
        for(int i=0;i<password.length();i++){
            newPasswordChars[i]=(char)(password.charAt(i)+keyAsInt);
        }
        newPassword=String.valueOf(newPasswordChars);

        return newPassword;
    }

    public static String decryptPassword(String password,String key){
        String newPassword=password;

        int keyAsInt=0;
        for(int i=0;i<key.length();i++){
            keyAsInt+=key.charAt(i)+i;
        }

        char[] newPasswordChars = newPassword.toCharArray();
        for(int i=0;i<password.length();i++){
            newPasswordChars[i]=(char)(password.charAt(i)-keyAsInt);
        }
        newPassword=String.valueOf(newPasswordChars);

        return newPassword;
    }

    public static void main(String[] args) {

        File file = new File("Main.java");
        String filePath=file.getAbsolutePath();
        PATH=(filePath.split("Main.java")[0]+"\\src\\main\\java\\qmegamax\\maxi\\");

        if(!getConfig())return;

        try {
            CONNECTION = DriverManager.getConnection("jdbc:mysql://"+DATABASEIP,DATABASEUSER, DATABASEPASSWORD);
        }
        catch (Exception e) {
            new ConnectionErrorPage();
            return;
        }

        new LoginPage();
    }
}