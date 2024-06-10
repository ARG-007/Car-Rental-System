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


    @Override
    public void display() throws DataSourceException {
        while(true) {
            clearScreen();
            switch (driverMenu.process()){
                case 1:
                    List<Rental> currentAssignment = drs.getPendingRentals(driver);

                    if(currentAssignment.isEmpty()){
                        System.out.println("You Have No Pending Assignments");
                    } else {
                        printRentals(currentAssignment);
                    }


                    input("Press Enter To Continue.....");

                    break;
                case 2:
                    List<Rental> completedAssignment = drs.getCompletedRentals(driver);

                    if(completedAssignment.isEmpty()){
                        System.out.println("You Have No Pending Assignments");
                    } else {
                        printRentals(completedAssignment);
                    }


                    input("Press Enter To Continue.....");

                    break;
                case 3:
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

                    input("Press Enter To Continue.....");

                    break;
                case 4:
                    List<Rental> assignmentHistory = drs.getAssignedRentals(driver);
                    if(assignmentHistory.isEmpty()) {
                        System.out.println("You Have No Assignments, Rest Well");
                    } else {
                        printRentals(assignmentHistory);
                    }
                    input("Press Enter To Continue.....");
                    break;
                case 5:
                    List<Rental> assHistory = drs.getCompletedRentals(driver);
                    if(assHistory.isEmpty()) {
                        System.out.println("You Haven't Completed Assignments So Far");
                        break;
                    } else {
                        System.out.println("You have Completed These Assignments So Far: ");

                        printRentals(assHistory);

                        double commissions = 0;
                        for(Rental r : assHistory){
                            commissions += r.getCost() * .5;
                        }

                        System.out.printf("Which Has A Total Commission of: %.2f ", commissions);
                    }

                    input("Press Enter To Continue.....");

                    break;
                case 6: return;
            }
        }
    }
}
