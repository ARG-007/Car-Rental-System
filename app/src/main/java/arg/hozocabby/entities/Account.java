package arg.hozocabby.entities;

import java.util.Arrays;
import java.util.NoSuchElementException;

public class Account {
    private final Integer id;
    private final String name, address;
    private final String phone;
    private final UserType type;
    private final String password;

    public Account(int id, String name, String address, String phone, String password, UserType type ){
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.password = password;
        this.type = type;
    }

    public Integer getId() {
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

    public Boolean verifyPassword(String password){
        return this.password.equals(password);
    }

    public UserType getType(){
        return type;
    }

    public enum UserType {
        CUSTOMER(1),
        OWNER(2),
        DRIVER(3),
        ADMIN(4);

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
            return Arrays.stream(values).filter(e -> e.getOrdinal()==ordinal).findFirst().orElseThrow(()-> new NoSuchElementException("Enum Has No Constant With That Ordinal"));
        }

        public static int size(){
            return values.length;
        }
    }
}
