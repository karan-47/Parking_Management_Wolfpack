-- Drop the table if it exists
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