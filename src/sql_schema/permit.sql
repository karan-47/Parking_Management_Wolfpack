-- Drop the table if it exists
DROP TABLE IF EXISTS Permit;

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