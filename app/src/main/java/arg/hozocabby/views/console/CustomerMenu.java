package arg.hozocabby.views.console;

import arg.hozocabby.entities.*;
import arg.hozocabby.exceptions.DataSourceException;
import arg.hozocabby.service.CustomerService;

import java.sql.Timestamp;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CustomerMenu extends Console{
    private final Account customer;

    private CustomerService customerService;

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yy : HH-mm");

    private Rental currentRental = null;
    private RentalInfo rental = new RentalInfo();
    private final List<Place> places;
    private List<Rental> rentalHistory;

    private final Menu customerActionsMenu = new Menu();
    private final Menu rentalMenu = new Menu();
    private final Menu carTypeSelection = new Menu();

    public CustomerMenu(Account cus, CustomerService customerService) throws DataSourceException{
        this.customer = cus;

        this.customerService = customerService;

        places = customerService.queryReachablePlaces();
        rentalHistory = customerService.queryRentalHistory(cus.getId());

        customerActionsMenu
            .setOuterSeparator('~')
            .setInnerSeparator('_')
            .setTitle(
                String.format(
                    "Logged In As: %s\t\t\tID: %d\nRole: Customer\t\t\tChoose Action To Proceed",
                    customer.getName(), customer.getId()
                )
            )
            .addOption("Rent", "View Rentals", "Cancel Rental", "Show Menu", "Exit")
            .setPrompt("Enter Your Choice: ");

        rentalMenu
            .setOuterSeparator('@')
            .setInnerSeparator('─')
            .setTitle("Rental Menu, Set/Change Fields")
            .addOption("Set Pickup", "Set Destination", "Set Car Type", "Set Pickup Time", "Driver Assignment")
            .addOption("Calculate Fare", "Confirm", "Exit")
            .setPrompt("Enter Your Choice: ");

        carTypeSelection
            .setOuterSeparator('=')
            .setInnerSeparator('-')
            .setTitle("Select Car Type")
            .addOption(Vehicle.VehicleType.values())
            .setPrompt("Enter Your Choice: ");

    }


    public void display() throws DataSourceException{
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



    private void rentMenu()  throws DataSourceException {

        int choice;

        Place pickup = null, destination = null;
        Vehicle selectedVehicle = null;
        Vehicle.VehicleType vehicleType = null;
        Timestamp pickupDate = null;
        Boolean wantDriver = null;

        RentalInfo info = new RentalInfo();

        while(true) {
            clearScreen();
            choice = rentalMenu.process();
            separator('-');

            switch(choice) {
                case 1:
                    pickup = getPlaceInputExcluding("Enter Your Pickup Place: ", pickup, destination);
                    rentalMenu.changeOption(1, String.format("Change Pickup [%s]", pickup.name()));
                    break;
                case 2:
                    destination = getPlaceInputExcluding("Enter Your Destination Place: ", pickup, destination);
                    rentalMenu.changeOption(2, String.format("Change Destination [%s]", destination.name()));
                    break;
                case 3:
                    int carType = carTypeSelection.process();
                    vehicleType = Vehicle.VehicleType.valueOf(carType);
                    List<Vehicle> vehicles = customerService.queryAvailableVehicleOfType(vehicleType);

                    separator('=');

                    System.out.println("Vehicles Available Of This Type Are: ");

                    separator('-');

                    Table t = new Table("Id", "Vehicle Name", "Charge Per Km", "Fuel Type");

                    for(Vehicle v : vehicles) {
                        t.addRow(v.getId(), v.getName(), v.getChargePerKm(), v.getFuelType());
                    }
                    t.display();


                    separator('=');

                    while(true) {
                        try {
                            int vId = integerInput("Enter The Id of the vehicle you chose: ");
                            selectedVehicle = vehicles.parallelStream().filter( vt -> vt.getId()==vId).findFirst().orElseThrow();
                            rentalMenu.changeOption(3, String.format("Change Car Type [%s : %s]", vehicleType, selectedVehicle.getName()));
                            break;
                        } catch (InputMismatchException | NumberFormatException e) {
                            System.out.println("Enter an valid");
                        } catch (NoSuchElementException e) {
                            System.out.println("Enter Only Shown IDs");
                        }
                    }

                    break;

                case 4:
                    while(true){
                        try {
                            String date = input("Enter Pickup Time [Format: DD-MM-YY : hh-mm]: ");
                            pickupDate = Timestamp.valueOf(LocalDateTime.parse(date, dateFormatter));
                            rentalMenu.changeOption(4, String.format("Change Pickup Time [%s]", date));
                            break;
                        } catch (DateTimeException pe){
                            System.out.println("Enter in Correct Format: [DD-MM-YY : hh-mm]");
                        }
                    }
                    break;

                case 5:
                    while(true) {
                        String dc= input("Do You Want An Driver To Be Assigned For The Vehicle (Yes/No): ").toLowerCase();
                        if(dc.equals("y") || dc.equals("yes")){
                            wantDriver = true;
                        } else if(dc.equals("n") || dc.equals("no")) {
                            wantDriver = false;
                        } else {
                            System.out.println("Enter Either 'Yes' or 'No'");
                            continue;
                        }
                        rentalMenu.changeOption(5, String.format("Driver Assignment [%s]", (wantDriver)?"YES":"NO"));
                        break;
                    }

                    break;
                case 6:
                    if(pickup!=null && destination!=null && vehicleType != null && pickupDate != null && selectedVehicle!=null && wantDriver !=null) {
                        double distance = pickup.distanceBetween(destination);
                        System.out.printf("Distance: %.2f KM\n", distance);
                        System.out.printf("Fare Cost: %.2f \n", distance* selectedVehicle.getChargePerKm());
                    } else {
                        System.out.println("Fill All Fields");
                    }

                    break;
                case 7:

                    if(pickup!=null && destination!=null && vehicleType != null && pickupDate != null && selectedVehicle!=null && wantDriver!=null) {
                        rental.setRequester(customer);
                        rental.setAssignedVehicle(selectedVehicle);
                        rental.setPickup(pickup);
                        rental.setDestination(destination);
                        rental.setRequestedVehicleType(vehicleType);
                        rental.setPickupTime(pickupDate);

                        currentRental = customerService.bookRent(rental);
                        rentalHistory.add(currentRental);

                    } else {
                        System.out.println("Fill All Fields");
                        break;
                    }


                case 8:

                    rentalMenu
                        .changeOption(1, "Set Pickup")
                        .changeOption(2, "Set Destination")
                        .changeOption(3, "Set Car Type")
                        .changeOption(4, "Set Pickup Time")
                        .changeOption(5, "Driver Assignment");

                    return;
            }
        }


    }

    private void printPlaces(List<Place> places){
        int size = places.size();
        int pivot = size/2;

        for (int i = 0; i < size - pivot; i++) {
            System.out.printf("%d: %s\t\t\t\t", places.get(i).id(), places.get(i).name());
            if(i+pivot+1 < size) System.out.printf("%d: %s", places.get(i+pivot+1).id(), places.get(i+pivot+1).name());
            System.out.println();
        }
    }

    private Place getPlaceInputExcluding(String prompt, Place... Exclude) {
        List<Place> validPlaces = places.stream()
                .filter(p -> {
                    for(Place i : Exclude) {
                        if(i!=null && p.id().equals(i.id()))
                            return false;
                    }
                    return true;
                })
                .toList();

        int size = validPlaces.size();
        int pivot = size/2;
        int columnWidth = 25;

        for (int i = 0; i < size - pivot; i++) {
            String col1 = String.format("%2d: %s", validPlaces.get(i).id(), validPlaces.get(i).name());

            int spaces = 25 - col1.length();

            System.out.printf(col1);
            for(int f = 0;f<spaces;f++) System.out.print(" ");
            if(i+pivot+1 < size)
                System.out.printf("%2d: %s", validPlaces.get(i+pivot+1).id(), validPlaces.get(i+pivot+1).name());
            System.out.println();
        }

        separator('+');

        while(true) {
            try {
                int placeId = integerInput(prompt);
                return validPlaces.parallelStream().filter(place -> place.id().equals(placeId)).findFirst().orElseThrow();
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println("Enter The Number Left of Place Name");
            } catch (NoSuchElementException e) {
                System.out.println("Enter An Valid Place Number");
            }

        }



    }

    private void rentalHistory() throws DataSourceException{

        Table t = new Table("ID", "Vehicle Name", "Pickup Location", "Destination Location", "Pickup Time", "Cost", "Status");

        for(Rental r : rentalHistory){
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

    private void cancelRental() throws DataSourceException{
        System.out.println("Your Pending Rental History Is: ");

        List<Rental> pendingRents = rentalHistory.parallelStream().filter(r->r.getStatus() == Rental.RentalStatus.PENDING).toList();

        Table t = new Table("ID", "Vehicle Name", "Pickup Location", "Destination Location", "Pickup Time", "Cost", "Status");


        for(Rental r : pendingRents){
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


        try {
            int rentId = integerInput("Enter The ID of the Rental you want to cancel: ");

            Rental cancellable = pendingRents.parallelStream().filter(r->r.getId().equals(rentId)).findFirst().orElseThrow();
            customerService.cancelRent(cancellable);
        } catch (NumberFormatException | InputMismatchException ae) {
            System.out.println("Enter Only The ID Number");
        } catch(NoSuchElementException nse){
            System.out.println("Invalid ID, Enter In The ID From Table");
        }

    }



}
