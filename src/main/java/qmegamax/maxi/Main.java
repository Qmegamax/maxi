package main.java.qmegamax.maxi;

import main.java.qmegamax.maxi.pages.errors.ConfigReadErrorPage;
import main.java.qmegamax.maxi.pages.LoginPage;
import main.java.qmegamax.maxi.pages.errors.ConnectionErrorPage;
import main.java.qmegamax.maxi.util.Credential;
import main.java.qmegamax.maxi.util.Reservation;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
            int completedChecks=0;

            while (sc.hasNextLine()) {
                if (completedChecks == 0) completedChecks++;
                String data = sc.nextLine();

                if (data.split("=")[0].equals("tableAmount")) {
                    TABLEAMOUNT = Integer.parseInt(data.split("=")[1]);
                    completedChecks++;
                }
                if (data.split("=")[0].equals("occupationTime")) {
                    OCUPATIONTIME = Integer.parseInt(data.split("=")[1]);
                    completedChecks++;
                }
                if (data.split("=")[0].equals("chairAmount")) {
                    CHAIRAMOUNT = Integer.parseInt(data.split("=")[1]);
                    completedChecks++;
                }
                if (data.split("=")[0].equals("areGuestsAllowed")) {
                    AREGUESTSALLOWED = Boolean.parseBoolean(data.split("=")[1]);
                    completedChecks++;
                }
                if (data.split("=")[0].equals("capchaAmount")) {
                    CAPCHAAMOUNT = Integer.parseInt(data.split("=")[1]);
                    completedChecks++;
                }
                if (data.split("=")[0].equals("databaseIp")) {
                    DATABASEIP = data.split("=")[1];
                    completedChecks++;
                }
                if (data.split("=")[0].equals("databaseUser")) {
                    DATABASEUSER = data.split("=")[1];
                    completedChecks++;
                }
                if (data.split("=")[0].equals("databasePassword")) {
                    DATABASEPASSWORD = data.split("=")[1];
                    completedChecks++;
                }
                if (data.split("=")[0].equals("useEncryption")) {
                    USEENCRYPTION = Boolean.parseBoolean(data.split("=")[1]);
                    completedChecks++;
                }
                if (data.split("=")[0].equals("encryptionKey")) {
                    ENCRYPTIONKEY = data.split("=")[1];
                    completedChecks++;
                }
                if (data.split("=")[0].equals("openingHour")) {
                    OPENINGHOUR = Integer.parseInt(data.split("=")[1]);
                    completedChecks++;
                }
                if (data.split("=")[0].equals("closingHour")) {
                    CLOSINGHOUR = Integer.parseInt(data.split("=")[1]);
                    completedChecks++;
                }
            }

            if(completedChecks!=13) {throw new Exception();}

            sc.close();
        } catch (Exception e) {
            new ConfigReadErrorPage();
            return false;
        }
        return true;
    }

    public static ArrayList<Credential> GetCredentialsFromDatabase(){
        ArrayList<Credential> credentials = new ArrayList<>();

        try{
            Statement statement = CONNECTION.createStatement();
            ResultSet resultSet = statement.executeQuery("select userId, name, email, password, type from credentials");

            while (resultSet.next()) {
                credentials.add(new Credential(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5)));
            }
        }
        catch (Exception ex) {System.out.println("uhoh");}

        return  credentials;
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

    public static ArrayList<Reservation> GetPendingReservationsFromDatabase(){
        ArrayList<Reservation> reservations = new ArrayList<>();

        try{
            Statement statement = CONNECTION.createStatement();
            ResultSet resultSet = statement.executeQuery("select reservationId, time, name, notes, tableId from pendingReservations");

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
        System.out.println(filePath.split("Main.java")[0]);

        if(!getConfig())return;

        try {
           // Class.forName("com.mysql.cj.jdbc.Driver");
            CONNECTION = DriverManager.getConnection("jdbc:mysql://"+DATABASEIP,DATABASEUSER, DATABASEPASSWORD);
        }
        catch (Exception e) {
            new ConnectionErrorPage();
            return;
        }

        new LoginPage();
    }
}