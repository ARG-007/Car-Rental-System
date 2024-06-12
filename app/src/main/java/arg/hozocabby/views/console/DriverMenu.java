package arg.hozocabby.views.console;

import arg.hozocabby.entities.Account;
import arg.hozocabby.entities.Rental;
import arg.hozocabby.exceptions.DataSourceException;
import arg.hozocabby.service.DriverService;

import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;

public class DriverMenu extends Console{

    private final Account driver;
    private final DriverService drs;

    private final Menu driverMenu = new Menu();

    public DriverMenu(Account account, DriverService drs) {
        this.driver = account;
        this.drs = drs;

        driverMenu
                .setInnerSeparator('%')
                .setOuterSeparator('=')
                .setTitle(String.format("Logged In As: %20s\nID: %3d\nDriver Menu", account.getName(), account.getId()))
                .addOption("Pending Assignment", "Completed Assignments", "End Assignment", "Assignment History", "Commissions", "Exit")
                .setPrompt("Enter The Choice: ");

    }

    protected void printRentals(List<Rental> rentals) {
        Table t = new Table("ID", "Vehicle Name", "Pickup Location", "Destination Location", "Pickup Time", "Cost", "Status");

        for(Rental r : rentals){
            t.addRow(
                    r.getId(),
                    r.getInfo().getAssignedVehicle().getName(),
                    r.getInfo().getPickup(),
                    r.getInfo().getDestination(),
                    r.getInfo().getPickupTime(),
                    String.format("%.2f",r.getCost()),
                    r.getStatus()
            );
        }

        t.display();
    }


    @Override
    public void display() throws DataSourceException {
        while(true) {
            clearScreen();
            switch (driverMenu.process()){
                case 1:
                    separator('#');
                    List<Rental> currentAssignment = drs.getPendingRentals(driver);

                    if(currentAssignment.isEmpty()){
                        System.out.println("You Have No Pending Assignments");
                    } else {
                        printRentals(currentAssignment);
                    }

                    separator('-');
                    input("Press Enter To Continue.....");

                    break;
                case 2:
                    separator('@');
                    List<Rental> completedAssignment = drs.getCompletedRentals(driver);

                    if(completedAssignment.isEmpty()){
                        System.out.println("You Have Not Completed Any Assignments");
                    } else {
                        printRentals(completedAssignment);
                    }

                    separator('=');
                    input("Press Enter To Continue.....");

                    break;
                case 3:
                    separator('$');
                    List<Rental> assignment = drs.getOngoingRentals(driver);

                    if(assignment.isEmpty()) {
                        System.out.println("You Have No On-Going Assignments");
                    } else {
                        printRentals(assignment);

                        try {
                            int rentId = integerInput("Enter The ID of the Rental you want to End: ");

                            Rental endable = assignment.parallelStream().filter(r->r.getId().equals(rentId)).findFirst().orElseThrow();
                            if(drs.endRental(driver, endable)){
                                System.out.println("Rental Completed Successfully");
                            } else{
                                System.out.println("Due To Internal Error It can not be completed, Try Again");
                            }
                        } catch (NumberFormatException | InputMismatchException ae) {
                            System.out.println("Enter Only The ID Number");
                        } catch(NoSuchElementException nse){
                            System.out.println("Invalid ID, Enter In The ID From Table");
                        }
                    }
                    separator('%');
                    input("Press Enter To Continue.....");

                    break;
                case 4:
                    separator('&');
                    List<Rental> assignmentHistory = drs.getAssignedRentals(driver);
                    if(assignmentHistory.isEmpty()) {
                        System.out.println("You Have No Assignments, Rest Well");
                    } else {
                        printRentals(assignmentHistory);
                    }
                    separator('%');
                    input("Press Enter To Continue.....");
                    break;
                case 5:
                    separator('^');
                    List<Rental> assHistory = drs.getCompletedRentals(driver);
                    if(assHistory.isEmpty()) {
                        System.out.println("You Haven't Completed Assignments So Far");
                    } else {
                        System.out.println("You have Completed These Assignments So Far: ");

                        printRentals(assHistory);

                        double commissions = 0;
                        for(Rental r : assHistory){
                            commissions += r.getCost() * .5;
                        }

                        System.out.printf("Which Has A Total Commission of: %.2f ", commissions);
                    }

                    separator('%');

                    input("Press Enter To Continue.....");

                    break;
                case 6: return;
            }
        }
    }
}
