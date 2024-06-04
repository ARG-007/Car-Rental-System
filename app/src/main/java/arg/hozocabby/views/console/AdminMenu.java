package arg.hozocabby.views.console;

public class AdminMenu extends Console{

    public void display(){
        clearScreen();
        separator('X');
        System.out.println("1: View Accounts");
        System.out.println("2: View Rentals");
        System.out.println("3: View Vehicles");
        System.out.println("4: View Places");
        System.out.println("5: Add Place");
        System.out.println("6: View HozoCabby Earnings");
        separator('X');

    }
}
