INSERT INTO ParkingLot (lot_name, lot_address)
VALUES
    ('Poulton Deck', '1021 Main Campus Dr Raleigh, NC, 27606'),
    ('Partners Way Deck', '851 Partners Way Raleigh, NC, 27606'),
    ('Dan Allen Parking Deck', '110 Dan Allen Dr Raleigh, NC, 27607');

INSERT INTO LotContainsZone (lot_name, zone_id)
VALUES
    ('Poulton Deck', 'A'),
    ('Poulton Deck', 'AS'),
    ('Poulton Deck', 'V'),
    ('Partners Way Deck', 'B'),
    ('Dan Allen Parking Deck', 'A'),
    ('Dan Allen Parking Deck', 'AS'),
    ('Dan Allen Parking Deck', 'B'),
    ('Dan Allen Parking Deck', 'BS'),
    ('Dan Allen Parking Deck', 'V');

INSERT INTO Driver (driver_id, driver_status, driver_name, is_handicap)
VALUES
    ('7729119111', 'V', 'Sam BankmanFried', 0),
    ('266399121', 'E', 'John Clay', 0),
    ('366399121', 'E', 'Julia Hicks', 0),
    ('466399121', 'E', 'Ivan Garcia', 0),
    ('122765234', 'S', 'Sachin Tendulkar', 0),
    ('9194789124', 'V', 'Charles Xavier', 1);

INSERT INTO Vehicle (vehicle_num, driver_id, vehicle_manuf, vehicle_model, vehicle_year, vehicle_color)
VALUES 
    ('SBF', '7729119111', 'Nissan', 'GT-R-Nismo', 2024, 'Pearl White TriCoat'),
    ('Clay1', '266399121', 'Tesla', 'Model S', 2023, 'Ultra Red'),
    ('Hicks1', '366399121', 'BMW', 'M2 Coupe', 2024, 'Zandvoort Blue'),
    ('Garcia1', '466399121', 'Bentley', 'Continental GT Speed', 2024, 'Blue Fusion'),
    ('CRICKET', '122765234', 'Honda', 'Civic SI', 2024, 'Sonic Gray Pearl'),
    ('PROFX', '9194789124', 'Porsche', 'Taycan Sport Turismo', 2024, 'Frozenblue Metallic');

INSERT INTO Permit (driver_id, permit_type, zone_id, permit_start_time, permit_expiry_time)
VALUES 
    ('7729119111', 'commuter', 'V', '2023-01-01 00:00:00.00', '2024-01-01 06:00:00.00'),
    ('266399121', 'residential', 'A', '2010-01-01 00:00:00.00', '2030-01-01 06:00:00.00'),
    ('366399121', 'commuter', 'A', '2023-01-01 00:00:00.00', '2024-01-01 06:00:00.00'),
    ('466399121', 'commuter', 'A', '2023-01-01 00:00:00.00', '2024-01-01 06:00:00.00'),
    ('122765234', 'residential', 'AS', '2022-01-01 00:00:00.00', '2023-09-30 06:00:00.00'),
    ('9194789124', 'special event', 'V', '2023-01-01 00:00:00.00', '2023-11-15 06:00:00.00');

INSERT INTO PermitForVehicle (permit_id, vehicle_num, space_type)
VALUES
  (1, 'SBF', 'regular'),
  (2, 'Clay1', 'electric'),
  (3, 'Hicks1', 'regular'),
  (4, 'Garcia1', 'regular'),
  (5, 'CRICKET', 'compact car'),
  (6, 'PROFX', 'handicap');

INSERT INTO Space (lot_name, space_num, space_type)
VALUES
  ('Poulton Deck', 1, 'electric'),
  ('Poulton Deck', 2, 'regular'),
  ('Poulton Deck', 3, 'regular'),
  ('Poulton Deck', 4, 'compact car'),
  ('Poulton Deck', 5, 'handicap'),
  ('Partners Way Deck', 1, 'electric'),
  ('Partners Way Deck', 2, 'regular'),
  ('Partners Way Deck', 3, 'regular'),
  ('Partners Way Deck', 4, 'compact car'),
  ('Partners Way Deck', 5, 'handicap'),
  ('Dan Allen Parking Deck', 1, 'electric'),
  ('Dan Allen Parking Deck', 2, 'regular'),
  ('Dan Allen Parking Deck', 3, 'regular'),
  ('Dan Allen Parking Deck', 4, 'compact car'),
  ('Dan Allen Parking Deck', 5, 'handicap');

INSERT INTO Citation (vehicle_num, violation_category, lot_name, citation_time, citation_amount, citation_paid, citation_appeal_comment)
VALUES
  ('VAN-9910', 'no permit', 'Dan Allen Parking Deck', '2021-10-11 08:00:00', 40.00, 1, NULL),
  ('CRICKET', 'expired permit', 'Poulton Deck', '2023-10-01 08:00:00', 30.00, 0, NULL);
