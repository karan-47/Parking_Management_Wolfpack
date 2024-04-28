DROP TABLE IF EXISTS PermitForVehicle;
DROP TABLE IF EXISTS Citation;
DROP TABLE IF EXISTS Permit;
DROP TABLE IF EXISTS Vehicle;
DROP TABLE IF EXISTS Driver;
DROP TABLE IF EXISTS LotContainsZone;
DROP TABLE IF EXISTS Space;
DROP TABLE IF EXISTS ParkingLot;
DROP TABLE IF EXISTS ZoneId;
DROP TABLE IF EXISTS Violation;
DROP TABLE IF EXISTS PermitType;
DROP TABLE IF EXISTS DriverStatus;
DROP TABLE IF EXISTS SpaceType;

-- Create the SpaceType table
CREATE TABLE SpaceType (
    space_type VARCHAR(20) PRIMARY KEY
);

INSERT INTO SpaceType (space_type) VALUES
('regular'),
('compact car'),
('electric'),
('handicap');

-- Create the DriverStatus table
CREATE TABLE DriverStatus (
    driver_status CHAR(1) PRIMARY KEY,
    max_permits INT NOT NULL,
    max_cars_in_permit INT NOT NULL,
    allow_extra BOOLEAN NOT NULL DEFAULT 0
);

INSERT INTO DriverStatus (driver_status, max_permits, max_cars_in_permit, allow_extra) VALUES
('E', 2, 2, 1),
('S', 1, 1, 1),
('V', 1, 1, 0);

-- Create the PermitType table
CREATE TABLE PermitType (
    permit_type VARCHAR(20) PRIMARY KEY
);

INSERT INTO PermitType (permit_type) VALUES
('residential'),
('commuter'),
('peak hours'),
('special event'),
('park and ride');

-- Create the Violation table
CREATE TABLE Violation (
    violation_category VARCHAR(20) PRIMARY KEY,
    violation_fee FLOAT NOT NULL
);

INSERT INTO Violation (violation_category, violation_fee) VALUES
('invalid permit', 25),
('expired permit', 30),
('no permit', 40);

-- Create the ZoneId table
CREATE TABLE ZoneId (
    zone_id VARCHAR(2) PRIMARY KEY,
    driver_status CHAR(1),
    FOREIGN KEY (driver_status) REFERENCES DriverStatus(driver_status)
        ON UPDATE CASCADE
);

INSERT INTO ZoneId (zone_id, driver_status) VALUES
('A', 'E'),
('B', 'E'),
('C', 'E'),
('D', 'E'),
('AS', 'S'),
('BS', 'S'),
('CS', 'S'),
('DS', 'S'),
('V', 'V');

-- Create the ParkingLot table
CREATE TABLE ParkingLot (
                            lot_name VARCHAR(30) PRIMARY KEY,
                            lot_address VARCHAR(50) NOT NULL UNIQUE
);

-- Create the Space table with a composite key
CREATE TABLE Space (
                       lot_name VARCHAR(30),
                       space_num INT,
                       space_type VARCHAR(20) DEFAULT 'regular',
                       avail_status BOOLEAN NOT NULL DEFAULT 1,
                       vehicle_num VARCHAR(10),
                       PRIMARY KEY (lot_name, space_num),
                       FOREIGN KEY (lot_name) REFERENCES ParkingLot(lot_name)
                           ON UPDATE CASCADE,
                       FOREIGN KEY (space_type) REFERENCES SpaceType(space_type)
                           ON UPDATE CASCADE
);

-- Create the LotContainsZone table with a composite key
CREATE TABLE LotContainsZone (
                                 lot_name VARCHAR(30),
                                 zone_id VARCHAR(2),
                                 PRIMARY KEY (lot_name, zone_id),
                                 FOREIGN KEY (lot_name) REFERENCES ParkingLot(lot_name)
                                     ON UPDATE CASCADE,
                                 FOREIGN KEY (zone_id) REFERENCES ZoneId(zone_id)
                                     ON UPDATE CASCADE
);

-- Create the Driver table
CREATE TABLE Driver (
                        driver_id VARCHAR(10) PRIMARY KEY,
                        driver_status CHAR(1),
                        driver_name VARCHAR(30) NOT NULL,
                        is_handicap BOOLEAN NOT NULL DEFAULT 0,
                        FOREIGN KEY (driver_status) REFERENCES DriverStatus(driver_status)
                            ON UPDATE CASCADE
);

-- Create the Vehicle table
CREATE TABLE Vehicle (
                         vehicle_num VARCHAR(10) PRIMARY KEY,
                         driver_id VARCHAR(10),
                         vehicle_manuf VARCHAR(20) NOT NULL,
                         vehicle_model VARCHAR(20) NOT NULL,
                         vehicle_year YEAR,
                         vehicle_color VARCHAR(20),
                         FOREIGN KEY (driver_id) REFERENCES Driver(driver_id)
                             ON UPDATE CASCADE
);

-- Create the Permit table
CREATE TABLE Permit (
                        permit_id INT AUTO_INCREMENT PRIMARY KEY,
                        driver_id VARCHAR(10),
                        permit_type VARCHAR(20),
                        zone_id VARCHAR(2),
                        permit_start_time DATETIME(2) NOT NULL,
                        permit_expiry_time DATETIME(2) NOT NULL,
                        FOREIGN KEY (driver_id) REFERENCES Driver(driver_id)
                            ON UPDATE CASCADE,
                        FOREIGN KEY (permit_type) REFERENCES PermitType(permit_type)
                            ON UPDATE CASCADE,
                        FOREIGN KEY (zone_id) REFERENCES ZoneId(zone_id)
                            ON UPDATE CASCADE
);

-- Create the Citation table
CREATE TABLE Citation (
                          citation_id INT AUTO_INCREMENT PRIMARY KEY,
                          vehicle_num VARCHAR(10),
                          lot_name VARCHAR(30),
                          violation_category VARCHAR(20),
                          citation_time DATETIME NOT NULL,
                          citation_amount FLOAT NOT NULL,
                          citation_valid BOOLEAN DEFAULT 1 NOT NULL,
                          citation_paid BOOLEAN DEFAULT 0 NOT NULL,
                          citation_appeal BOOLEAN DEFAULT 0 NOT NULL,
                          citation_appeal_comment TEXT,
                          FOREIGN KEY (lot_name) REFERENCES ParkingLot(lot_name)
                              ON UPDATE CASCADE,
                          FOREIGN KEY (violation_category) REFERENCES Violation(violation_category)
                              ON UPDATE CASCADE
);

-- Create the PermitForVehicle table with a composite key
CREATE TABLE PermitForVehicle (
                                  permit_id INT,
                                  vehicle_num VARCHAR(10),
                                  space_type VARCHAR(20),
                                  PRIMARY KEY (permit_id, vehicle_num),
                                  FOREIGN KEY (permit_id) REFERENCES Permit(permit_id)
                                      ON UPDATE CASCADE,
                                  FOREIGN KEY (vehicle_num) REFERENCES Vehicle(vehicle_num)
                                      ON UPDATE CASCADE,
                                  FOREIGN KEY (space_type) REFERENCES SpaceType(space_type)
                                      ON UPDATE CASCADE
);