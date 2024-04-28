package db_models;

import db.DBUtilities;

import java.sql.*;

public class Vehicle {

    private boolean isValidParameter(String parameter) {
        return parameter!=null && !parameter.trim().isEmpty();
    }

    public boolean isValidYear(int year) {
        return year >= 1900 && year <= 2100;
    }

    public int addVehicleInfo(Connection dbConn, String car_license_number, String driver_id, String model, String color, String manufacturer, int year) throws Exception {
        int rowsAffected = 0;

        try {
            // Check if the vehicle already exists in the table
            if (isVehicleExists(dbConn, car_license_number)) {
                System.out.println("Vehicle already exists in the table.");
                return rowsAffected;
            }

            if (!isValidParameter(car_license_number)) {
                System.out.println("Incorrect car_license_number parameter value");
                return rowsAffected;
            }
            if (!isValidParameter(driver_id)) {
                System.out.println("Incorrect driver_id parameter value");
                return rowsAffected;
            }
            if (!isValidParameter(model)) {
                System.out.println("Incorrect model parameter value");
                return rowsAffected;
            }
            if (!isValidParameter(color)) {
                System.out.println("Incorrect color parameter value");
                return rowsAffected;
            }
            if (!isValidParameter(manufacturer)) {
                System.out.println("Incorrect manufacturer parameter value");
                return rowsAffected;
            }
            if (!isValidYear(year)) {
                System.out.println("Incorrect year parameter value");
                return rowsAffected;
            }

            // Update the SQL statement to include the correct table and columns
            String sql = "INSERT INTO Vehicle(vehicle_num, driver_id, vehicle_model, vehicle_color, vehicle_manuf, vehicle_year) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = dbConn.prepareStatement(sql);
            preparedStatement.setString(1, car_license_number);
            preparedStatement.setString(2, driver_id);
            preparedStatement.setString(3, model);
            preparedStatement.setString(4, color);
            preparedStatement.setString(5, manufacturer);
            preparedStatement.setInt(6, year);

            rowsAffected = preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw e;
        }
        return rowsAffected;
    }

    private boolean isVehicleExists(Connection dbConn, String car_license_number) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM Vehicle WHERE vehicle_num = ?";
        PreparedStatement preparedStatement = dbConn.prepareStatement(sql);
        preparedStatement.setString(1, car_license_number);
        ResultSet rs = preparedStatement.executeQuery();

        int count = 0;
        while (rs.next()) {
            count = rs.getInt("count");
        }

        return count > 0;
    }

    public int updateVehicleInfo(Connection dbConn, String car_license_number, String newColor) throws Exception {
        int rowsAffected = 0;
        try {
            if (!isValidParameter(car_license_number)) {
                System.out.println("Incorrect car_license_number parameter value");
                return rowsAffected;
            }

            // Check if the vehicle already exists in the table
            if (!isVehicleExists(dbConn, car_license_number)) {
                System.out.println("Vehicle does not exist in the table.");
                return rowsAffected;
            }

            if (!isValidParameter(newColor)) {
                System.out.println("Incorrect newColor parameter value");
                return rowsAffected;
            }

            // Update the SQL statement to include the correct table and column names
            String sql = "UPDATE Vehicle SET vehicle_color = ? WHERE vehicle_num = ?";
            PreparedStatement preparedStatement = dbConn.prepareStatement(sql);
            preparedStatement.setString(1, newColor);
            preparedStatement.setString(2, car_license_number);

            rowsAffected = preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw e;
        }
        return rowsAffected;
    }


    public int deleteVehicleInfo(Connection dbConn, String car_license_number) throws Exception {
        int rowsAffected = 0;
        try {
            if (!isValidParameter(car_license_number)) {
                System.out.println("Incorrect car_license_number parameter value");
                return rowsAffected;
            }

            // Check if the vehicle exists in the table
            if (!isVehicleExists(dbConn, car_license_number)) {
                System.out.println("Vehicle does not exist in the table.");
                return rowsAffected;
            }

            // Update the SQL statement to include the correct table and column names
            String sql = "DELETE FROM Vehicle WHERE vehicle_num = ?";
            PreparedStatement preparedStatement = dbConn.prepareStatement(sql);
            preparedStatement.setString(1, car_license_number);

            rowsAffected = preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw e;
        }
        return rowsAffected;
    }


    public void printAllVehicleInfo(Connection dbConn) {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM Vehicle";
            stmt = dbConn.createStatement();
            rs = stmt.executeQuery(sql);
            DBUtilities.printResult(rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(stmt);
            close(rs);
        }
    }

    public void getVehicleInfo(Connection dbConn, String car_license_number) {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            if (!isValidParameter(car_license_number)) {
                System.out.println("Incorrect car_license_number parameter value");
                return;
            }

            // Check if the vehicle exists in the table
            if (!isVehicleExists(dbConn, car_license_number)) {
                System.out.println("Vehicle does not exist in the table.");
                return;
            }

            // Update the SQL statement to include the correct table and column names
            String sql = "SELECT * FROM Vehicle WHERE vehicle_num = ?";
            preparedStatement = dbConn.prepareStatement(sql);
            preparedStatement.setString(1, car_license_number);
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
        if(rs != null) {
            try { rs.close(); } catch(Throwable whatever) {}
        }
    }



}
