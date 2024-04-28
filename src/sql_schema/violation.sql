-- Drop the table if it exists
DROP TABLE IF EXISTS Violation;

-- Create the Violation table
CREATE TABLE Violation (
    violation_category VARCHAR(20) PRIMARY KEY,
    violation_fee FLOAT NOT NULL
);

INSERT INTO Violation (violation_category, violation_fee) VALUES
('invalid permit', 25),
('expired permit', 30),
('no permit', 40);