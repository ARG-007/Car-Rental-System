package arg.hozocabby.database.entities.user;

public final class Customer extends User {
    public Customer(int id, String name,String address, String phone, String password ) {
        super(id, name, address, phone, password);

        this.type = UserType.Customer;

    }
}
