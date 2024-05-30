package arg.hozocabby.database.entities.user;

public final class Driver extends User {
    public Driver(int id, String name,String address, String phone, String password ) {
        super(id, name, address, phone, password);

        this.type = UserType.Driver;
    }
}
