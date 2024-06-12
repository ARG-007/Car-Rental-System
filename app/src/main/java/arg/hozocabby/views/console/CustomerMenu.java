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

    private final CustomerService customerService;

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
        rentalHistory = customerService.queryRentalHistory(cus);

        customerActionsMenu
            .setOuterSeparator('~')
            .setInnerSeparator('_')
            .setTitle(
                String.format(
                    "Logged In As: %s\t\t\tID: %d\nRole: Customer\t\t\tChoose Action To Proceed",
                    customer.getName(), customer.getId()
                )
            )
            .addOption("Rent", "View Rentals", "Cancel Rental", "Exit")
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
            .addOption("Back")
            .setPrompt("Enter Your Choice: ");

    }


    public void display() throws DataSourceException{
        int choice;

        while(true){
            clearScreen();
            choice = customerActionsMenu.process();
            switch(choice){
                case 1: rentMenu(); break;
                case 2:
                    rentalHistory = customerService.queryRentalHistory(customer);
                    rentalHistory(); break;
                case 3: cancelRental();break;
//                case 4: new GOL().display();break;
                case 4: return;
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

            boolean allFieldsEntered = pickup!=null && destination!=null && vehicleType != null && pickupDate != null && selectedVehicle!=null && wantDriver !=null;

            switch(choice) {
                case 1:
                    pickup = getPlaceInputExcluding("Enter Your Pickup Place: ", destination);
                    rentalMenu.changeOption(1, String.format("Change Pickup [%s]", pickup.name()));
                    break;
                case 2:
                    destination = getPlaceInputExcluding("Enter Your Destination Place: ", pickup);
                    rentalMenu.changeOption(2, String.format("Change Destination [%s]", destination.name()));
                    break;
                case 3:

                    List<Vehicle> vehicles = null;

                    boolean back = false;

                    while(true) {
                        int carType = carTypeSelection.process();

                        if(carType == 5) {
                            back = true;
                            break;
                        }

                        vehicleType = Vehicle.VehicleType.valueOf(carType);
                        vehicles = customerService.queryAvailableVehicleOfType(vehicleType);

                        if(vehicles.isEmpty()){
                            System.out.printf("There Are No %s Type Vehicles Available\nPlease Try Again After Sometime Or Choose Another Type\n", vehicleType);
                            continue;
                        }
                        break;

                    }

                    if(back) break;



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
                            System.out.println("Enter Number Only");
                        } catch (NoSuchElementException e) {
                            System.out.println("Enter Only Shown IDs");
                        }
                    }

                    break;

                case 4:
                    while(true){
                        try {
                            String date = input("Enter Pickup Time [Format: DD-MM-YY : hh-mm](eg: 04-02-69 : 20-30): ");
                            LocalDateTime ldt = LocalDateTime.parse(date, dateFormatter);

                            if(ldt.isBefore(LocalDateTime.now())){
                                System.out.println("The Entered Datetime Is In The Past, Please Enter Time In Future");
                                continue;
                            }

                            pickupDate = Timestamp.valueOf(ldt);


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
                    if(allFieldsEntered) {
                        double distance = pickup.distanceBetween(destination);
                        separator('=');
                        double driverCost = (wantDriver)?distance*2:0;
                        double fareCost = distance*selectedVehicle.getChargePerKm();

                        System.out.printf("Distance      : %.2f KM\n", distance);
                        System.out.printf("Driver Charges: %.2f Rs\n", driverCost);
                        System.out.printf("Fare Cost     : %.2f Rs\n", fareCost);
                        System.out.printf("Total Cost    : %.2f Rs\n", driverCost+fareCost);
                        separator('=');
                        input("Press Enter To Continue.....");
                    } else {
                        System.out.println("Fill All Fields");
                    }

                    break;
                case 7:

                    if(allFieldsEntered) {
                        rental.setRequester(customer);
                        rental.setAssignedVehicle(selectedVehicle);
                        rental.setPickup(pickup);
                        rental.setDestination(destination);
                        rental.setRequestedVehicleType(vehicleType);
                        rental.setPickupTime(pickupDate);

                        boolean retry = true;
                        do {
                            try {
                                currentRental = customerService.bookRent(rental, wantDriver);
                                rentalHistory.add(currentRental);
                                retry = false;
                                break;
                            } catch (NoSuchElementException nse) {
                                System.out.println("No Available Drivers Present, We Will Assign an driver at the time of starting rent");
                                retry = input("Press Enter To Continue Renting Or Any Other Key To Cancel").equals("\r");
                                wantDriver = false;
                                rentalMenu.changeOption(5,"Driver Assignment [NO - NO AVAILABLE DRIVERS]");
                            }
                        } while(retry);

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

    private Place getPlaceInputExcluding(String prompt, Place... exclude) {
        List<Place> validPlaces = places.stream()
                .filter(p -> {
                    for(Place i : exclude) {
                        if(i!=null && p.id().equals(i.id()))
                            return false;
                    }
                    return true;
                })
                .toList();


        int size = validPlaces.size();
        int pivot = size/2;
//        int columnWidth = 25;
//
//
//        for (int i = 0; i < size - pivot; i++) {
//            String col1 = String.format("%2d: %s", validPlaces.get(i).id(), validPlaces.get(i).name());
//
//            int spaces = columnWidth - col1.length();
//
//            System.out.printf(col1);
//            for(int f = 0;f<spaces;f++) System.out.print(" ");
//            if(i+pivot+1 < size) {
//                System.out.printf("%2d: %s", validPlaces.get(i + pivot + 1).id(), validPlaces.get(i + pivot + 1).name());
//            }
//            System.out.println();
//        }

        Table t = new Table("ID", "Name"," ", "ID", "Name");
        for(int i = 0;i <size - pivot; i++) {
            int piv = i+pivot+1;
            boolean within = piv<size;
            t.addRow(validPlaces.get(i).id(), validPlaces.get(i).name(),"", within?validPlaces.get(piv).id():"", within?validPlaces.get(piv).name():"");
        }

        t.display();

        separator('+');

        while(true) {
            try {
                int placeId = integerInput(prompt);

                for(Place p : exclude) {
                    if(p!=null && p.id() == placeId) throw new IllegalArgumentException();
                }

                return validPlaces.parallelStream().filter(place -> place.id().equals(placeId)).findFirst().orElseThrow();
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println("Enter The Number Left of Place Name");
            } catch (NoSuchElementException e) {
                System.out.println("Please Enter Only The ID of The Displayed Places");
            } catch (IllegalArgumentException iae) {
                System.out.println("Destination and Pickup cannot be same");
            }

        }



    }

    private void rentalHistory(){


        Table t = new Table("ID", "Vehicle Name", "Driver Assigned", "Pickup Location", "Destination Location", "Pickup Time", "Cost", "Status");

        for(Rental r : rentalHistory){
            t.addRow(
                r.getId(),
                r.getInfo().getAssignedVehicle().getName(),
                r.getDriver()!=null ? r.getDriver().getName() : "No Driver",
                r.getInfo().getPickup(),
                r.getInfo().getDestination(),
                r.getInfo().getPickupTime(),
                String.format("%.2f",r.getCost()),
                r.getStatus()
            );
        }

        t.display();

        input("Press Enter To Continue");    }

    private void cancelRental() throws DataSourceException{
        System.out.println("Your Pending Rental History Is: ");

        List<Rental> pendingRents = rentalHistory.parallelStream().filter(r->r.getStatus() == Rental.RentalStatus.PENDING).toList();

        if(pendingRents.isEmpty()){
            System.out.println("You Haven't Rented Any Car");

            input("Press Enter To Continue");

            return;
        }


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

        while(true) {
            try {
                int rentId = integerInput("Enter The ID of the Rental you want to cancel: ");

                Rental cancellable = pendingRents.parallelStream().filter(r->r.getId().equals(rentId)).findFirst().orElseThrow();
                boolean cancelled = customerService.cancelRent(cancellable);
                if(cancelled)
                    System.out.println("Cancelled Successfully");
                else
                    System.out.println("Cancellation Failed");

                input("Press Enter To Continue");

                break;
            } catch (NumberFormatException | InputMismatchException ae) {
                System.out.println("Enter Only The ID Number");
            } catch(NoSuchElementException nse){
                System.out.println("Invalid ID, Enter In The ID From Table");
            }
        }

    }



}
