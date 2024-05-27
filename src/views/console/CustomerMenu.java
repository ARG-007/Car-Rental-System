package views.console;

import database.entities.RentalInfo;
import database.entities.person.Customer;

public class CustomerMenu extends Console{
    private Customer customer;

    private RentalInfo rental = new RentalInfo();



    public void showMenu(){
        separator('~');
        System.out.println("1: Rent");
        System.out.println("2: View Rentals");
        System.out.println("3: Pay for Rent");
        System.out.println("4: Show Menu");
        System.out.println("5: Exit");
        separator('~');
    }


    public void display() {
        int choice;
        showMenu();
        while(true){
            choice = integerInput("Enter You Choice [4 to Show Menu] : ");
            separator('-');
            switch(choice){
                case 1: rentMenu(); break;
                case 2: break;
                case 3: break;
                case 4: showMenu(); break;
                case 5: return;
                default: break;
            }
        }
    }



    void rentMenu(){

        int choice;



        do {
            separator('@');
            System.out.println("\t1: Change Pickup");
            System.out.println("\t2: Change Rental Type");
            System.out.println("\t3: Change Destination");
            System.out.println("\t4: Change Car Type");
            System.out.println("\t5: Calculate Fare");
            System.out.println("\t6: Confirm");
            System.out.println("\t7: Exit");
            separator('@');

            choice = integerInput("Enter Your Choice: ");
            separator('-');

            switch(choice) {
                case 1: break;
                case 2: break;
                case 3: break;
                case 4: break;
                case 5: break;
                case 6: break;

                case 7: break;
                default:
                    System.out.println("Enter an Correct Choice!");
            }

        } while(choice != 7);


    }

    boolean validateInput(){


        return false;
    }

    void setPickup(){

    }

    void setRentalType(){

    }


}
