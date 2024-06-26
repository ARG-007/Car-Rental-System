package arg.hozocabby.views.console;

import arg.hozocabby.entities.Account;
import arg.hozocabby.entities.Vehicle;
import arg.hozocabby.exceptions.DataSourceException;
import arg.hozocabby.service.OwnerService;
import arg.hozocabby.util.Validation;

import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;

public class OwnerMenu extends Console{

    private final Account owner;
    private final OwnerService ors;
    private final Table vehicleTable;



    private final Menu ownerMenu = new Menu();
    private final Menu typeMenu = new Menu();
    private final Menu fuelMenu = new Menu();

    public OwnerMenu(Account owner, OwnerService ors) {
        this.owner = owner;
        this.ors = ors;

        ownerMenu
            .setOuterSeparator('^')
            .setInnerSeparator('>')
            .setTitle(String.format("Logged In As: %20s\nID: %5d\n%-10s", owner.getName(), owner.getId(), "Owner Menu"))
            .setPrompt("Enter Your Choice: ")
            .addOption("View Fleet", "Add Car To Fleet", "Retire Car From Fleet", "Exit");

        typeMenu
            .setOuterSeparator('@')
            .setInnerSeparator('~')
            .setTitle("Select Type Of Your Vehicle")
            .addOption(Vehicle.VehicleType.values())
            .setPrompt("Enter ID: ");

        fuelMenu
            .setOuterSeparator('%')
            .setInnerSeparator('*')
            .setTitle("Select Fuel Type Of Your Vehicle")
            .addOption(Vehicle.FuelType.values())
            .setPrompt("Enter ID: ");

        vehicleTable = new Table("Vehicle ID", "Name", "Owner", "Vehicle Type", "Fuel Type", "Charge Per KM", "Availability Status");
    }

    @Override
    public void display() throws DataSourceException {

        while (true) {
            clearScreen();
            switch (ownerMenu.process()) {
                case 1:
                    vehicleTable.clearRows();
                    for(Vehicle v : ors.getVehiclesOf(owner)){
                        vehicleTable.addRow(
                            v.getId(),
                            v.getName(),
                            v.getOwner().getName(),
                            v.getVehicleType(),
                            v.getFuelType(),
                            v.getChargePerKm(),
                            v.getStatus()
                        );
                    }
                    vehicleTable.display();

                    input("Press Enter To Continue");
                    break;
                case 2:
                    String name = getValidatedInput("Enter Model Of Your Vehicle [Car Manufacturer-Car Model]: ", Validation.FIELD_NOT_EMPTY);
                    Double cpk = Double.parseDouble(getValidatedInput("Enter Charge Per KM: ", Validation.INVALID_DOUBLE));
                    Vehicle.VehicleType type = Vehicle.VehicleType.valueOf(typeMenu.process());
                    Integer seats = Integer.parseInt(getValidatedInput("Enter Number Seats In Your Car: ", Validation.ONLY_NUMERIC));
                    Vehicle.FuelType fuel = Vehicle.FuelType.valueOf(fuelMenu.process());
                    Double mileage = Double.parseDouble(getValidatedInput("Enter The Mileage Of The Car: ", Validation.INVALID_DOUBLE));

                    if(ors.addVehicle(new Vehicle(name, type, this.owner, seats, cpk, mileage, fuel)).isPresent()){
                        System.out.println("Added Successfully");
                    } else {
                        System.out.println("Addition Failed");
                    }


                    input("Press Enter To Continue");

                    break;
                case 3:
                    List<Vehicle> availableVehicle = ors.getVehiclesOf(owner);

                    availableVehicle = availableVehicle.parallelStream().filter(v->v.getStatus().equals(Vehicle.VehicleStatus.AVAILABLE)).toList();

                    if(availableVehicle.isEmpty()){
                        System.out.println("All Your Vehicles Are Either Booked or You Don't Have Any Vehicle");
                        break;
                    }

                    vehicleTable.clearRows();
                    for(Vehicle v : availableVehicle){
                        vehicleTable.addRow(
                            v.getId(),
                            v.getName(),
                            v.getOwner().getName(),
                            v.getVehicleType(),
                            v.getFuelType(),
                            v.getChargePerKm(),
                            v.getStatus()
                        );
                    }
                    vehicleTable.display();

                    try {
                        int vid = integerInput("Enter The ID of the Vehicle you want to Retire: ");

                        Vehicle cancellable = availableVehicle.parallelStream().filter(r->r.getId().equals(vid)).findFirst().orElseThrow();
                        ors.retireVehicle(cancellable);
                    } catch (NumberFormatException | InputMismatchException ae) {
                        System.out.println("Enter Only The ID Number");
                    } catch(NoSuchElementException nse){
                        System.out.println("Invalid ID, Enter In The ID From Table");
                    }

                    input("Press Enter To Continue");

                    break;
                case 4: return;
            }
        }

    }
}
