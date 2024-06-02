package arg.hozocabby.managers;

import arg.hozocabby.database.entities.Account;
import arg.hozocabby.database.Database;

import java.util.Optional;

public class AuthenticationManager {
    Database db;

    public AuthenticationManager(Database db) {
        this.db = db;
    }

    public Optional<Account> getUser(String name) {
        return Optional.ofNullable(db.Accounts.get(name));
    }


    public Account login(String phone, String password, Account.UserType type) throws IllegalArgumentException{


        Optional<Account> loggedInUser = getUser(phone);
        if(loggedInUser.isEmpty()) {
            throw new IllegalArgumentException("NO SUCH USER PRESENT");
        }


        if(! loggedInUser.get().getPassword().equals(password)){
            throw new IllegalArgumentException("INCORRECT PASSWORD");
        }

        if(!loggedInUser.get().getType().equals(type)) {
            throw new IllegalArgumentException("USER IS NOT AUTHORIZED");
        }

        return loggedInUser.get();
    }

    public Account register(String name, String phone, String address, Account.UserType type, String password) throws IllegalArgumentException{
        if(getUser(phone).isPresent()) {
            throw new IllegalArgumentException("USER ALREADY EXISTS");
        }

        Account regAccount = new Account(db.Accounts.size(), name, phone, address, password, type);

        db.Accounts.put(phone, regAccount);

        return regAccount;
    }
}
