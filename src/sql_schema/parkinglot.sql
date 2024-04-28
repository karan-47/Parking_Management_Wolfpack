-- Drop the table if it exists
DROP TABLE IF EXISTS ParkingLot;

-- Create the ParkingLot table
CREATE TABLE ParkingLot (
    lot_name VARCHAR(30) PRIMARY KEY,
    lot_address VARCHAR(50) NOT NULL UNIQUE
);