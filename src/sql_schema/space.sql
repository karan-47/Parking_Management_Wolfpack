-- Drop the table if it exists
DROP TABLE IF EXISTS Space;

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