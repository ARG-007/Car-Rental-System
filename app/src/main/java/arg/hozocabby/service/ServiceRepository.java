package arg.hozocabby.service;

import arg.hozocabby.database.Database;

public class ServiceRepository {
    private AuthenticationService authenticationService;
    private CustomerService customerService;
    private DriverService driverService;
    private OwnerService ownerService;
    private AdminService adminService;

    public ServiceRepository(Database db) {
        authenticationService = new AuthenticationService(db.getAccountDataAccess());
        customerService = new CustomerService(db);

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
