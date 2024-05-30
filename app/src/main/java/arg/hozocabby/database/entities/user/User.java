package arg.hozocabby.database.entities.user;

public abstract class User {
    protected int id;
    protected String name, address;
    protected String phone;
    protected UserType type;
    protected String password;

    protected User(int id, String name,String address, String phone, String password ){
        this.id=id;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public UserType getType(){
        return type;
    }
}
