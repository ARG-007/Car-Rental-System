package entities.person;

public final class Customer extends Person{
    public Customer(int id, int age, String name,String address, String phone ) {
        this.id=id;
        this.age = age;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }
}
