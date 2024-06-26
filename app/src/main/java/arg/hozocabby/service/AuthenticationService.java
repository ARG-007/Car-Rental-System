package arg.hozocabby.service;

import arg.hozocabby.database.AccountDataAccess;
import arg.hozocabby.entities.Account;
import arg.hozocabby.exceptions.DataAccessException;
import arg.hozocabby.exceptions.DataSourceException;

import java.util.Optional;

public class AuthenticationService {
    AccountDataAccess accountDataAccess;

    AuthenticationService(AccountDataAccess accountDataAccess) {
        this.accountDataAccess = accountDataAccess;
    }

    public Account login(String phone, String password, Account.UserType type) throws IllegalArgumentException, DataSourceException, DataAccessException {

        Optional<Account> loggedInUser = accountDataAccess.getAccountByMobile(phone);
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

    public Account register(String name, String phone, String address, Account.UserType type, String password) throws IllegalArgumentException, DataSourceException, DataAccessException{
        if(accountDataAccess.containsAccount(phone)) {
            throw new IllegalArgumentException("USER ALREADY EXISTS");
        }

        if(password == null || password.isEmpty()){
            throw new IllegalArgumentException("EMPTY_PASSWORD");
        }

        return accountDataAccess.addAccount(name, phone, address, password, type);
    }
}
