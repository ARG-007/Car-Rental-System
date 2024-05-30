package arg.hozocabby.managers;

import arg.hozocabby.database.entities.user.*;
import arg.hozocabby.database.Database;

import java.util.Optional;

public class AuthenticationManager {
    Database db;

    public AuthenticationManager(Database db) {
        this.db = db;
    }

    public Optional<User> getUser(String name) {
        return Optional.ofNullable(db.users.get(name));
    }


    public User login(String phone, String pass, UserType type) throws IllegalArgumentException{


        Optional<User> loggedInUser = getUser(phone);
        if(loggedInUser.isEmpty()) {
            throw new IllegalArgumentException("NO SUCH USER PRESENT");
        }


        if(! loggedInUser.get().getPassword().equals(pass)){
            throw new IllegalArgumentException("INCORRECT PASSWORD");
        }

        if(!loggedInUser.get().getType().equals(type)) {
            throw new IllegalArgumentException("USER IS NOT AUTHORIZED");
        }

        return loggedInUser.get();
    }

    public User register(String name, String phone, String address, UserType type, String password) throws IllegalArgumentException{
        if(getUser(phone).isPresent()) {
            throw new IllegalArgumentException("USER ALREADY EXISTS");
        }

        User regUser = switch(type) {
            case Customer -> new Customer(db.users.size(), name, phone, address, password);
            case Owner -> new Owner(db.users.size(), name, phone, address, password);
            case Driver -> new Driver(db.users.size(), name, phone, address, password);
        };

        db.users.put(phone, regUser);

        return regUser;
    }
}
