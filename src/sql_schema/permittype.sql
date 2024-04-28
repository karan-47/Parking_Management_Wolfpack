-- Drop the table if it exists
DROP TABLE IF EXISTS PermitType;

-- Create the PermitType table
CREATE TABLE PermitType (
    permit_type VARCHAR(20) PRIMARY KEY
);

INSERT INTO PermitType (permit_type) VALUES
('residential'),
('commuter'),
('peak hours'),
('special event'),
('park and ride');