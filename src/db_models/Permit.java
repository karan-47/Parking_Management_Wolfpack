package db_models;

import db.DBUtilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Permit {

    private boolean isValidParameter(String parameter) {
        return parameter!=null && !parameter.trim().isEmpty();
    }

    public int addPermitInfo(Connection dbConn, String driverId, String permitType, String zoneId,
                             String startDateTime, String expiryDateTime) throws Exception {
        int generatedPermitId = 0;
        PreparedStatement preparedStatement = null;

        if (!isValidParameter(driverId)) {
            System.out.println("Incorrect driver id parameter value");
            return generatedPermitId;
        }
        if (!isValidParameter(permitType)) {
            System.out.println("Incorrect permit type parameter value");
            return generatedPermitId;
        }
        if (!isValidParameter(zoneId)) {
            System.out.println("Incorrect zone id parameter value");
            return generatedPermitId;
        }
        if (!isValidParameter(startDateTime)) {
            System.out.println("Incorrect start date time parameter value");
            return generatedPermitId;
        }
        if (!isValidParameter(expiryDateTime)) {
            System.out.println("Incorrect expiry date time parameter value");
            return generatedPermitId;
        }

        try {
            dbConn.setAutoCommit(false);

            if (!canAssignPermit(dbConn, driverId, permitType)) {
                System.out.println("Limit for permit type (" + permitType + ") exceeds for driver ' "+ driverId + "'");
                dbConn.rollback();
                return generatedPermitId;
            }

            String sql = "INSERT INTO Permit (driver_id, permit_type, zone_id, permit_start_time, permit_expiry_time) VALUES (?, ?, ?, ?, ?)";
            preparedStatement = dbConn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, driverId);
            preparedStatement.setString(2, permitType);
            preparedStatement.setString(3, zoneId);
            preparedStatement.setString(4, startDateTime);
            preparedStatement.setString(5, expiryDateTime);
            int sqlStatus = preparedStatement.executeUpdate();

            if (sqlStatus > 0) {
                ResultSet generatedPermit = preparedStatement.getGeneratedKeys();
                if (generatedPermit.next()) {
                    generatedPermitId = generatedPermit.getInt(1);
                }
            }
            dbConn.commit();
        } catch (Exception e) {
            dbConn.rollback();
            System.err.println("Caught exception when adding new permit: " + e);
        } finally {
            dbConn.setAutoCommit(true);
            DBUtilities.close(preparedStatement);
        }

        return generatedPermitId;
    }

    public int updatePermitInfo(Connection dbConn, int permitId, String startDateTime, String expiryDateTime) throws Exception {
        int rowsAffected = 0;
        if (!isValidParameter(startDateTime)) {
            System.out.println("Incorrect start date time parameter value");
            return rowsAffected;
        }
        if (!isValidParameter(expiryDateTime)) {
            System.out.println("Incorrect expiry date time parameter value");
            return rowsAffected;
        }

        String sql = "UPDATE Permit SET permit_start_time = ?, permit_expiry_time = ? WHERE permit_id = ?";
        try(PreparedStatement preparedStatement = dbConn.prepareStatement(sql)) {
            preparedStatement.setString(1, startDateTime);
            preparedStatement.setString(2, expiryDateTime);
            preparedStatement.setInt(3, permitId);
            rowsAffected = preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.println("Caught exception in updatePermitInfo: " + e);
        }

        return rowsAffected;
    }

    public int updateZoneInPermit(Connection dbConn, int permitId, String zoneId) throws Exception {
        int rowsAffected = 0;
        if (!isValidParameter(zoneId)) {
            System.out.println("Incorrect zone id parameter value");
            return rowsAffected;
        }

        String sql = "UPDATE Permit SET zone_id = ? WHERE permit_id = ?";
        try(PreparedStatement preparedStatement = dbConn.prepareStatement(sql)) {
            preparedStatement.setString(1, zoneId);
            preparedStatement.setInt(2, permitId);
            rowsAffected = preparedStatement.executeUpdate();
        }  catch (Exception e) {
            System.err.println("Caught exception in updateZoneInPermit: " + e);
        }
        return rowsAffected;
    }

    public int deletePermitInfo(Connection dbConn, int permitId) throws Exception {
        int rowsAffected = 0;
        String sql = "DELETE FROM Permit WHERE permit_id = ?";
        try(PreparedStatement preparedStatement = dbConn.prepareStatement(sql)) {
            preparedStatement.setInt(1, permitId);
            rowsAffected = preparedStatement.executeUpdate();
        }  catch (Exception e) {
            System.err.println("Caught exception in deletePermitInfo: " + e);
        }
        return rowsAffected;
    }

    private boolean canAssignPermit(Connection dbConn, String driverId, String permitType) throws Exception {
        boolean canAssign = false;
        String sql;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        if (permitType.equals("special event") || permitType.equals("park and ride"))
        {
            sql = "SELECT IF((((driver_status = 'E' or driver_status = 'S') AND num_permits = 0)), 1, 0)" +
                    " AS AssignPermit FROM" +
                    " (SELECT driver_status," +
                    " (SELECT COUNT(*) AS permits_cnt FROM Driver D" +
                    " JOIN Permit P ON D.driver_id = P.driver_id" +
                    " WHERE" +
                    " D.driver_id = ?" +
                " AND permit_expiry_time > NOW()" +
                " AND permit_type IN ('special event', 'park and ride')) AS num_permits" +
                " FROM Driver WHERE driver_id = ?) as sq";
        }
        else {
            sql = "SELECT IF((" +
                    " ((driver_status = 'E') AND num_permits < 2)" +
                    " OR" +
                    " ((driver_status = 'S' OR driver_status = 'V') AND num_permits = 0)), 1, 0)" +
                    " AS AssignPermit FROM" +
                    " (SELECT driver_status," +
                    " (SELECT COUNT(*) AS permits_cnt FROM Driver D" +
                    " JOIN Permit P ON D.driver_id = P.driver_id" +
                    " WHERE" +
                    " D.driver_id = ?" +
                    " AND permit_expiry_time > NOW()" +
                    " AND permit_type NOT IN ('special event', 'park and ride')) AS num_permits" +
                    " FROM Driver WHERE driver_id = ?) as sq";
        }

        try {
            preparedStatement = dbConn.prepareStatement(sql);
            preparedStatement.setString(1, driverId);
            preparedStatement.setString(2, driverId);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                canAssign = rs.getString("AssignPermit").equals("1");
            }
        } catch (Exception e) {
            System.err.println("Caught exception in canAssignPermit: " + e);
        } finally {
            DBUtilities.close(preparedStatement);
            DBUtilities.close(rs);
        }

        return canAssign;
    }

    public boolean canAddVehicleToPermit(Connection dbConn, int permitId) throws Exception {
        boolean canAdd = false;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        String sql = "SELECT CASE" +
                " WHEN ((driver_status = 'E' AND vehicle_count < 2)" +
                " OR" +
                " (driver_status IN ('S', 'V') AND vehicle_count < 1))" +
                " THEN 1" +
                " ELSE 0" +
                " END AS Can_Add_Vehicle" +
                " FROM (" +
                " SELECT D.driver_status, COUNT(PFV.vehicle_num) AS vehicle_count" +
                " FROM Driver D" +
                " JOIN Permit P ON D.driver_id = P.driver_id" +
                " LEFT JOIN PermitForVehicle PFV ON P.permit_id = PFV.permit_id" +
                " WHERE P.permit_id = ?)" +
                " AS Subquery";

        try {
            preparedStatement = dbConn.prepareStatement(sql);
            preparedStatement.setInt(1, permitId);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                canAdd = rs.getString("Can_Add_Vehicle").equals("1");
            }
        } catch (Exception e) {
            System.err.println("Caught exception in canAddVehicleToPermit: " + e);
        } finally {
            DBUtilities.close(preparedStatement);
            DBUtilities.close(rs);
        }

        return canAdd;
    }

    private boolean checkVehicleHasSomePermit(Connection dbConn, String vehicleNum) throws Exception {
        String sql = "SELECT permit_id FROM PermitForVehicle WHERE vehicle_num = ?";
        try(PreparedStatement preparedStatement = dbConn.prepareStatement(sql)) {
            preparedStatement.setString(1, vehicleNum);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }

    private ResultSet retrieveAllPermitsForVehicle(Connection dbConn, String vehicleNum, String lotName, String spaceType) throws Exception {
        String sql = "SELECT Permit.permit_id, permit_expiry_time < NOW() AS expired FROM Permit" +
                " JOIN PermitForVehicle ON Permit.permit_id = PermitForVehicle.permit_id" +
                " WHERE" +
                " Permit.zone_id IN (SELECT zone_id FROM LotContainsZone WHERE lot_name = ?) AND" +
                " PermitForVehicle.vehicle_num = ? AND" +
                " PermitForVehicle.space_type = ? AND" +
                " Permit.permit_start_time <= NOW()";
        PreparedStatement preparedStatement = dbConn.prepareStatement(sql);
        preparedStatement.setString(1, lotName);
        preparedStatement.setString(2, vehicleNum);
        preparedStatement.setString(3, spaceType);
        return preparedStatement.executeQuery();
    }

    public String checkParkingViolation(Connection dbConn, String vehicleNum, String lotName, String spaceType) throws Exception {
        String violation = "no permit";
        boolean allPermitsExpired = true;
        ResultSet rs = null;

        try {
            dbConn.setAutoCommit(false);
            do {
                // if no permit exists for vehicle, then return with no permit (default)
                boolean exists = checkVehicleHasSomePermit(dbConn, vehicleNum);
                if (!exists) {
                    dbConn.rollback();
                    break;
                }

                // get all permits of vehicle for zones present in lotName and permit applicable for given space type
                rs = retrieveAllPermitsForVehicle(dbConn, vehicleNum, lotName, spaceType);
                if(!rs.isBeforeFirst()) {
                    violation = "invalid permit";
                    dbConn.rollback();
                    break;
                }

                // find at least one unexpired permit from the retrieved permits
                while (rs.next()) {
                    boolean expiredPermit = rs.getBoolean("expired");
                    if (!expiredPermit) {
                        allPermitsExpired = false;
                        break;
                    }
                }
                violation = allPermitsExpired ? "expired permit" : "no violation";
                dbConn.commit();

            } while (false);
        } catch (Exception e) {
            dbConn.rollback();
            System.err.println("Caught exception in method checkParkingViolation: " + e);
        } finally {
            DBUtilities.close(rs);
            dbConn.setAutoCommit(true);
        }

        return violation;
    }

    public void getPermitInfo(Connection dbConn) {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM Permit";
            stmt = dbConn.createStatement();
            rs = stmt.executeQuery(sql);
            DBUtilities.printResult(rs);
        } catch (Exception e) {
            System.err.println("Caught exception in method getPermitInfo: " + e);
        } finally {
            DBUtilities.close(stmt);
            DBUtilities.close(rs);
        }
    }

    public void getPermitInfo(Connection dbConn, int permitId) {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM Permit WHERE permit_id = ?";
            preparedStatement = dbConn.prepareStatement(sql);
            preparedStatement.setInt(1, permitId);
            rs = preparedStatement.executeQuery();
            DBUtilities.printResult(rs);
        } catch (Exception e) {
            System.err.println("Caught exception in method getPermitInfo: " + e);
        } finally {
            DBUtilities.close(preparedStatement);
            DBUtilities.close(rs);
        }
    }

}