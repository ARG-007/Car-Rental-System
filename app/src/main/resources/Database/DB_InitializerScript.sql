DROP TABLE IF EXISTS Rental;
DROP TABLE IF EXISTS Vehicle;
DROP TABLE IF EXISTS Info;
DROP TABLE IF EXISTS Account;
DROP TABLE IF EXISTS Place;
DROP TABLE IF EXISTS VehicleStatus;
DROP TABLE IF EXISTS RentalStatus;
DROP TABLE IF EXISTS VehicleType;
DROP TABLE IF EXISTS FuelType;
DROP TABLE IF EXISTS UserType;

CREATE TABLE UserType (
    userType_id INTEGER PRIMARY KEY AUTOINCREMENT,
    type TEXT UNIQUE
);

INSERT INTO UserType (type) VALUES ('Customer'), ('Owner'), ('Driver'), ('Admin');

CREATE TABLE FuelType (
    fuelType_id INTEGER PRIMARY KEY AUTOINCREMENT,
    type TEXT UNIQUE
);

INSERT INTO FuelType (type) VALUES ('Petrol'), ('Diesel'), ('CNG');

CREATE TABLE VehicleType (
    vehicleType_id INTEGER PRIMARY KEY AUTOINCREMENT,
    type TEXT UNIQUE
);

INSERT INTO VehicleType (type) VALUES ('HatchBack'), ('Sedan'), ('SUV'), ('MaxiCab');

CREATE TABLE VehicleStatus (
    vehicleStatus_id INTEGER PRIMARY KEY AUTOINCREMENT,
    status TEXT UNIQUE
);

INSERT INTO VehicleStatus (status) VALUES ('Available'), ('Booked'), ('Retired');

CREATE TABLE Account (
    account_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    mobile TEXT UNIQUE NOT NULL,
    address TEXT,
    password TEXT NOT NULL,
    userType_id INTEGER,
    FOREIGN KEY (userType_id) REFERENCES UserType(userType_id)
);

CREATE TABLE Vehicle (
    vehicle_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT,
    seats INTEGER NOT NULL,
    chargePerKm REAL NOT NULL,
    owner_id INTEGER NOT NULL,
    mileage REAL NOT NULL,
    fuelType_id INTEGER,
    vehicleType_id INTEGER,
    vehicleStatus_id INTEGER DEFAULT 1,
    FOREIGN KEY (owner_id) REFERENCES Account(account_id),
    FOREIGN KEY (fuelType_id) REFERENCES FuelType(fuelType_id),
    FOREIGN KEY (vehicleType_id) REFERENCES VehicleType(vehicleType_id),
    FOREIGN KEY (vehicleStatus_id) REFERENCES VehicleStatus(vehicleStatus_id)
);

CREATE TABLE Place (
    place_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT,
    lat REAL,
    long REAL
);

CREATE TABLE RentalStatus (
    rentalStatus_id INTEGER PRIMARY KEY AUTOINCREMENT,
    status TEXT UNIQUE
);

INSERT INTO RentalStatus (status) VALUES ('Pending'), ('Ongoing'), ('Completed'), ('Cancelled');

CREATE TABLE Info (
    info_id INTEGER PRIMARY KEY AUTOINCREMENT,
    requester_id INTEGER ,
    requestedVehicle_id INTEGER,
    requestedVehicleType_id INTEGER,
    pickupPlace_id INTEGER,
    destinationPlace_id INTEGER,
    pickupTime DATETIME,

    FOREIGN KEY (requester_id) REFERENCES Account(account_id),
    FOREIGN KEY (requestedVehicle_id) REFERENCES Vehicle(vehicle_id),
    FOREIGN KEY (requestedVehicleType_id) REFERENCES VehicleType(vehicleType_id),
    FOREIGN KEY (pickupPlace_id) REFERENCES Place(place_id),
    FOREIGN KEY (destinationPlace_id) REFERENCES Place(place_id)
);

CREATE TABLE Rental (
    rental_id INTEGER PRIMARY KEY AUTOINCREMENT,
    info_id INTEGER UNIQUE NOT NULL,
    driver_id INTEGER NULL,
    distance REAL,
    fareEndTime DATETIME,
    fareCost REAL,
    rentalStatus_id INTEGER NOT NULL DEFAULT 1,

    FOREIGN KEY (driver_id) REFERENCES Account(account_id),
    FOREIGN KEY (info_id) REFERENCES Info(info_id),
    FOREIGN KEY (rentalStatus_id) REFERENCES RentalStatus(rentalStatus_id)
);
