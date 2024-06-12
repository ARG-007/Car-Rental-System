package arg.hozocabby.service;

import arg.hozocabby.database.DatabaseManager;

public class ServiceRepository {
    private final AuthenticationService authenticationService;
    private final CustomerService customerService;
    private final DriverService driverService;
    private final OwnerService ownerService;
    private final AdminService adminService;

    public ServiceRepository(DatabaseManager dbMan) {
        authenticationService = new AuthenticationService(dbMan.getAccountDataAccess());
        customerService = new CustomerService(dbMan);
        driverService = new DriverService(dbMan);
        ownerService = new OwnerService(dbMan);
        adminService = new AdminService(dbMan);
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
