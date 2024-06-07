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
                .addOption("Pending Assignment", "End Assignment", "Assignment History", "Commissions", "Exit")
                .setPrompt("Enter The Choice: ");

    }


    @Override
    public void display() throws DataSourceException {
        while(true) {
            switch (driverMenu.process()){
                case 1:
                    List<Rental> currentAssignment = drs.getAssignedRentals(driver);
                    if(currentAssignment.isEmpty()) {
                        System.out.println("You Have No Assignments, Rest Well");
                        break;
                    }

                    currentAssignment = currentAssignment.parallelStream().filter(rents->rents.getStatus()== Rental.RentalStatus.PENDING).toList();

                    if(currentAssignment.isEmpty()){
                        System.out.println("You Have No Pending Assignments");
                    }

                    printRentals(currentAssignment);

                    break;
                case 2:
                    List<Rental> assignment = drs.getAssignedRentals(driver);
                    if(assignment.isEmpty()) {
                        System.out.println("You Have No Assignments, Rest Well");
                        break;
                    }

                    assignment = assignment.parallelStream().filter(rent->rent.getStatus()== Rental.RentalStatus.ONGOING).toList();

                    if(assignment.isEmpty()) {
                        System.out.println("You Have No On-Going Assignments");
                        break;
                    }

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

                    break;
                case 3:
                    List<Rental> assignmentHistory = drs.getAssignedRentals(driver);
                    if(assignmentHistory.isEmpty()) {
                        System.out.println("You Have No Assignments, Rest Well");
                        break;
                    }
                    printRentals(assignmentHistory);
                    break;
                case 4:
                    List<Rental> assHistory = drs.getAssignedRentals(driver);
                    if(assHistory.isEmpty()) {
                        System.out.println("You Haven't Got Any Rentals Assigned For You So Far");
                        break;
                    }

                    assHistory = assHistory.parallelStream().filter(rent->rent.getStatus()== Rental.RentalStatus.COMPLETED).toList();

                    System.out.println("You have Completed These Assignments So Far: ");

                    printRentals(assHistory);

                    double commissions = 0;
                    for(Rental r : assHistory){
                        commissions += r.getCost() * .5;
                    }

                    System.out.printf("Which Has A Total Commission of: %.2f ", commissions);
                    break;
                case 5: return;
            }
        }
    }
}
