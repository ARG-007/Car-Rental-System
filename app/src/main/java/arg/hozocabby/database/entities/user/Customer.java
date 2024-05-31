package arg.hozocabby.database.entities.user;

import arg.hozocabby.database.entities.Account;

public final class Customer extends Account {
    public Customer(int id, String name,String address, String phone, String password ) {
        super(id, name, address, phone, password, UserType.Customer);

    }
}
