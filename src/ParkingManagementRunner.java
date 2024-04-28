import db.DBConnector;
import db_models.*;

import java.sql.Connection;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Scanner;
import java.util.List;

public class ParkingManagementRunner {
	
	static Scanner scanString = new Scanner(System.in);
	static Scanner scanInt = new Scanner(System.in);
	
	private static void OptionParkingLots(Connection dbConn) {
		boolean exit = false;
		int ch = 0;
		int status = 0;
		try {
			ParkingLot lot = new ParkingLot();
			LotContainsZone lotContainsZone = new LotContainsZone(lot);
			do {
				System.out.println("Manage Parking Lot(s) information:");
				System.out.println("1. Add parking lot");
				System.out.println("2. Update parking lot (name, address)");
				System.out.println("3. Delete parking lot");
				System.out.println("4. Assign zone to parking lot");
				System.out.println("5. Delete zone from parking lot");
				System.out.println("6. List all parking lots");
				System.out.println("7. List zones in parking lot (given lot name)");
				System.out.println("8. List information on parking lot (given lot name)");
				System.out.println("0. Go back to main menu");
				System.out.println("Choose an option (0-8): ");
				ch = scanInt.nextInt();
				switch (ch) {
					case 0 -> exit = true;
					case 1 -> {
						String lotName, lotAddress;
						System.out.println("Parking lot name:");
						lotName = scanString.nextLine();
						System.out.println("Parking lot address: ");
						lotAddress = scanString.nextLine();
						status = lot.addParkingLotInfo(dbConn, lotName, lotAddress);
						if (status == 0) {
							System.out.println("Failed to add parking lot");
						} else {
							System.out.println("Parking lot -> " + lotName + " inserted successfully");
						}
						System.out.println();
					}
					case 2 -> {
						String lotName, newLotName, newLotAddress;
						System.out.println("Parking lot name: ");
						lotName = scanString.nextLine();
						System.out.println("New parking lot name: ");
						newLotName = scanString.nextLine();
						System.out.println("New parking lot address: ");
						newLotAddress = scanString.nextLine();
						status = lot.updateParkingLot(dbConn, lotName, newLotName, newLotAddress);
						if (status == 0) {
							System.out.println("Failed to update parking lot");
						} else {
							System.out.println("Parking lot -> " + lotName + " updated successfully");
						}
						System.out.println();
					}
					case 3 -> {
						String lotName;
						System.out.println("Parking lot name: ");
						lotName = scanString.nextLine();
						status = lot.deleteParkingLotInfo(dbConn, lotName);
						if (status == 0) {
							System.out.println("Failed to delete parking lot");
						} else {
							System.out.println("Parking lot -> " + lotName + " deleted successfully");
						}
						System.out.println();
					}
					case 4 -> {
						String lotName, newZone;
						System.out.println("Parking lot name: ");
						lotName = scanString.nextLine().trim();
						System.out.println("Zone to assign: ");
						newZone = scanString.nextLine().trim();
						status = lotContainsZone.assignZoneToLot(dbConn, lotName, newZone);
						if (status == 0) {
							System.out.println("Failed to assign zone -> " + newZone + " to lot -> " + lotName);
						} else {
							System.out.println("Zone -> " + newZone + " assigned to lot -> " + lotName);
						}
						System.out.println();
					}
					case 5 -> {
						String lotName, newZone;
						System.out.println("Parking lot name: ");
						lotName = scanString.nextLine().trim();
						System.out.println("Zone to remove: ");
						newZone = scanString.nextLine().trim();
						status = lotContainsZone.deleteZoneFromLot(dbConn, lotName, newZone);
						if (status == 0) {
							System.out.println("Failed to delete zone -> " + newZone + " from lot -> " + lotName);
						} else {
							System.out.println("Zone -> " + newZone + " deleted from lot -> " + lotName);
						}
					}
					case 6 -> {
						lot.getParkingLotInfo(dbConn);
						System.out.println();
					}
					case 7 -> {
						String lotName;
						System.out.println("Parking lot name: ");
						lotName = scanString.nextLine().trim();
						List<String> zones = lot.getZonesInLot(dbConn, lotName);
						if (zones.isEmpty()) {
							System.out.println("No Zones found for lot -> " + lotName);
						} else {
							System.out.println("Zone -> " + zones + " have been assigned to Lot Name -> " + lotName);
						}
						System.out.println();
					}
					case 8 -> {
						String lotName;
						System.out.println("Parking lot name: ");
						lotName = scanString.nextLine();
						lot.getParkingLotInfo(dbConn, lotName);
						System.out.println();
					}
					default -> System.out.println("Invalid option. Please try again.");
				}
			} while (!exit);
		} catch (SQLIntegrityConstraintViolationException duplicateException) {
			System.out.println("Integrity constraint violated for the operation");
		}
		catch (Exception e) {
			System.err.println("Caught exception in method OptionParkingLots: " + e);
		}
	}
	
	private static void OptionSpaces(Connection dbConn) {
	    boolean exit = false;
	    int ch;   

	    try {
			Space space = new Space();
	        do {
	            System.out.println("Manage Space(s) Information:");
	            System.out.println("1. Add space");
	            System.out.println("2. Update space type");
	            System.out.println("3. Delete space");
	            System.out.println("4. List all spaces in all parking lots");
				System.out.println("5. List all spaces (given parking lot)");
	            System.out.println("6. List space information (given lot name, space number)");
	            System.out.println("7. Update space availability status");
	            System.out.println("0. Go back to main menu");
	            System.out.println("Choose an option (0-7): ");
	            ch = scanInt.nextInt();

	            switch (ch) {
	                case 0 -> exit = true;
	                case 1 -> {
						System.out.println("Enter Lot Name:");
						String lotName = scanString.nextLine();
						System.out.println("Enter Space Number:");
						int spaceNum = scanInt.nextInt();
						System.out.println("Enter Space Type (1. Regular, 2. Compact Car, 3. Electric, 4. Handicap):");
						int spaceTypeOption = scanInt.nextInt();
						String spaceType = mapSpaceType(spaceTypeOption);
						int status = space.addSpaceToLot(dbConn, lotName, spaceNum, spaceType);
						if (status == 0) {
							System.out.println("Failed to add space");
						} else {
							System.out.println("Space added successfully");
						}
						System.out.println();
					}
	                case 2 -> {
						System.out.println("Enter Lot Name:");
						String lotName = scanString.nextLine();
						System.out.println("Enter Space Number:");
						int spaceNum = scanInt.nextInt();
						System.out.println("Enter New Space Type (1. Regular, 2. Compact Car, 3. Electric, 4. Handicap):");
						int newSpaceTypeOption = scanInt.nextInt();
						String newSpaceType = mapSpaceType(newSpaceTypeOption);
						int status = space.updateSpaceInfo(dbConn, lotName, spaceNum, newSpaceType);
						if (status == 0) {
							System.out.println("Failed to update space");
						} else {
							System.out.println("Space updated successfully");
						}
						System.out.println();
					}
	                case 3 -> {
						System.out.println("Enter Lot Name:");
						String lotName = scanString.nextLine();
						System.out.println("Enter Space Number:");
						int spaceNum = scanInt.nextInt();
						int status = space.deleteSpaceInfo(dbConn, lotName, spaceNum);
						if (status == 0) {
							System.out.println("Failed to delete space");
						} else {
							System.out.println("Space deleted successfully");
						}
						System.out.println();
					}
	                case 4 -> {
						space.printAllSpaceInfo(dbConn);
						System.out.println();
					}
					case 5 -> {
						System.out.println("Enter Lot Name:");
						String lotName = scanString.nextLine();
						space.getSpaceInfo(dbConn, lotName);
						System.out.println();
					}
	                case 6 -> {
						System.out.println("Enter Lot Name:");
						String lotName = scanString.nextLine();
						System.out.println("Enter Space Number:");
						int spaceNum = scanInt.nextInt();
						space.getSpaceInfo(dbConn, lotName, spaceNum);
						System.out.println();
					}
	                case 7 -> {
						System.out.println("Enter Lot Name:");
						String lotName = scanString.nextLine();
						System.out.println("Enter Space Number:");
						int spaceNum = scanInt.nextInt();
						System.out.println("Enter Availability Status (0 or 1):");
						int availStatus = scanInt.nextInt();

						String vehicleNum = null;
						if (availStatus == 0) {
							System.out.println("Enter vehicle number parked in space:");
							vehicleNum = scanString.nextLine();
						}

						int status = space.updateAvailabilitySpaceStatus(dbConn, lotName, spaceNum, availStatus, vehicleNum);
						if (status == 0) {
							System.out.println("Failed to update availability status");
						} else {
							System.out.println("Status updated successfully");
						}
						System.out.println();
					}
					default -> System.out.println("Invalid option. Please try again.");
	            }

	        } while (!exit);
	    } catch (SQLIntegrityConstraintViolationException duplicateException) {
			System.out.println("Integrity constraint violated for the operation");
		} catch (Exception e) {
	        System.err.println("Caught exception in method OptionSpaces: " + e);
	    }
	}

	private static String mapSpaceType(int spaceTypeOption) {
        return switch (spaceTypeOption) {
            case 2 -> "compact car";
            case 3 -> "electric";
            case 4 -> "handicap";
            default -> "regular";
        };
	}

	private static void OptionDrivers(Connection dbConn) {
		boolean exit = false;
		int ch = 0;
		int status = 0;
		try {
			Driver driver = new Driver();
			do {
				System.out.println("Manage Driver(s) Information:");
				System.out.println("1. Add driver");
				System.out.println("2. Update driver (name, handicap status)");
				System.out.println("3. Delete driver (given driver Id)");
				System.out.println("4. List driver status (given driver Id)");
				System.out.println("5. List all drivers");
				System.out.println("6. List driver (given driver Id)");
				System.out.println("0. Go back to main menu");
				System.out.println("Choose an option (0-6): ");
				ch = scanInt.nextInt();
				switch (ch) {
					case 0 -> exit = true;
					case 1 -> {
						String driverId, driverName, driverStatus, isHandicap;
						System.out.println("Driver Id:");
						driverId = scanString.nextLine().trim();
						System.out.println("Driver Name: ");
						driverName = scanString.nextLine().trim();
						System.out.println("Driver Status:");
						driverStatus = scanString.nextLine().trim();
						System.out.println("Is Driver handicapped? (0 or 1) :");
						isHandicap = scanString.nextLine().trim();
						boolean handicap = Integer.parseInt(isHandicap) == 1;
						status = driver.addDriverInfo(dbConn, driverId, driverName, driverStatus, handicap);
						if (status == 0) {
							System.out.println("Failed to add driver information");
						} else {
							System.out.println("Driver -> " + driverId + " inserted successfully");
						}
						System.out.println();
					}
					case 2 -> {
						String driverId, driverName, isHandicap;
						System.out.println("Driver Id:");
						driverId = scanString.nextLine().trim();
						System.out.println("Driver Name: ");
						driverName = scanString.nextLine().trim();
						System.out.println("Is Driver handicapped? (0 or 1) :");
						isHandicap = scanString.nextLine().trim();
						boolean handicap = Integer.parseInt(isHandicap) == 1;
						status = driver.updateDriverInfo(dbConn, driverId, driverName, handicap);
						if (status == 0) {
							System.out.println("Failed to update driver information");
						} else {
							System.out.println("Driver -> " + driverId + " updated successfully");
						}
						System.out.println();
					}
					case 3 -> {
						String driverID;
						System.out.println("Driver Id: ");
						driverID = scanString.nextLine().trim();
						status = driver.deleteDriverInfo(dbConn, driverID);
						if (status == 0) {
							System.out.println("Failed to delete parking lot");
						} else {
							System.out.println("Driver ID -> " + driverID + " deleted successfully");
						}
						System.out.println();
					}
					case 4 -> {
						String driverID;
						System.out.println("Driver Id: ");
						driverID = scanString.nextLine().trim();
						driver.getDriverStatus(dbConn, driverID);
						System.out.println();
					}
					case 5 -> {
						driver.printAllDriverInfo(dbConn);
						System.out.println();
					}
					case 6 -> {
						String driverID;
						System.out.println("Driver Id: ");
						driverID = scanString.nextLine().trim();
						status = driver.getDriverInfo(dbConn, driverID);
						if (status == 0) {
							System.out.println("Failed to retrieve driver record for driver_id -> " + driverID);
						} else {
							System.out.println("Driver ID -> " + driverID + " detail retrieved successfully");
						}
						System.out.println();
					}
					default -> System.out.println("Invalid option. Please try again.");
				}
			} while (!exit);
		} catch (Exception e) {
			System.err.println("Caught exception in method OptionDrivers: " + e);
		}
	}

	private static void OptionVehicles(Connection dbConn) {
		boolean exit = false;
		int ch = 0;
		int status = 0;
		try {
			Vehicle vehicle = new Vehicle();
			PermitForVehicle permitForVehicle = new PermitForVehicle();
			do {
				System.out.println("Manage Vehicle(s) Information:");
				System.out.println("1. Add vehicle");
				System.out.println("2. Update vehicle (color)");
				System.out.println("3. Delete vehicle");
				System.out.println("4. List all vehicles");
				System.out.println("5. List vehicle info (given vehicle number)");
				System.out.println("0. Go back to main menu");

				System.out.println("\nManage Vehicle Permit(s):");
				System.out.println("6. Add vehicle to permit");
				System.out.println("7. Remove vehicle from permit");
				System.out.println("8. Update vehicle space type in permit");
				System.out.println("9. List vehicles on permit (given permit Id)");
				System.out.println("10. List all vehicles with unexpired permit");
				System.out.println("11. List all vehicles with permits");
				System.out.println("0. Go back to main menu");
				System.out.println("Choose an option (0-11): ");
				ch = scanInt.nextInt();
				switch (ch) {
					case 0 -> exit = true;
					case 1 -> {
						String car_license_number, driver_id, model, color, manufacturer;
						int year;

						System.out.println("Enter Vehicle Number:");
						car_license_number = scanString.nextLine();
						System.out.println("Enter Driver ID:");
						driver_id = scanString.nextLine();
						System.out.println("Enter Manufacturer:");
						manufacturer = scanString.nextLine();
						System.out.println("Enter Model:");
						model = scanString.nextLine();
						System.out.println("Enter Color:");
						color = scanString.nextLine();
						System.out.println("Enter Year:");
						year = scanInt.nextInt();

						status = vehicle.addVehicleInfo(dbConn, car_license_number, driver_id, model, color, manufacturer, year);

						if (status == 0) {
							System.out.println("Failed to add vehicle.");
						} else {
							System.out.println("Vehicle added successfully.");
						}
						System.out.println();
					}
					case 2 -> {
						String car_license_number, newColor;

						System.out.println("Enter Vehicle Number:");
						car_license_number = scanString.nextLine();
						System.out.println("Enter New Color:");
						newColor = scanString.nextLine();

						status = vehicle.updateVehicleInfo(dbConn, car_license_number, newColor);

						if (status == 0) {
							System.out.println("Failed to update vehicle color.");
						} else {
							System.out.println("Vehicle color updated successfully.");
						}
						System.out.println();
					}
					case 3 -> {
						String car_license_number;

						System.out.println("Enter Vehicle Number:");
						car_license_number = scanString.nextLine();

						status = vehicle.deleteVehicleInfo(dbConn, car_license_number);

						if (status == 0) {
							System.out.println("Failed to delete vehicle.");
						} else {
							System.out.println("Vehicle deleted successfully.");
						}
						System.out.println();
					}
					case 4 -> {
						vehicle.printAllVehicleInfo(dbConn);
						System.out.println();
					}
					case 5 -> {
						String car_license_number;

						System.out.println("Enter Vehicle Number:");
						car_license_number = scanString.nextLine();

						vehicle.getVehicleInfo(dbConn, car_license_number);
						System.out.println();
					}
					case 6 -> {
						int spaceTypeInt;
						String carLicenseNumber;
						int permitId;
						System.out.println("Permit Id:");
						permitId = scanInt.nextInt();
						System.out.println("Enter Vehicle Number:");
						carLicenseNumber = scanString.nextLine();
						System.out.println("Space Type (1. Regular, 2. Compact Car, 3. Electric, 4. Handicap):");
						spaceTypeInt = scanInt.nextInt();
						status = permitForVehicle.addVehicleToPermit(dbConn, permitId, carLicenseNumber, mapSpaceType(spaceTypeInt));
						System.out.println(status == 0 ? "Failed to add vehicle to permit" : "Vehicle added to permit successfully");
						System.out.println();
					}
					case 7 -> {
						String carLicenseNumber;
						int permitId;
						System.out.println("Permit Id:");
						permitId = scanInt.nextInt();
						System.out.println("Enter Vehicle Number:");
						carLicenseNumber = scanString.nextLine();
						status = permitForVehicle.removeVehicleFromPermit(dbConn, permitId, carLicenseNumber);
						System.out.println(status == 0 ? "Failed to remove vehicle from permit" : "Vehicle removed from permit successfully");
						System.out.println();
					}
					case 8 -> {
						int newSpaceType;
						String carLicenseNumber;
						int permitId;
						System.out.println("Permit Id:");
						permitId = scanInt.nextInt();
						System.out.println("Enter Vehicle Number:");
						carLicenseNumber = scanString.nextLine();
						System.out.println("New Space Type (1. Regular, 2. Compact Car, 3. Electric, 4. Handicap):");
						newSpaceType = scanInt.nextInt();
						status = permitForVehicle.updateVehicleToPermit(dbConn, permitId, carLicenseNumber, mapSpaceType(newSpaceType));
						System.out.println(status == 0 ? "Failed to update vehicle space type" : "Vehicle space type updated successfully");
						System.out.println();
					}
					case 9 -> {
						String permitId;
						System.out.println("Permit Id:");
						permitId = scanString.nextLine();
						permitForVehicle.GetVehicleOnPermit(dbConn, permitId);
						System.out.println();
					}
					case 10 -> {
						permitForVehicle.listUnexpiredVehiclePermits(dbConn);
						System.out.println();
					}
					case 11 -> {
						permitForVehicle.listAllVehiclePermits(dbConn);
						System.out.println();
					}
					default -> System.out.println("Invalid option. Please try again.");
				}
			} while (!exit);
		} catch (Exception e) {
			System.err.println("Caught exception in method OptionVehicles: " + e);
		}
	}

	private static void OptionPermits(Connection dbConn) {
		boolean exit = false;
		int ch = 0;
		int status = 0;
		try {
			Permit permit = new Permit();
			do {
				System.out.println("Manage Permit(s) Information:");
				System.out.println("1. Add permit");
				System.out.println("2. Update permit (start time/expiry time)");
				System.out.println("3. Delete permit");
				System.out.println("4. Update zone in permit");
				System.out.println("5. List all permits");
				System.out.println("6. List information about permit (given permit id)");
				System.out.println("0. Go back to main menu");
				System.out.println("Choose an option (0-6): ");
				ch = scanInt.nextInt();
				switch (ch) {
					case 0 -> exit = true;
					case 1 -> {
						int permitTypeInt;
						String permitType = "none";
						String driverId, zoneId, startTime, expiryTime;
						System.out.println("Driver Id:");
						driverId = scanString.nextLine();
						System.out.println("Permit type (press 1-5):");
						System.out.println("(commuter: 1, peak hours: 2, residential: 3, park and ride: 4, special event: 5)");
						permitTypeInt = scanInt.nextInt();
						switch (permitTypeInt) {
							case 1 -> permitType = "commuter";
							case 2 -> permitType = "peak hours";
							case 3 -> permitType = "residential";
							case 4 -> permitType = "park and ride";
							case 5 -> permitType = "special event";
						}
						System.out.println("Zone Id: ");
						zoneId = scanString.nextLine();
						System.out.println("Start Date/Time: ");
						startTime = scanString.nextLine();
						System.out.println("Expiry Date/Time: ");
						expiryTime = scanString.nextLine();
						int permitId = permit.addPermitInfo(dbConn, driverId, permitType, zoneId, startTime, expiryTime);
						if (permitId == 0) {
							System.out.println("Failed to add permit");
						} else {
							System.out.println("Permit added successfully: Permit Id: " + permitId);
						}
						System.out.println();
					}
					case 2 -> {
						int permitId;
						String startTime, expiryTime;
						System.out.println("Permit Id:");
						permitId = scanInt.nextInt();
						System.out.println("Start Date/Time: ");
						startTime = scanString.nextLine();
						System.out.println("Expiry Date/Time: ");
						expiryTime = scanString.nextLine();
						status = permit.updatePermitInfo(dbConn, permitId, startTime, expiryTime);
						if (status == 0) {
							System.out.println("Failed to update permit info");
						}  else {
							System.out.println("Permit updated successfully");
						}
						System.out.println();
					}
					case 3 -> {
						int permitId;
						System.out.println("Permit id: ");
						permitId = scanInt.nextInt();
						status = permit.deletePermitInfo(dbConn, permitId);
						if (status == 0) {
							System.out.println("Failed to delete permit");
						} else {
							System.out.println("Permit deleted successfully");
						}
						System.out.println();
					}
					case 4 -> {
						int permitId;
						String zoneId;
						System.out.println("Permit Id:");
						permitId = scanInt.nextInt();
						System.out.println("Zone Id: ");
						zoneId = scanString.nextLine();
						status = permit.updateZoneInPermit(dbConn, permitId, zoneId);
						if (status == 0) {
							System.out.println("Failed to update zone in permit");
						} else {
							System.out.println("Updated zone in permit successfully");
						}
						System.out.println();
					}
					case 5 -> {
						permit.getPermitInfo(dbConn);
						System.out.println();
					}
					case 6 -> {
						int permitId;
						System.out.println("Permit Id: ");
						permitId = scanInt.nextInt();
						permit.getPermitInfo(dbConn, permitId);
						System.out.println();
					}
				}
			} while (!exit);
		} catch (SQLIntegrityConstraintViolationException duplicateException) {
			System.out.println("Integrity constraint violated for the operation");
		}
		catch (Exception e) {
			System.err.println("Caught exception in method OptionPermits: " + e);
		}
	}

	private static void OptionCitations(Connection dbConn) {
		boolean exit = false;
		int ch = 0;
		int status = 0;
		try {
			do {
				Citation citation = new Citation();
				System.out.println("Manage Citation(s) information:");
				System.out.println("Driver menu:");
				System.out.println("1. Pay citation");
				System.out.println("2. Request citation appeal");
				System.out.println("3. View unpaid valid citations (given vehicle id)");
				System.out.println("4. View all citations (given vehicle id)");
				System.out.println("0. Go back to main menu");

				System.out.println("\nSecurity/Admin menu:");
				System.out.println("5. Check violation");
				System.out.println("6. Generate citation");
				System.out.println("7. View citation appeal requests");
				System.out.println("8. Process citation appeal request");
				System.out.println("9. View all citations");
				System.out.println("0. Go back to main menu");
				System.out.println("Choose an option (0-9): ");
				ch = scanInt.nextInt();
				switch (ch) {
					case 0 -> exit = true;
					case 1 -> {
						System.out.println("Citation Id:");
						int citationId = scanInt.nextInt();
						status = citation.driverMakePayment(dbConn, citationId);
						if (status == 0) {
							System.out.println("Failed to make payment for citation");
						} else {
							System.out.println("Citation payed successfully");
						}
						System.out.println();
					}
					case 2 -> {
						System.out.println("Citation Id:");
						int citationId = scanInt.nextInt();
						System.out.println("Appeal comments (text):");
						String citationComments = scanString.nextLine();
						status = citation.driverRequestCitationAppeal(dbConn, citationId, citationComments);
						if (status == 0) {
							System.out.println("Failed to request appeal for citation");
						} else {
							System.out.println("Citation appeal requested successfully");
						}
						System.out.println();
					}
					case 3 -> {
						System.out.println("Vehicle Number:");
						String vehicleNum = scanString.nextLine();
						citation.getUnpaidValidCitationsForVehicle(dbConn, vehicleNum);
						System.out.println();
					}
					case 4 -> {
						System.out.println("Vehicle Number:");
						String vehicleNum = scanString.nextLine();
						citation.getCitationsForVehicle(dbConn, vehicleNum);
						System.out.println();
					}
					case 5 -> {
						System.out.println("Vehicle Number:");
						String vehicleNum = scanString.nextLine();
						System.out.println("Lot Name:");
						String lotName = scanString.nextLine();
						System.out.println("Space where vehicle is parked (1. Regular, 2. Compact Car, 3. Electric, 4. Handicap):");
						int spaceType = scanInt.nextInt();
						String spaceTypeStr = mapSpaceType(spaceType);
						Permit p = new Permit();
						String violation = p.checkParkingViolation(dbConn, vehicleNum, lotName, spaceTypeStr);
						System.out.println("Vehicle has: " + violation);
						System.out.println();
					}
					case 6 -> {
						System.out.println("Vehicle Number:");
						String vehicleNum = scanString.nextLine();
						System.out.println("Lot name:");
						String lotName = scanString.nextLine();
						System.out.println("Space where vehicle is parked (1. Regular, 2. Compact Car, 3. Electric, 4. Handicap):");
						int spaceType = scanInt.nextInt();
						String spaceTypeStr = mapSpaceType(spaceType);
						if(!citation.generateCitation(dbConn, vehicleNum, lotName, spaceTypeStr)) {
							System.out.println("Citation generation failed");
						}
						System.out.println();
					 }
					case 7 -> {
						citation.adminViewCitationAppeals(dbConn);
						System.out.println();
					}
					case 8 -> {
						System.out.println("Citation Id:");
						int citationId = scanInt.nextInt();
						System.out.println("Accept appeal (1), Reject appeal (0):");
						int accept = scanInt.nextInt();
						status = citation.adminProcessCitationAppeal(dbConn, citationId, accept != 0);
						if (status == 0) {
							System.out.println("Failed to accept/deny appeal status");
						} else {
							System.out.println("Citation appeal request processed successfully");
						}
						System.out.println();
					}
					case 9 -> {
						citation.getCitationInfo(dbConn);
						System.out.println();
					}
				}
			} while (!exit);
		} catch (SQLIntegrityConstraintViolationException duplicateException) {
			System.out.println("Integrity constraint violated for the operation");
		} catch(Exception e){
			System.err.println("Caught exception in method OptionCitations: " + e);
		}
	}
	
	private static void OptionReports(Connection dbConn) {
		boolean exit = false;
		int ch = 0;
		int status = 0;
		try {
			do {
				ParkingLot lot = new ParkingLot();
				LotContainsZone lotContainsZone = new LotContainsZone(lot);
				Reports reports = new Reports(lotContainsZone);
				System.out.println("Generate Reports:");
				System.out.println("1. Get citations (given time range)");
				System.out.println("2. Get assigned zones as (lot, zone) pairs ");
				System.out.println("3. Get number of cars currently in violation");
				System.out.println("4. Get employees with permit in zone");
				System.out.println("5. Get permits (given driver Id)");
				System.out.println("6. Get free space in lot (given space type)");
				System.out.println("0. Go back to main menu");
				System.out.println("Choose an option (0-6): ");
				ch = scanInt.nextInt();
				switch (ch) {
					case 0 -> exit = true;
					case 1 -> {
						System.out.println("Parking lot name: ");
						String lotName = scanString.nextLine();
						System.out.println("Start Date/Time: ");
						String startTime = scanString.nextLine();
						System.out.println("Expiry Date/Time: ");
						String endTime = scanString.nextLine();
						reports.getCitationForDuration(dbConn, lotName, startTime, endTime);
						System.out.println();
					}
					case 2 -> {
						reports.getZonesInLot(dbConn);
						System.out.println();
					}
					case 3 -> {
						int vehicles = reports.getVehiclesInViolation(dbConn);
						System.out.println("Number of vehicles currently in violation: " + vehicles);
						System.out.println();
					}
					case 4 -> {
						int numEmployees = 0;
						String zoneId;
						System.out.println("Zone Id: ");
						zoneId = scanString.nextLine();
						numEmployees = reports.getEmployeesWithPermitInZone(dbConn, zoneId);
						System.out.println(numEmployees + " distinct employees found having permit in zone " + zoneId);
						System.out.println();
					}
					case 5 -> {
						String driverId;
						System.out.println("Driver Id: ");
						driverId = scanString.nextLine();
						reports.getPermitRecordsForDriver(dbConn, driverId);
						System.out.println();
					}
					case 6 -> {
						System.out.println("Enter Lot Name:");
						String lotName = scanString.nextLine();
						System.out.println("Enter Space Type (1. Regular, 2. Compact Car, 3. Electric, 4. Handicap):");
						int spaceTypeOption = scanInt.nextInt();
						String spaceType = mapSpaceType(spaceTypeOption);
						int spaceNum = reports.getSpaceNumberForType(dbConn, lotName, spaceType);
						if (spaceNum == -1) {
							System.out.println("No space available for the type " + spaceType + " in the lot: " + lotName);
						} else {
							System.out.println("Available space: " + spaceNum + " (lot: " + lotName + ", type: " + spaceType + ")");
						}
						System.out.println();
					}

				}
			} while (!exit);
		} catch (Exception e) {
			System.err.println("Caught exception in method OptionReports: " + e);
		}
	}

	public static void main(String[] args) {
		boolean exit = false;
		int ch = 0;
		Connection dbConnection = null;

		try {
			dbConnection = DBConnector.getDbConnection();
			if (dbConnection == null) {
				System.out.println("Failed to connect to database");
			}
			else {
				System.out.println("Running WolfPack Parking Management");
				do {
					System.out.println("1. Parking Lots");
					System.out.println("2. Spaces");
					System.out.println("3. Drivers");
					System.out.println("4. Vehicles");
					System.out.println("5. Permits");
					System.out.println("6. Citations");
					System.out.println("7. Reports");
					System.out.println("0. Exit");
					System.out.println("Choose an option (0-7) :");
					ch = scanInt.nextInt();
					switch (ch) {
						case 0 -> exit = true;
						case 1 -> OptionParkingLots(dbConnection);
						case 2 -> OptionSpaces(dbConnection);
						case 3 -> OptionDrivers(dbConnection);
						case 4 -> OptionVehicles(dbConnection);
						case 5 -> OptionPermits(dbConnection);
						case 6 -> OptionCitations(dbConnection);
						case 7 -> OptionReports(dbConnection);
					}
				} while (!exit);
			}
		} catch (Exception e) {
			System.err.println("Caught exception in main: " + e);
		} finally {
			DBConnector.close(dbConnection);
		}
	}
	

}