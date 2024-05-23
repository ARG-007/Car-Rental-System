package views.console;

import entities.person.Customer;
import entities.vehicle.VehicleType;

public class CustomerMenu extends Console{
    private Customer customer;

    private String pickup, destination;
    private int rentType;
    private VehicleType vt;


    public void showMenu(){
        separator('~');
        System.out.println("1: Set Pickup");
        System.out.println("2: Set Rental Type");
        System.out.println("3: Set Destination");
        System.out.println("4: Calculate Fare");
        System.out.println("5: Select Car Type");
        System.out.println("6: View Bookings");
        System.out.println("7: Pay for Rent");
        System.out.println("8: Show Menu");
        System.out.println("9: Exit");
        separator('~');
    }



    public void display() {
        showMenu();
        while(true){
            switch(integerInput("Enter You Choice [8 to Show Menu] : ")){
                case 1: break;
                case 2: break;
                case 3: break;
                case 4: break;
                case 5: break;
                case 6: break;
                case 7: break;
                case 8: break;
                case 9: return;
                default: break;
            }
            separator('-');
        }
    }

    public void setCustomer() {
        this.customer = customer;
    }

    public void setPickup() {
        this.pickup = pickup;
    }

    public void setDestination() {
        this.destination = destination;
    }

    public void setRentType() {
        this.rentType = rentType;
    }

    public void setVt() {
        this.vt = vt;
    }
}
