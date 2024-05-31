package arg.hozocabby.database.entities.user;

import arg.hozocabby.database.entities.Account;

public final class Owner extends Account {
    public Owner(int id, String name, String address, String phone, String password ) {
        super(id, name, address, phone, password, UserType.Owner);
    }
}
