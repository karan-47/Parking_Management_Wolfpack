package db_models;

import db.DBUtilities;

import java.sql.*;

public class Citation {
	private boolean isValidParameter(String parameter) {
        return parameter != null && !parameter.trim().isEmpty();
    }
	
	public int driverRequestCitationAppeal(Connection dbConn, int citationId, String appealComment) throws Exception {
        int rowsAffected = 0;
        PreparedStatement preparedStatement = null;
        try {
            if (citationId <= 0 || !isValidParameter(appealComment)) {
                System.out.println("Incorrect CitationId or AppealComment parameter value");
                return rowsAffected;
            }

            String sql = "UPDATE Citation SET citation_appeal = 1, citation_appeal_comment = ? WHERE citation_id = ?";
            preparedStatement = dbConn.prepareStatement(sql);
            preparedStatement.setString(1, appealComment);
            preparedStatement.setInt(2, citationId);
            rowsAffected = preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            DBUtilities.close(preparedStatement);
        }
        return rowsAffected;
    }

    public int adminProcessCitationAppeal(Connection dbConn, int citationId, boolean accept) throws Exception {
        int rowsAffected = 0;
        String sql;
        PreparedStatement preparedStatement = null;
        try {
            if (citationId <= 0) {
                System.out.println("Incorrect CitationId parameter value");
                return rowsAffected;
            }

            if (accept) {
                sql = "UPDATE Citation SET citation_valid = 0 WHERE citation_id = ?";
            }
            else {
                sql = "UPDATE Citation SET citation_appeal = 0, citation_appeal_comment = NULL WHERE citation_id = ?";
            }

            preparedStatement = dbConn.prepareStatement(sql);
            preparedStatement.setInt(1, citationId);
            rowsAffected = preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            DBUtilities.close(preparedStatement);
        }
        return rowsAffected;
    }

    public int driverMakePayment(Connection dbConn, int citationId) throws Exception {
        int rowsAffected = 0;
        PreparedStatement preparedStatement = null;
        try {
            if (citationId <= 0) {
                System.out.println("Incorrect CitationId parameter value");
                return rowsAffected;
            }

            String sql = "UPDATE Citation SET citation_paid = 1 WHERE citation_id = ?";
            preparedStatement = dbConn.prepareStatement(sql);
            preparedStatement.setInt(1, citationId);

            rowsAffected = preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw e;
        }  finally {
            DBUtilities.close(preparedStatement);
        }
        return rowsAffected;
    }
	
	public boolean generateCitation(Connection dbConn, String vehicleNum, String lotName, String spaceType) throws Exception {
        boolean processedCitation = false;
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;
        PreparedStatement preparedStatement3 = null;
        ResultSet rs = null;

        try {
            if (!isValidParameter(vehicleNum) || !isValidParameter(lotName)) {
                System.out.println("Incorrect parameters for generating citation");
                return processedCitation;
            }

            Permit p = new Permit();
            String violationCategory = p.checkParkingViolation(dbConn, vehicleNum, lotName, spaceType);
            if(violationCategory.equals("no violation")) {
                processedCitation = true;
                System.out.println("Vehicle has no parking violation");
                return processedCitation;
            }

            // Calculation of citationAmount
            boolean isHandicap = false;
            String driverSql = "SELECT is_handicap FROM Driver WHERE driver_id = " +
                    "(SELECT driver_id FROM Vehicle WHERE vehicle_num = ?)";
            preparedStatement1 = dbConn.prepareStatement(driverSql);
            preparedStatement1.setString(1, vehicleNum);
            rs = preparedStatement1.executeQuery();
            while (rs.next()) {
                isHandicap = rs.getBoolean("is_handicap");
            }

            DBUtilities.close(rs);

            // Retrieve violation category cost
            float violationCost = 40; // defaults to no permit
            String violationCostSql = "SELECT violation_fee FROM Violation WHERE violation_category = ?";
            preparedStatement2 = dbConn.prepareStatement(violationCostSql);
            preparedStatement2.setString(1, violationCategory);
            rs = preparedStatement2.executeQuery();
            while (rs.next()) {
                violationCost = rs.getFloat("violation_fee");
            }

            if (isHandicap) {
               violationCost = violationCost / 2;
            }

            String sql = "INSERT INTO Citation (vehicle_num, violation_category, lot_name, citation_time, citation_amount) VALUES (?, ?, ?, NOW(), ?)";
            preparedStatement3 = dbConn.prepareStatement(sql);
            preparedStatement3.setString(1, vehicleNum);
            preparedStatement3.setString(2, violationCategory);
            preparedStatement3.setString(3, lotName);
            preparedStatement3.setFloat(4, violationCost);
            int rowsAffected = preparedStatement3.executeUpdate();
            processedCitation = true;
            System.out.println("Generated citation for vehicle successfully.");
        } catch (Exception e) {
            throw e;
        } finally {
            DBUtilities.close(preparedStatement1);
            DBUtilities.close(preparedStatement2);
            DBUtilities.close(preparedStatement3);
            DBUtilities.close(rs);
        }
        return processedCitation;
    }
	
	public void getCitationInfo(Connection dbConn, int citationId) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            if (citationId <= 0) {
                System.out.println("Incorrect CitationId parameter value");
                return;
            }

            String sql = "SELECT * FROM Citation WHERE citation_id = ?";
            preparedStatement = dbConn.prepareStatement(sql);
            preparedStatement.setInt(1, citationId);
            rs = preparedStatement.executeQuery();
            DBUtilities.printResult(rs);
        } catch (Exception e) {
            throw e;
        } finally {
            DBUtilities.close(preparedStatement);
            DBUtilities.close(rs);
        }
    }

    public void getCitationInfo(Connection dbConn) {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM Citation";
            stmt = dbConn.createStatement();
            rs = stmt.executeQuery(sql);
            DBUtilities.printResult(rs);
        } catch (Exception e) {
            System.err.println("Caught exception in method getCitationInfo: " + e);
        } finally {
            DBUtilities.close(stmt);
            DBUtilities.close(rs);
        }
    }

    public void adminViewCitationAppeals(Connection dbConn) {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM Citation WHERE citation_valid = 1 AND citation_appeal = 1";
            stmt = dbConn.createStatement();
            rs = stmt.executeQuery(sql);
            DBUtilities.printResult(rs);
        } catch (Exception e) {
            System.err.println("Caught exception in method getCitationInfo: " + e);
        } finally {
            DBUtilities.close(stmt);
            DBUtilities.close(rs);
        }
    }

    public void getUnpaidValidCitationsForVehicle(Connection dbConn, String vehicleNum) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        if (!isValidParameter(vehicleNum)) {
            System.out.println("Incorrect vehicle num parameter value");
            return;
        }

        try {
            String sql = "SELECT citation_id, vehicle_num, lot_name, violation_category, " +
                    "citation_time, citation_amount, citation_appeal, citation_appeal_comment FROM Citation " +
                    "WHERE citation_valid = 1 AND citation_paid = 0 AND vehicle_num = ?";
            preparedStatement = dbConn.prepareStatement(sql);
            preparedStatement.setString(1, vehicleNum);
            rs = preparedStatement.executeQuery();
            DBUtilities.printResult(rs);
        } catch (Exception e) {
            throw e;
        } finally {
            DBUtilities.close(preparedStatement);
            DBUtilities.close(rs);
        }
    }

    public void getCitationsForVehicle(Connection dbConn, String vehicleNum) {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            if (!isValidParameter(vehicleNum)) {
                System.out.println("Incorrect vehicle num parameter value");
                return;
            }
            String sql = "SELECT * FROM Citation WHERE vehicle_num = ?";
            preparedStatement = dbConn.prepareStatement(sql);
            preparedStatement.setString(1, vehicleNum);
            rs = preparedStatement.executeQuery();
            DBUtilities.printResult(rs);
        } catch (Exception e) {
            System.err.println("Caught exception in method getCitationsForVehicle: " + e);
        } finally {
            DBUtilities.close(preparedStatement);
            DBUtilities.close(rs);
        }
    }
	
}