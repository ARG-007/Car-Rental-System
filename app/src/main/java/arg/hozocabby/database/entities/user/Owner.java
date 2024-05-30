package arg.hozocabby.database.entities.user;

public final class Owner extends User {
    public Owner(int id, String name, String address, String phone, String password ) {
        super(id, name, address, phone, password);

        this.type = UserType.Owner;
    }
}
