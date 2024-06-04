package arg.hozocabby.managers;

import arg.hozocabby.database.AccountManager;
import arg.hozocabby.database.entities.Account;

import java.sql.SQLException;
import java.util.Optional;

public class AuthenticationManager {
    AccountManager accountMan;

    public AuthenticationManager(AccountManager accountMan) {
        this.accountMan = accountMan;
    }

    public Account login(String phone, String password, Account.UserType type) throws IllegalArgumentException, SQLException{


        Optional<Account> loggedInUser = accountMan.getAccountByMobile(phone);
        if(loggedInUser.isEmpty()) {
            throw new IllegalArgumentException("NO SUCH USER PRESENT");
        }


        if(! loggedInUser.get().verifyPassword(password)){
            throw new IllegalArgumentException("INCORRECT PASSWORD");
        }

        if(!loggedInUser.get().getType().equals(type)) {
            throw new IllegalArgumentException("USER IS NOT AUTHORIZED");
        }

        return loggedInUser.get();
    }

    public Account register(String name, String phone, String address, Account.UserType type, String password) throws IllegalArgumentException, SQLException{
        if(accountMan.containsAccount(phone)) {
            throw new IllegalArgumentException("USER ALREADY EXISTS");
        }

        return accountMan.createAccount(name, phone, address, password, type);
    }
}
