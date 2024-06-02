package arg.hozocabby.database.entities;

import java.util.Arrays;
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
        ADMIN(1),
        CUSTOMER(2),
        OWNER(3),
        DRIVER(4);

        private final int ordinal;
        private final static UserType[] values;

        static {
            values = values();
        }

        UserType(int ord){
            ordinal = ord;
        }

        public int getOrdinal(){
            return ordinal;
        }


        public static UserType valueOf(int ordinal) throws NoSuchElementException {
            return Arrays.stream(values).findFirst().filter(e -> e.getOrdinal()==ordinal).orElseThrow(()-> new NoSuchElementException("Enum Has No Constant With That Ordinal"));
        }

        public static int size(){
            return values.length;
        }
    }
}
