-- Drop the table if it exists
DROP TABLE IF EXISTS DriverStatus;

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