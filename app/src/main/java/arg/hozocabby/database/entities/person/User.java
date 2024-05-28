package arg.hozocabby.database.entities.person;

public abstract class User {
    protected int id, age;
    protected String name, address;
    protected String phone;

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }
}
