package main.java.qmegamax.maxi.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class Reservation extends DatabaseRow {
    public int id;
    public LocalDateTime date;
    public String name;
    public String notes;
    public int table;

    public Reservation(int id,LocalDateTime date,String name,String notes,int table){
        this.id=id;
        this.date=date;
        this.name=name;
        this.notes=notes;
        this.table= table;
    }

    public static Reservation getEmpty(){
        return new Reservation(0,null,null,null,0);
    }

    public Reservation insert(ResultSet resultSet){

        try {
            return new Reservation(resultSet.getInt(1),resultSet.getObject(2,LocalDateTime.class),resultSet.getString(3),resultSet.getString(4),resultSet.getInt(5));
        } catch (SQLException ignored) {System.out.println("A");}

        return null;
    }
}
