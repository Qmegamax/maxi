package main.java.qmegamax.maxi;

import main.java.qmegamax.maxi.pages.errors.ConfigReadErrorPage;
import main.java.qmegamax.maxi.pages.LoginPage;
import main.java.qmegamax.maxi.pages.errors.ConnectionErrorPage;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
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
            }

            if(completedChecks!=11) {throw new Exception();}

            sc.close();
        } catch (Exception e) {
            new ConfigReadErrorPage();
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        File file = new File("Main.java");
        String filePath=file.getAbsolutePath();
        PATH=(filePath.split("Main.java")[0]+"\\src\\main\\java\\qmegamax\\maxi\\");
        System.out.println(filePath.split("Main.java")[0]);

        if(!getConfig())return;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            CONNECTION = DriverManager.getConnection("jdbc:mysql://"+DATABASEIP,DATABASEUSER, DATABASEPASSWORD);
        }
        catch (Exception e) {
            new ConnectionErrorPage();
            return;
        }

        new LoginPage();
    }
}