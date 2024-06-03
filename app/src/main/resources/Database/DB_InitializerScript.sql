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

INSERT INTO Place(name, lat, long) VALUES
()

CREATE TABLE RentalStatus (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    status TEXT UNIQUE
);

INSERT INTO RentalStatus (status) VALUES ('Pending'), ('Ongoing'), ('Completed'), ('Cancelled');

CREATE TABLE Rental (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    requester INTEGER ,
    requestedVehicle INTEGER,
    driver INTEGER,
    pickupPlace INTEGER,
    destinationPlace INTEGER,
    pickupTime DATETIME,
    FareEndTime DATETIME,
    rentalStatus_id INTEGER,
    FOREIGN KEY (requester) REFERENCES Account(id),
    FOREIGN KEY (requestedVehicle) REFERENCES Vehicle(id),
    FOREIGN KEY (driver) REFERENCES Account(id),
    FOREIGN KEY (pickupPlace) REFERENCES Place(id),
    FOREIGN KEY (destinationPlace) REFERENCES Place(id),
    FOREIGN KEY (rentalStatus) REFERENCES RentalStatus(id)
);

CREATE TABLE Info (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    rental_id INTEGER,
    distance REAL,
    fareCost REAL,
    FOREIGN KEY (rental_id) REFERENCES Rental(id)
);
