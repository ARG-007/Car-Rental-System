package arg.hozocabby.views.console;

import arg.hozocabby.entities.Account;
import arg.hozocabby.entities.Place;
import arg.hozocabby.entities.Rental;
import arg.hozocabby.entities.Vehicle;
import arg.hozocabby.exceptions.DataSourceException;
import arg.hozocabby.service.AdminService;

public class AdminMenu extends Console{

    private final AdminService ads;
    private final Account adminAcount ;

    private final Table accountTable;
    private final Table vehicleTable;
    private final Table placeTable;
    private final Table rentalTable;


    private final Menu adminMenu = new Menu();

    public AdminMenu(Account adminAcount, AdminService ads) {
        this.ads = ads;
        this.adminAcount = adminAcount;

        adminMenu
            .setTitle("Admin Menu")
            .setInnerSeparator('-')
            .setOuterSeparator('#')
            .addOption("View Accounts", "View Rentals", "View Vehicles", "View Places", "Exit")
            .setPrompt("Enter Your Choice: ");

        accountTable = new Table("Account ID", "Name", "Mobile", "Address", "Type");
        vehicleTable = new Table("Vehicle ID", "Name", "Owner", "Vehicle Type", "Fuel Type", "Charge Per KM", "Availability Status");
        placeTable = new Table("Place ID", "Name", "Latitude", "Longitude");
        rentalTable = new Table("Rental ID", "Customer", "Pickup", "Pickup Time", "Destination", "Status");

    }

    public void display() throws DataSourceException {

        while(true) {
            clearScreen();
            switch (adminMenu.process()){
                case 1:
                    accountTable.clearRows();
                    for(Account acc : ads.getAccounts()){
                        accountTable.addRow(acc.getId(), acc.getName(), acc.getPhone(), acc.getAddress(), acc.getType());
                    }
                    accountTable.display();
                    break;
                case 2:
                    rentalTable.clearRows();
                    for(Rental r: ads.getRentals()){
                        rentalTable.addRow(
                            r.getId(),
                            r.getInfo().getRequester().getName(),
                            r.getInfo().getPickup(),
                            r.getInfo().getPickupTime(),
                            r.getInfo().getDestination(),
                            r.getStatus()
                        );
                    }
                    rentalTable.display();
                    break;
                case 3:
                    vehicleTable.clearRows();
                    for(Vehicle v : ads.getVehicles()){
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
                    break;
                case 4:
                    placeTable.clearRows();
                    for(Place p: ads.getPlaces()){
                        placeTable.addRow(
                            p.id(),
                            p.name(),
                            String.format("%.2f", p.latitude()),
                            String.format("%.2f",p.longitude())
                        );
                    }
                    placeTable.display();
                    break;
                case 5: return;
            }
        }




    }
}
