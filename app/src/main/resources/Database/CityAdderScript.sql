INSERT INTO Place(name, lat, long) VALUES
('Attur', 11.6, 37.3),
('Bodinayakkanur', 10.016666666666667, 2.283333333333333),
('Chennai', 13.066666666666666, 5.333333333333333),
('Chidambaram', 11.4, 25.316666666666666),
('Chingleput', 12.7, 43.333333333333336),
('Coimbatore', 11.0, 1.2833333333333334),
('Cuddalore', 11.716666666666667, 44.31666666666667),
('Devakottai', 9.95, 58.3),
('Dhanushkodi', 9.166666666666666, 11.316666666666666),
('Dharmapuri', 12.133333333333333, 9.3),
('Dindigul', 10.366666666666667, 23.3),
('Erode', 11.333333333333334, 21.283333333333335),
('Kanchipuram', 12.833333333333334, 51.31666666666667),
('Karur', 10.966666666666667, 59.3),
('Kodaikanal', 10.216666666666667, 14.283333333333333),
('Krishnagiri', 12.533333333333333, 33.3),
('Kumbakonam', 10.966666666666667, 59.31666666666667),
('Madurai', 9.966666666666667, 59.3),
('Nagercoil', 8.183333333333334, 12.283333333333333),
('Namakkal', 11.216666666666667, 14.3),
('Perambalur', 11.233333333333333, 15.3),
('Pollachi', 10.65, 40.28333333333333),
('Ponneri', 13.033333333333333, 3.333333333333333),
('Salem', 11.65, 40.3),
('Tenkasi', 8.966666666666667, 59.28333333333333),
('Tiruchchirappalli', 10.833333333333334, 51.3),
('Yercaud', 11.8, 49.3);

INSERT INTO Account(name, mobile, address, password, userType_id) VALUES
('Tester', '1', 'Dungeon', '1', 1),
('Deepesh Kumarsamy', '9876543210', 'Vada malai, Nagar', 'D12K', 1),
('Balaji Shakthi Seelan', '8765432109', 'Singam Vayil, Kotai', 'BalaShakthi', 1),
('Gokul MP', '7654321098', 'Vengadam Seeran, Kuruthirai', 'Kuruthirai789', 1),
('Yaman Sivamani', '4567890123', 'Underground', 'Yaman', 2),
('Shanmugam Govindasamy', '5678901234', 'Emerald Drive, Complex', 'Complex567', 2),
('Avatar Dhanuush', '9012345678', 'Pandora', 'Pandora', 3),
('Yama Kanthan', '0123456789', 'Underworld', 'Ender69', 3),
('ARG', '3210987654', 'Mars Earth Control', 'Control321', 4);

-- Sedans
INSERT INTO Vehicle (name, seats, chargePerKm, owner_id, mileage, fuelType_id, vehicleType_id) VALUES
('Toyota Camry', 5, 9.5, 4, 14.5, 1, 1), -- Owner: Yaman Sivamani
('Honda Accord', 5, 8.8, 5, 16.2, 1, 1), -- Owner: Shanmugam Govindasamy
('Nissan Altima', 5, 8.2, 4, 15.8, 1, 1), -- Owner: Yaman Sivamani
('Chevrolet Malibu', 5, 9.1, 5, 17.4, 1, 1); -- Owner: Shanmugam Govindasamy

-- SUVs
INSERT INTO Vehicle (name, seats, chargePerKm, owner_id, mileage, fuelType_id, vehicleType_id) VALUES
('Toyota Fortuner', 7, 11.5, 4, 13.2, 1, 2), -- Owner: Yaman Sivamani
('Ford Explorer', 7, 12.3, 5, 15.9, 1, 2), -- Owner: Shanmugam Govindasamy
('Honda CR-V', 7, 10.8, 4, 14.7, 1, 2), -- Owner: Yaman Sivamani
('Chevrolet Suburban', 7, 13.6, 5, 12.9, 1, 2); -- Owner: Shanmugam Govindasamy

-- Hatchbacks
INSERT INTO Vehicle (name, seats, chargePerKm, owner_id, mileage, fuelType_id, vehicleType_id) VALUES
('Hyundai i10', 5, 6.2, 4, 20.5, 2, 3), -- Owner: Yaman Sivamani
('Maruti Suzuki Alto', 5, 5.8, 5, 22.1, 2, 3), -- Owner: Shanmugam Govindasamy
('Volkswagen Polo', 5, 6.0, 4, 21.3, 2, 3), -- Owner: Yaman Sivamani
('Ford Fiesta', 5, 6.5, 5, 20.7, 2, 3); -- Owner: Shanmugam Govindasamy



