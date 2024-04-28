-- Drop the table if it exists
DROP TABLE IF EXISTS Driver;

-- Create the Driver table
CREATE TABLE Driver (
    driver_id VARCHAR(10) PRIMARY KEY,
    driver_status CHAR(1),
    driver_name VARCHAR(30) NOT NULL,
    is_handicap BOOLEAN NOT NULL DEFAULT 0,
    FOREIGN KEY (driver_status) REFERENCES DriverStatus(driver_status)
        ON UPDATE CASCADE
);