package db_models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import db.DBUtilities;

public class LotContainsZone {
	
	ParkingLot lot;
	static Scanner scanString = new Scanner(System.in);
	
	public LotContainsZone(ParkingLot lot) {
		this.lot = lot;
	}

	private boolean isValidParameter(String parameter) {
	    return parameter != null && !parameter.trim().isEmpty();
	}

	public int assignZoneToLot(Connection dbConn, String lotName, String zoneId) throws Exception {
	    PreparedStatement preparedStatement = null;
	    int rowsAffected = 0, status = 0;
	    try {
	    	dbConn.setAutoCommit(false);
	        if (!isValidParameter(lotName) || !isValidParameter(zoneId)) 
	            return 0;
	        if (!this.lot.isParkingLotPresent(dbConn, lotName)) {
	        	System.out.println("Parking Lot -> " + lotName + " NOT present. Hence, inserting Parking lot details.");
				String lotAddress;
				System.out.println("Enter Parking Lot address for " + lotName + ": ");
				lotAddress = scanString.nextLine().trim();
				status = lot.addParkingLotInfo(dbConn, lotName, lotAddress);
				if (status == 0) {
					System.out.println("Failed to add parking lot");
					dbConn.rollback();
					return rowsAffected;
				} else 
					System.out.println("Parking lot -> " + lotName + " inserted successfully");
	    	}
	        String sql = "INSERT INTO LotContainsZone (lot_name, zone_id) VALUES (?, ?)";
	        preparedStatement = dbConn.prepareStatement(sql);
	        preparedStatement.setString(1, lotName);
	        preparedStatement.setString(2, zoneId);
	        rowsAffected = preparedStatement.executeUpdate();
	        dbConn.commit();
	    } catch (SQLIntegrityConstraintViolationException duplicateException) {
        	System.out.println("Lot Name -> " + lotName + " already assigned zone -> " + zoneId);
        	dbConn.rollback();
        } catch (Exception e) {
	        e.printStackTrace();
	        dbConn.rollback();
	    } finally {
	    	dbConn.setAutoCommit(true);
	        DBUtilities.close(preparedStatement);
	    }
	    return rowsAffected;
	}

	public int deleteZoneFromLot(Connection dbConn, String lotName, String zoneId) {
	    PreparedStatement preparedStatement = null;
	    int rowsAffected = 0;
	    try {
	        if (!isValidParameter(lotName) || !isValidParameter(zoneId)) {
	        	System.out.println("Incorrect parameters. LotName and ZoneID must not be empty.");
	        	return 0;
	        }
	        if (!this.lot.isParkingLotPresent(dbConn, lotName)) {
	        	System.out.println("Parking Lot -> " + lotName + " NOT present; hence not deleting any records");
	        	return 0;
	    	}
	        String sql = "DELETE FROM LotContainsZone WHERE lot_name = ? AND zone_id = ?";
	        preparedStatement = dbConn.prepareStatement(sql);
	        preparedStatement.setString(1, lotName);
	        preparedStatement.setString(2, zoneId);
	        rowsAffected = preparedStatement.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        DBUtilities.close(preparedStatement);
	    }
	    return rowsAffected;
	}
	
//	public List<String> getAllZonesForLot(Connection dbConn, String lotName) {
//		List<String> zoneList = new ArrayList<String>();
//        PreparedStatement stmt = null;
//		ResultSet rs = null;
//        try {
//        	if (!this.lot.isParkingLotPresent(dbConn, lotName)) {
//        		System.out.println("Parking Lot -> " + lotName + " NOT Present");
//        		return zoneList;
//        	}
//        	String sql = "SELECT zone_id FROM LotContainsZone WHERE lot_name = ?";
//        	stmt = dbConn.prepareStatement(sql);
//        	stmt.setString(1, lotName);
//        	rs = stmt.executeQuery();
//	        while (rs.next()) {
//	        	String zoneId = rs.getString("zone_id");
//                zoneList.add(zoneId);
//	        }
//        } catch (Exception e) {
//        	e.printStackTrace();
//        } finally {
//        	DBUtilities.close(rs);
//        	DBUtilities.close(stmt);
//        }
//        return zoneList;
//	}
	
}

