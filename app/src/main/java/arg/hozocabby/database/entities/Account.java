package arg.hozocabby.database.entities;

import java.util.NoSuchElementException;

public class Account {
    protected int id;
    protected String name, address;
    protected String phone;
    protected UserType type;
    protected String password;

    public Account(int id, String name, String address, String phone, String password, UserType type ){
        this.id=id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.password = password;
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

    public enum UserType {
        Customer,
        Owner,
        Driver;

        private final static UserType[] values;

        static {
            values = values();
        }

        public static UserType valueOf(int ordinal) throws NoSuchElementException {
            if(ordinal < 0 || ordinal > size())
                throw new NoSuchElementException("Enum Has No Constant With That Ordinal");
            return values[ordinal];
        }

        public static int size(){
            return values.length;
        }
    }
}
