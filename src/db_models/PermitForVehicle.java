package db_models;

import db.DBUtilities;

import java.sql.*;

public class PermitForVehicle {

    // Other methods...

    public int addVehicleToPermit(Connection dbConn, int permitId, String carLicenseNumber, String spaceType) throws Exception {
        int rowsAffected = 0;
        try {

            // Check if the vehicle is already associated with the permit
            if (isVehicleInPermit(dbConn, permitId, carLicenseNumber)) {
                System.out.println("Vehicle is already associated with the permit.");
                return rowsAffected;
            }

            // Check if it's possible to add a vehicle to the permit using PermitValidator
            Permit permitValidator = new Permit();
            if (!permitValidator.canAddVehicleToPermit(dbConn, permitId)) {
                System.out.println("Validation for max vehicle count failed. (permit does not exist/max limit reached)");
                return rowsAffected;
            }

            boolean isHandicap = false;
            String driverSql = "SELECT is_handicap FROM Driver WHERE driver_id = " +
                    "(SELECT driver_id FROM Vehicle WHERE vehicle_num = ?)";
            PreparedStatement preparedStatement1 = dbConn.prepareStatement(driverSql);
            preparedStatement1.setString(1, carLicenseNumber);
            ResultSet rs = preparedStatement1.executeQuery();
            while (rs.next()) {
                isHandicap = rs.getBoolean("is_handicap");
            }

            if(!isHandicap && spaceType.equals("handicap")) {
                System.out.println("Cannot add handicap space to non-handicap driver");
                return rowsAffected;
            }

            // Update the SQL statement to include the correct table and columns
            String insertSql = "INSERT INTO PermitForVehicle(permit_id, vehicle_num, space_type) VALUES (?, ?, ?)";
            try (PreparedStatement insertStmt = dbConn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, permitId);
                insertStmt.setString(2, carLicenseNumber);
                insertStmt.setString(3, spaceType);

                rowsAffected = insertStmt.executeUpdate();

            }

        } catch (Exception e) {
            throw e;
        }
        return rowsAffected;
    }

    public int removeVehicleFromPermit(Connection dbConn, int permitId, String carLicenseNumber) throws Exception {
        int rowsAffected = 0;
        try {
            // Check if the vehicle is associated with the permit
            if (!isVehicleInPermit(dbConn, permitId, carLicenseNumber)) {
                System.out.println("Vehicle is not associated with the permit.");
                return rowsAffected;
            }

            // Update the SQL statement to include the correct table and columns
            String sql = "DELETE FROM PermitForVehicle WHERE permit_id = ? AND vehicle_num = ?";
            try (PreparedStatement preparedStatement = dbConn.prepareStatement(sql)) {
                preparedStatement.setInt(1, permitId);
                preparedStatement.setString(2, carLicenseNumber);

                rowsAffected = preparedStatement.executeUpdate();

            }

        } catch (Exception e) {
            throw e;
        }
        return rowsAffected;
    }

    public int updateVehicleToPermit(Connection dbConn, int permitId, String carLicenseNumber, String newSpaceType) throws Exception {
        int rowsAffected = 0;
        try {
            // Check if the vehicle is associated with the permit
            if (!isVehicleInPermit(dbConn, permitId, carLicenseNumber)) {
                System.out.println("Vehicle is not associated with the permit.");
                return rowsAffected;
            }

            // Update the SQL statement to include the correct table and columns
            String sql = "UPDATE PermitForVehicle SET space_type = ? WHERE permit_id = ? AND vehicle_num = ?";
            try (PreparedStatement preparedStatement = dbConn.prepareStatement(sql)) {
                preparedStatement.setString(1, newSpaceType);
                preparedStatement.setInt(2, permitId);
                preparedStatement.setString(3, carLicenseNumber);

                rowsAffected = preparedStatement.executeUpdate();

            }

        } catch (Exception e) {
            throw e;
        }
        return rowsAffected;
    }

    public void GetVehicleOnPermit(Connection dbConn, String permitId) {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT vehicle_num, space_type FROM PermitForVehicle WHERE permit_id = ?";
            preparedStatement = dbConn.prepareStatement(sql);
            preparedStatement.setString(1, permitId);
            rs = preparedStatement.executeQuery();
            DBUtilities.printResult(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(preparedStatement);
            close(rs);
        }
    }

    public void listAllVehiclePermits(Connection dbConn) {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT vehicle_num, PermitForVehicle.permit_id, permit_type, zone_id, permit_expiry_time, " +
                    "space_type FROM PermitForVehicle JOIN " +
                    "Permit on PermitForVehicle.permit_id = Permit.permit_id";
            preparedStatement = dbConn.prepareStatement(sql);
            rs = preparedStatement.executeQuery();
            DBUtilities.printResult(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(preparedStatement);
            close(rs);
        }
    }

    public void listUnexpiredVehiclePermits(Connection dbConn) {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT vehicle_num, PermitForVehicle.permit_id, permit_type, zone_id, permit_expiry_time, " +
                    "space_type FROM PermitForVehicle JOIN " +
                    "Permit on PermitForVehicle.permit_id = Permit.permit_id" +
                    " WHERE permit_expiry_time > NOW()";
            preparedStatement = dbConn.prepareStatement(sql);
            rs = preparedStatement.executeQuery();
            DBUtilities.printResult(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(preparedStatement);
            close(rs);
        }
    }

    private boolean isVehicleInPermit(Connection dbConn, int permitId, String carLicenseNumber) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM PermitForVehicle WHERE permit_id = ? AND vehicle_num = ?";
        try (PreparedStatement preparedStatement = dbConn.prepareStatement(sql)) {
            preparedStatement.setInt(1, permitId);
            preparedStatement.setString(2, carLicenseNumber);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                int count = 0;
                while (rs.next()) {
                    count = rs.getInt("count");
                }
                return count > 0;
            }
        }
    }

    private void close(Statement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Throwable whatever) {
            }
        }
    }
}