package db_models;

import db.DBUtilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

public class ParkingLot {


    private boolean isValidParameter(String parameter) {
        return parameter!=null && !parameter.trim().isEmpty();
    }

    public int addParkingLotInfo(Connection dbConn, String lotName, String lotAddress) throws Exception {
        int rowsAffected = 0;

        if (!isValidParameter(lotName)) {
            System.out.println("Parking lot name cannot be empty");
            return rowsAffected;
        }
        if (!isValidParameter(lotAddress)) {
            System.out.println("Parking lot address cannot be empty");
            return rowsAffected;
        }

        String sql = "INSERT INTO ParkingLot (lot_name, lot_address) VALUES (?, ?)";

        try (PreparedStatement preparedStatement = dbConn.prepareStatement(sql)) {
            preparedStatement.setString(1, lotName);
            preparedStatement.setString(2, lotAddress);
            rowsAffected = preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.println("Caught exception in method addParkingLotInfo: " + e);
        }

        return rowsAffected;
    }

    public int updateParkingLot(Connection dbConn, String lotName, String newLotName, String newLotAddress) throws Exception {
        int rowsAffected = 0;
        if (!isValidParameter(lotName)) {
            System.out.println("Incorrect parking lot name parameter value");
            return rowsAffected;
        }
        if (!isValidParameter(newLotName)) {
            System.out.println("Incorrect new parking lot name parameter value");
            return rowsAffected;
        }
        if (!isValidParameter(newLotAddress)) {
            System.out.println("Incorrect new lot address parameter value");
            return rowsAffected;
        }

        String sql = "UPDATE ParkingLot SET lot_name = ?, lot_address = ? WHERE lot_name = ?";
        try (PreparedStatement preparedStatement = dbConn.prepareStatement(sql)) {
            preparedStatement.setString(1, newLotName);
            preparedStatement.setString(2, newLotAddress);
            preparedStatement.setString(3, lotName);
            rowsAffected = preparedStatement.executeUpdate();
        }  catch (Exception e) {
            System.err.println("Caught exception in method updateParkingLot: " + e);
        }

        return rowsAffected;
    }

    public int deleteParkingLotInfo(Connection dbConn, String lotName) throws Exception {
        int rowsAffected = 0;
        if (!isValidParameter(lotName)) {
            System.out.println("Incorrect parking lot name parameter value");
            return rowsAffected;
        }

        String sql = "DELETE FROM ParkingLot WHERE lot_name = ?";
        try (PreparedStatement preparedStatement = dbConn.prepareStatement(sql)) {
            preparedStatement.setString(1, lotName);
            rowsAffected = preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.println("Caught exception in method deleteParkingLotInfo: " + e);
        }

        return rowsAffected;
    }

    public void getParkingLotInfo(Connection dbConn) {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM ParkingLot";
            stmt = dbConn.createStatement();
            rs = stmt.executeQuery(sql);
            DBUtilities.printResult(rs);
        } catch (Exception e) {
            System.err.println("Caught exception in method getParkingLotInfo: " + e);
        } finally {
            DBUtilities.close(stmt);
            DBUtilities.close(rs);
        }
    }

    public void getParkingLotInfo(Connection dbConn, String lotName) {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            if (!isValidParameter(lotName)) {
                System.out.println("Incorrect parking lot name parameter value");
                return;
            }
            String sql = "SELECT * FROM ParkingLot WHERE lot_name = ?";
            preparedStatement = dbConn.prepareStatement(sql);
            preparedStatement.setString(1, lotName);
            rs = preparedStatement.executeQuery();
            DBUtilities.printResult(rs);
        } catch (Exception e) {
            System.err.println("Caught exception in method getParkingLotInfo: " + e);
        } finally {
            DBUtilities.close(preparedStatement);
            DBUtilities.close(rs);
        }
    }
    
    public boolean isParkingLotPresent(Connection dbConn, String lotName) {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            if (!isValidParameter(lotName)) {
                System.out.println("Incorrect parking lot name parameter value");
                return false;
            }
            String sql = "SELECT count(*) FROM ParkingLot WHERE lot_name = ?";
            preparedStatement = dbConn.prepareStatement(sql);
            preparedStatement.setString(1, lotName);
            rs = preparedStatement.executeQuery();
            int count = 0;
            if (rs.next()) {
            	count = rs.getInt(1);
            	return count > 0;
            }
        } catch (Exception e) {
            System.err.println("Caught exception in method getParkingLotInfo: " + e);
        } finally {
            DBUtilities.close(preparedStatement);
            DBUtilities.close(rs);
        }
        return false;
    }

    public List<String> getZonesInLot(Connection dbConn, String lotName) {
        List<String> zoneList = new ArrayList<String>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            if (!isValidParameter(lotName)) {
                System.out.println("Lot Name -> " + lotName + " should be non-empty value");
                return zoneList;
            }
            if (!isParkingLotPresent(dbConn, lotName)) {
                System.out.println("Parking Lot -> " + lotName + " NOT Present");
                return zoneList;
            }
            String sql = "SELECT zone_id FROM LotContainsZone WHERE lot_name = ?";
            stmt = dbConn.prepareStatement(sql);
            stmt.setString(1, lotName);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String zoneId = rs.getString("zone_id");
                zoneList.add(zoneId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtilities.close(rs);
            DBUtilities.close(stmt);
        }
        return zoneList;
    }

}