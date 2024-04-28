INSERT INTO ParkingLot (lot_name, lot_address)
VALUES
    ('Lot A', '123 Main Street'),
    ('Lot B', '456 Elm Avenue'),
    ('Lot C', '789 Oak Road'),
    ('Lot D', '101 Pine Lane'),
    ('Lot E', '202 Cedar Drive');

INSERT INTO LotContainsZone (lot_name, zone_id)
VALUES
    ('Lot A', 'A'),
    ('Lot A', 'AS'),
    ('Lot A', 'B'),
    ('Lot A', 'BS'),
    ('Lot B', 'B'),
    ('Lot C', 'C'),
    ('Lot D', 'D'),
    ('Lot D', 'V'),
    ('Lot E', 'CS'),
    ('Lot E', 'DS');

INSERT INTO Driver (driver_id, driver_status, driver_name, is_handicap)
VALUES
    ('E001', 'E', 'John Smith', 0),
    ('E002', 'E', 'Jane Johnson', 1),
    ('S001', 'S', 'Michael Brown', 0),
    ('S002', 'S', 'Sarah Davis', 1),
    ('9190001111', 'V', 'Robert Wilson', 0);

INSERT INTO Vehicle (vehicle_num, driver_id, vehicle_manuf, vehicle_model, vehicle_year, vehicle_color)
VALUES 
    ('V001', 'E001', 'Toyota', 'Camry', 2020, 'Silver'),
    ('V002', 'E001', 'Honda', 'Civic', 2019, 'Red'),
    ('V003', 'E001', 'Ford', 'Escape', 2021, 'Blue'),
    ('V004', 'S001', 'Chevrolet', 'Malibu', 2018, 'Gray'),
    ('V005', 'S001', 'Nissan', 'Altima', 2017, 'White'),
    ('V006', 'S002', 'Honda', 'CR-V', 2019, 'Black'),
    ('V007', '9190001111', 'Toyota', 'Corolla', 2022, 'Silver');

INSERT INTO Permit (driver_id, permit_type, zone_id, permit_start_time, permit_expiry_time)
VALUES 
    ('E001', 'commuter', 'A', '2022-10-19 08:00:00.00', '2024-10-19 18:00:00.00'),
    ('E001', 'commuter', 'C', '2022-10-19 10:30:00.00', '2023-10-19 15:30:00.00'),
    ('S001', 'residential', 'AS', '2022-10-19 07:30:00.00', '2024-10-19 17:30:00.00'),
    ('S002', 'commuter', 'CS', '2022-10-19 09:00:00.00', '2024-08-01 13:00:00.00'),
    ('9190001111', 'special event', 'V', '2022-10-19 12:00:00.00', '2024-10-19 21:00:00.00');

INSERT INTO PermitForVehicle (permit_id, vehicle_num, space_type)
VALUES
  (7, 'V001', 'electric'),
  (7, 'V002', 'regular'),
  (8, 'V003', 'regular'),
  (9, 'V004', 'compact car'),
  (10, 'V006', 'handicap'),
  (11, 'V007', 'electric');

INSERT INTO Space (lot_name, space_num, space_type)
VALUES
  ('Lot A', 1, 'electric'),
  ('Lot A', 2, 'regular'),
  ('Lot A', 3, 'regular'),
  ('Lot A', 4, 'compact car'),
  ('Lot A', 5, 'handicap'),
  ('Lot B', 1, 'electric'),
  ('Lot B', 2, 'regular'),
  ('Lot B', 3, 'regular'),
  ('Lot B', 4, 'compact car'),
  ('Lot B', 5, 'handicap'),
  ('Lot C', 1, 'electric'),
  ('Lot C', 2, 'regular'),
  ('Lot C', 3, 'regular'),
  ('Lot C', 4, 'compact car'),
  ('Lot C', 5, 'handicap'),
  ('Lot D', 1, 'electric'),
  ('Lot D', 2, 'regular'),
  ('Lot D', 3, 'regular'),
  ('Lot D', 4, 'compact car'),
  ('Lot D', 5, 'handicap'),
  ('Lot E', 1, 'electric'),
  ('Lot E', 2, 'regular'),
  ('Lot E', 3, 'regular'),
  ('Lot E', 4, 'compact car'),
  ('Lot E', 5, 'handicap');

INSERT INTO Citation (vehicle_num, violation_category, lot_name, citation_time, citation_amount, citation_appeal_comment)
VALUES
  ('V001', 'invalid permit', 'Lot A', '2023-10-19 12:00:00', 25.00, NULL),
  ('V009', 'no permit', 'Lot B', '2023-10-19 12:00:00', 40.00, NULL),
  ('V002', 'invalid permit', 'Lot C', '2023-10-19 11:00:00', 25.00, NULL),
  ('V006', 'expired permit', 'Lot A', '2023-10-19 10:00:00', 15.00, NULL),
  ('V003', 'invalid permit', 'Lot D', '2023-10-19 14:20:00', 25.00, NULL);
