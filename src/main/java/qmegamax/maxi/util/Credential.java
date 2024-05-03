package main.java.qmegamax.maxi.util;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Credential extends DatabaseRow {
    public static enum AccountType {
        USER,ADMIN
    }
    public int id;
    public String name;
    public String email;
    public String password;
    public AccountType type;

    public Credential(int id,String name,String email,String password,String type){
        this.id=id;
        this.name=name;
        this.email=email;
        this.password=password;
        this.type=AccountType.valueOf(type);
    }

    public static Credential getEmpty(){
        return new Credential(0,null,null,null,"USER");
    }

    public Credential insert(ResultSet resultSet){
        try {
            return new Credential(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5));
        } catch (SQLException ignored) {}

        return null;
    }
}
