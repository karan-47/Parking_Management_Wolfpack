-- Drop the table if it exists
DROP TABLE IF EXISTS PermitForVehicle;

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