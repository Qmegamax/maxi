package main.java.qmegamax.maxi.util;

import java.sql.Date;
import java.time.LocalDateTime;

public class Reservation {
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
}
