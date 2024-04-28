-- Drop the table if it exists
DROP TABLE IF EXISTS LotContainsZone;

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