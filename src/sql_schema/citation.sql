-- Drop the table if it exists
DROP TABLE IF EXISTS Citation;

-- Create the Citation table
CREATE TABLE Citation (
    citation_id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_num VARCHAR(10),
    lot_name VARCHAR(30),
    violation_category VARCHAR(20),
    citation_time DATETIME NOT NULL,
    citation_amount FLOAT NOT NULL,
    citation_valid BOOLEAN DEFAULT 1 NOT NULL,
    citation_paid BOOLEAN DEFAULT 0 NOT NULL,
    citation_appeal BOOLEAN DEFAULT 0 NOT NULL,
    citation_appeal_comment TEXT,
    FOREIGN KEY (lot_name) REFERENCES ParkingLot(lot_name)
        ON UPDATE CASCADE,
    FOREIGN KEY (violation_category) REFERENCES Violation(violation_category)
        ON UPDATE CASCADE
);