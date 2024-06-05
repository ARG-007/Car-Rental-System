DROP TABLE IF EXISTS UserType;
DROP TABLE IF EXISTS FuelType;
DROP TABLE IF EXISTS VehicleType;
DROP TABLE IF EXISTS Account;
DROP TABLE IF EXISTS Vehicle;
DROP TABLE IF EXISTS Place;
DROP TABLE IF EXISTS RentalStatus;
DROP TABLE IF EXISTS Rental;
DROP TABLE IF EXISTS Info;

CREATE TABLE UserType (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    type TEXT UNIQUE
);

INSERT INTO UserType (type) VALUES ('Customer'), ('Owner'), ('Driver'), ('Admin');

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

CREATE TABLE VehicleStatus {
    id INT PRIMARY KEY AUTOINCREMENT,
    status TEXT UNIQUE
}

INSERT INTO VehicleStatus (status) VALUES ('Available'), ('Booked'), ('Retired');

CREATE TABLE Account (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    mobile TEXT UNIQUE NOT NULL,
    address TEXT,
    password TEXT NOT NULL,
    userType INTEGER,
    FOREIGN KEY (userType) REFERENCES UserType(id)
);

CREATE TABLE Vehicle (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    seats INTEGER NOT NULL,
    chargePerKm REAL NOT NULL,
    owner INTEGER NOT NULL,
    mileage REAL NOT NULL,
    fuelType INTEGER,
    vehicleType INTEGER,
    vehicleStatus INTEGER DEFAULT 1,
    FOREIGN KEY (owner) REFERENCES Account(id),
    FOREIGN KEY (fuelType) REFERENCES FuelType(id),
    FOREIGN KEY (vehicleType) REFERENCES VehicleType(id),
    FOREIGN KEY (vehicleStatus) REFERENCES VehicleStatus(id)
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

CREATE TABLE Info (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    requester INTEGER ,
    requestedVehicle INTEGER,
    pickupPlace INTEGER,
    destinationPlace INTEGER,
    pickupTime DATETIME,

    FOREIGN KEY (requester) REFERENCES Account(id),
    FOREIGN KEY (requestedVehicle) REFERENCES Vehicle(id),

    FOREIGN KEY (pickupPlace) REFERENCES Place(id),
    FOREIGN KEY (destinationPlace) REFERENCES Place(id)
);

CREATE TABLE Rental (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    info INTEGER UNIQUE NOT NULL,
    driver INTEGER NULL,
    distance REAL,
    fareEndTime DATETIME,
    fareCost REAL,
    status INTEGER NOT NULL,

    FOREIGN KEY (driver) REFERENCES Account(id),
    FOREIGN KEY (info) REFERENCES Info(id),
    FOREIGN KEY (status) REFERENCES RentalStatus(id)
);
