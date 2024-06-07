package arg.hozocabby.views.console;

import arg.hozocabby.entities.*;
import arg.hozocabby.exceptions.DataSourceException;
import arg.hozocabby.service.CustomerService;

import java.sql.Timestamp;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class CustomerMenu extends Console{
    private final Account customer;

    private CustomerService customerService;

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yy : HH-mm");

    private final RentalInfo rental = new RentalInfo();
    private final List<Place> places;
    private final Menu customerActionsMenu = new Menu();
    private final Menu rentalMenu = new Menu();
    private final Menu carTypeSelection = new Menu();

    public CustomerMenu(Account cus, CustomerService customerService) throws DataSourceException{
        this.customer = cus;

        this.customerService = customerService;

        places = customerService.queryReachablePlaces();

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
            .addOption("Set Pickup", "Set Destination", "Set Car Type", "Set Pickup Time")
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



    void rentMenu()  throws DataSourceException {

        int choice;

        List<Place> placeList = customerService.queryReachablePlaces();

        Place pickup = null, destination = null;
        Vehicle selectedVehicle = null;
        Vehicle.VehicleType vehicleType = null;
        Timestamp pickupDate = null;

        RentalInfo info = new RentalInfo();

        while(true) {
            clearScreen();
            choice = rentalMenu.process();
            separator('-');

            switch(choice) {
                case 1:
                    if(destination != null) {
                        Place finalDestination = destination;
                        printPlaces(
                            placeList
                            .parallelStream()
                            .filter((exPlace)->!exPlace.id().equals(finalDestination.id()))
                            .toList()
                        );
                    }
                    else
                        printPlaces(placeList);
                    separator('─');
                    while(true){
                        try{
                            int place = integerInput("Pickup From Places Above[Enter Number]: ");
                            System.out.println(place);

                            Optional<Place> p;
                            p = places
                                    .stream()
                                    .filter((op)->op.id()==place)
                                    .findFirst();



                            if(p.isEmpty())
                                throw new NoSuchElementException();

                            pickup = p.get();

                            rentalMenu.changeOption(1, String.format("Change Pickup [%s]", pickup.name()));
                            break;
                        } catch (InputMismatchException | NumberFormatException e) {
                            System.out.println("Enter The Number Left of Place Name");
                        } catch (NoSuchElementException e) {
                            System.out.println("Enter An Valid Place Number");
                        } finally {
                            separator('+');
                        }

                    }
                    break;
                case 2:
                    if(pickup != null) {
                        Place finalPickup = pickup;
                        printPlaces(
                            placeList
                                .stream()
                                .filter((exPlace)->!exPlace.id().equals(finalPickup.id()))
                                .toList()
                        );

                    }
                    else
                        printPlaces(placeList);
                    separator('─');
                    while(true){
                        try{
                            int place = integerInput("Pickup From Places Above[Enter Number]: ");

                            System.out.println(place);

                            Optional<Place> p;
                            p = places
                                    .stream()
                                    .filter((op)->op.id()==place)
                                    .findFirst();


                            if(p.isEmpty())
                                throw new NoSuchElementException();

                            destination = p.get();

                            rentalMenu.changeOption(2, String.format("Change Destination [%s]", destination.name()));
                            break;
                        } catch (InputMismatchException | NumberFormatException e) {
                            System.out.println("Enter The Number Left of Place Name");
                        } catch (NoSuchElementException e) {
                            System.out.println("Enter An Valid Place Number");
                        } finally {
                            separator('+');
                        }
                    }
                    break;
                case 3:
                    int carType = carTypeSelection.process();
                    vehicleType = Vehicle.VehicleType.valueOf(carType);
                    List<Vehicle> vehicles = customerService.queryVehicleOfType(vehicleType);

                    separator('=');

                    System.out.println("Vehicles Available Of This Type Are: ");

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
                            rentalMenu.changeOption(4, String.format("Change Pickup Time [%s]:", date));
                            break;
                        } catch (DateTimeException pe){
                            System.out.println("Enter in Correct Format: [DD-MM-YY : hh-mm]");
                        }
                    }
                    break;
                case 5:
                    if(pickup!=null && destination!=null && vehicleType != null && pickupDate != null && selectedVehicle!=null) {
                        double distance = pickup.distanceBetween(destination);
                        System.out.println("Distance: " + distance + " KM");
                        System.out.println("Fare Cost: " + distance*2.5);
                    } else {
                        System.out.println("Fill All Fields");
                    }

                    break;
                case 6:

                    if(pickup!=null && destination!=null && vehicleType != null && pickupDate != null && selectedVehicle!=null) {
                        rental.setRequester(customer);
                        rental.setAssignedVehicle(selectedVehicle);
                        rental.setPickup(pickup);
                        rental.setDestination(destination);
                        rental.setRequestedVehicleType(vehicleType);
                        rental.setPickupTime(pickupDate);

                        customerService.bookRent(rental);


                        rentalMenu
                                .changeOption(1, "Set Pickup")
                                .changeOption(2, "Set Destination")
                                .changeOption(3, "Set Car Type")
                                .changeOption(4, "Set Pickup Time");
                        return;
                    } else {
                        System.out.println("Fill All Fields");
                    }


                    break;
                case 7:

                    rentalMenu
                        .changeOption(1, "Set Pickup")
                        .changeOption(2, "Set Destination")
                        .changeOption(3, "Set Car Type")
                        .changeOption(4, "Set Pickup Time");

                    return;
            }
        }


    }

    void printPlaces(List<Place> places){
        int size = places.size();
        int pivot = size/2;

        for (int i = 0; i < size - pivot; i++) {
            System.out.printf("%d: %s\t\t\t\t", places.get(i).id(), places.get(i).name());
            if(i+pivot+1 < size) System.out.printf("%d: %s", places.get(i+pivot+1).id(), places.get(i+pivot+1).name());
            System.out.println();
        }
    }

    void rentalHistory() throws DataSourceException{
        List<Rental> rentals = customerService.queryRentalHistory(customer.getId());

        Table t = new Table("ID", "Vehicle Name", "Pickup Location", "Destination Location", "Pickup Time", "Cost", "Status");

        for(Rental r : rentals){
            t.addRow(r.getId(), r.getInfo().getAssignedVehicle().getName(), r.getInfo().getPickup(), r.getInfo().getDestination(), r.getInfo().getPickupTime(), r.getCost(), r.getStatus());
        }

        t.display();
    }

    void cancelRental() throws DataSourceException{
        List<Rental> rentals = customerService.queryRentalHistory(customer.getId());

        rentalHistory();


    }



}
