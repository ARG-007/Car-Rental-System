package database.entities.person;

public final class Driver extends User {
    public Driver(int id, int age, String name,String address, String phone ) {
        this.id=id;
        this.age = age;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }
}
