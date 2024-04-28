-- Drop the table if it exists
DROP TABLE IF EXISTS Vehicle;

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