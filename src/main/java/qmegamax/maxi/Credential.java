package main.java.qmegamax.maxi;

public class Credential {
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
}
