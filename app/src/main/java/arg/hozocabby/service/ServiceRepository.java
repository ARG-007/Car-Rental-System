package arg.hozocabby.service;

import arg.hozocabby.database.Database;

public class ServiceRepository {
    private final AuthenticationService authenticationService;
    private final CustomerService customerService;
    private final DriverService driverService;
    private final OwnerService ownerService;
    private final AdminService adminService;

    public ServiceRepository(Database db) {
        authenticationService = new AuthenticationService(db.getAccountDataAccess());
        customerService = new CustomerService(db);
        driverService = new DriverService(db);
        ownerService = new OwnerService(db);
        adminService = new AdminService(db);
    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public CustomerService getCustomerService() {
        return customerService;
    }

    public DriverService getDriverService() {
        return driverService;
    }

    public OwnerService getOwnerService() {
        return ownerService;
    }

    public AdminService getAdminService() {
        return adminService;
    }
}
