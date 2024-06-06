package arg.hozocabby.views.console;

import arg.hozocabby.entities.RentalInfo;
import arg.hozocabby.entities.Account;
import arg.hozocabby.service.CustomerService;

public class CustomerMenu extends Console{
    private final Account customer;

    private CustomerService cs;

    private final RentalInfo rental = new RentalInfo();

    private final Menu customerActionsMenu = new Menu();
    private final Menu rentalMenu = new Menu();

    public CustomerMenu(Account cus, CustomerService cs){
        this.customer = cus;

        customerActionsMenu
            .setOuterSeparator('~')
            .setInnerSeparator('_')
            .setTitle(
                String.format(
                    "Role: Customer\tLogged In As: %s\tID: %d\n\tChoose Action To Proceed",
                    customer.getName(), customer.getId()
                )
            )
            .addOption("Rent", "View Rentals", "Cancel Rental", "Show Menu", "Exit")
            .setPrompt("Enter Your Choice: ");

        rentalMenu
            .setOuterSeparator('@')
            .setInnerSeparator('─')
            .setTitle("Rental Menu, Set/Change Fields")
            .addOption("Set Pickup", "Set Destination", "Set Car Type", "Set Pickup Time")
            .addOption("Calculate Fare", "Confirm", "Exit")
            .setPrompt("Enter Your Choice: ");

    }


    public void display() {
        int choice;

        while(true){
            choice = customerActionsMenu.process();
            switch(choice){
                case 1: rentMenu(); break;
                case 2: rentalHistory(); break;
                case 3: cancelRental();break;
                case 4: break;
                case 5: return;
            }
        }
    }



    void rentMenu(){

        int choice;

        do {
            choice = rentalMenu.process();
            separator('-');

            switch(choice) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    return;
            }

        } while(choice != 7);


    }

    void rentalHistory(){

    }

    void cancelRental(){

    }



}
