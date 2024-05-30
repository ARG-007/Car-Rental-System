package arg.hozocabby.database.entities.user;

import java.util.Arrays;
import java.util.Optional;

public enum UserType {
    Customer,
    Owner,
    Driver;

    private final static UserType[] values;

    static {
        values = values();
    }

    public static Optional<UserType> valueOf(int ordinal){
        if(ordinal < 0 || ordinal > size())
            return Optional.empty();
        return Optional.of(values[ordinal]);
    }

    public static int size(){
        return values.length;
    }
}
