CREATE TABLE UserType (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    type TEXT UNIQUE
);

INSERT INTO UserType (type) VALUES ('Admin'), ('Customer'), ('Owner'), ('Driver');

CREATE TABLE FuelType (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    type TEXT UNIQUE
);

INSERT INTO FuelType (type) VALUES ('Petrol'), ('Diesel'), ('CNG');

CREATE TABLE VehicleType (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    type TEXT UNIQUE
);

INSERT INTO VehicleType (type) VALUES ('HatchBack'), ('Sedan'), ('SUV'), ('MaxiCab');

CREATE TABLE Account (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT,
    mobile TEXT,
    address TEXT,
    password TEXT,
    userType_id INTEGER,
    FOREIGN KEY (userType_id) REFERENCES UserType(id)
);

CREATE TABLE Vehicle (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    seats INTEGER,
    chargePerKm REAL,
    owner INTEGER,
    mileage REAL,
    fuelType_id INTEGER,
    vehicleType_id INTEGER,
    FOREIGN KEY (owner) REFERENCES Account(id),
    FOREIGN KEY (fuelType_id) REFERENCES FuelType(id),
    FOREIGN KEY (vehicleType_id) REFERENCES VehicleType(id)
);

CREATE TABLE Place (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT,
    lat REAL,
    long REAL
);

CREATE TABLE RentalStatus (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    status TEXT UNIQUE
);

INSERT INTO RentalStatus (status) VALUES ('Pending'), ('Ongoing'), ('Completed'), ('Cancelled');

CREATE TABLE Rental (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    requester_id INTEGER,
    requestedVehicle_id INTEGER,
    driver_id INTEGER,
    pickupPlace_id INTEGER,
    destinationPlace_id INTEGER,
    pickupTime DATETIME,
    FareEndTime DATETIME,
    rentalStatus_id INTEGER,
    FOREIGN KEY (requester_id) REFERENCES Account(id),
    FOREIGN KEY (requestedVehicle_id) REFERENCES Vehicle(id),
    FOREIGN KEY (driver_id) REFERENCES Account(id),
    FOREIGN KEY (pickupPlace_id) REFERENCES Place(id),
    FOREIGN KEY (destinationPlace_id) REFERENCES Place(id),
    FOREIGN KEY (rentalStatus_id) REFERENCES RentalStatus(id)
);

CREATE TABLE Info (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    rental_id INTEGER,
    distance REAL,
    fareCost REAL,
    FOREIGN KEY (rental_id) REFERENCES Rental(id)
);
