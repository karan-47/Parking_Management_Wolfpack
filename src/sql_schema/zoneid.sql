-- Drop the table if it exists
DROP TABLE IF EXISTS ZoneId;

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