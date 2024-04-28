package db_models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;

import db.DBUtilities;

public class Driver {
	
    private boolean isValidParameter(String parameter) {
        return parameter!=null && !parameter.trim().isEmpty();
    }
	
	public int addDriverInfo(Connection dbConn, String driverId, String driverName, String status, boolean isHandicapped) throws Exception {
        int rowsAffected = 0;
        PreparedStatement preparedStatement = null;
		try {
        	if (!isValidParameter(driverId)) {
        		System.out.println("Incorrect DriverID parameter value");
        		return rowsAffected;
        	}
        	if (!isValidParameter(driverName)) {
        		System.out.println("Incorrect DriverName parameter value");
        		return rowsAffected;
        	}
        	if (!isValidParameter(status)) {
        		System.out.println("Incorrect Status parameter value");	
        		return rowsAffected;
        	}
        	if (isDriverPresent(dbConn, driverId)) {
        		System.out.println("Driver -> " + driverId + " already present");
        		return rowsAffected;
        	}
            String sql = "INSERT INTO Driver(driver_id, driver_name, driver_status, is_handicap) VALUES (?, ?, ?, ?)";
        	preparedStatement = dbConn.prepareStatement(sql);
            preparedStatement.setString(1, driverId);
            preparedStatement.setString(2, driverName);
            preparedStatement.setString(3, status);
            preparedStatement.setBoolean(4, isHandicapped);
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException duplicateException) {
        	System.out.println("Driver with driver_id -> " + driverId + " already inserted in the database");
        } catch (Exception e) {
            throw e;
        } finally {
        	close(preparedStatement);
        }
		return rowsAffected;
    }

	public int updateDriverInfo(Connection dbConn, String driverId, String driverName, boolean isHandicapped) throws Exception {
	    int rowsAffected = 0;
	    PreparedStatement preparedStatement = null;
	    try {
	        if (!isValidParameter(driverId)) {
	            System.out.println("Incorrect DriverID parameter value");
        		return rowsAffected;
	        }
	        if (!isValidParameter(driverName)) {
	            System.out.println("Incorrect DriverName parameter value");
        		return rowsAffected;
	        }
//	        if (!isValidParameter(status)) {
//	            System.out.println("Incorrect Status parameter value");
//        		return rowsAffected;
//	        }
	        String sql = "UPDATE Driver SET driver_name = ?, is_handicap = ? WHERE driver_id = ?";
	        preparedStatement = dbConn.prepareStatement(sql);
            preparedStatement.setString(1, driverName);
//            preparedStatement.setString(2, status);
            preparedStatement.setBoolean(2, isHandicapped);
            preparedStatement.setString(3, driverId);
            rowsAffected = preparedStatement.executeUpdate();
	    } catch (Exception e) {
	        throw e;
	    } finally {
	    	close(preparedStatement);
	    }
	    return rowsAffected;
	}

	public int deleteDriverInfo(Connection dbConn, String driverId) throws Exception {
	    int rowsAffected = 0;
	    PreparedStatement preparedStatement = null;
	    try {
	        if (!isValidParameter(driverId)) {
	            System.out.println("Incorrect DriverID parameter value");
	            return rowsAffected;
	        }
	        String sql = "DELETE FROM Driver WHERE driver_id = ?";
	        preparedStatement = dbConn.prepareStatement(sql);
            preparedStatement.setString(1, driverId);
            rowsAffected = preparedStatement.executeUpdate();
	    } catch (Exception e) {
	        throw e;
	    } finally {
	    	close(preparedStatement);
	    }
	    return rowsAffected;
	}
	
	public int getDriverStatus(Connection dbConn, String driverId) {
		PreparedStatement stmt = null;
	    ResultSet rs = null;
	    int row = 0;
	    if (!isValidParameter(driverId)) {
    		System.out.println("No driver with driver_id -> " + driverId + " found.");
    		return row;
    	}
		try {
	        String sql = "SELECT driver_status FROM Driver WHERE driver_id = ?;";
	        stmt = dbConn.prepareStatement(sql);
	        stmt.setString(1, driverId);
	        rs = stmt.executeQuery();
	        if (rs.next()) {
	        	rs.beforeFirst();
		        DBUtilities.printResult(rs);
	        	return 1;
			} else {
        		return 0;
			}
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	    	DBUtilities.close(rs);
	    	DBUtilities.close(stmt);
	    }	
		return row;
	}
	
	public int getDriverInfo(Connection dbConn, String driverId) {
		PreparedStatement stmt = null;
	    ResultSet rs = null;
	    int row = 0;
	    if (!isValidParameter(driverId)) {
    		System.out.println("No driver with driver_id -> " + driverId + " found.");
    		return row;
    	}
		try {
	        String sql = "SELECT * FROM Driver WHERE driver_id = ?;";
	        stmt = dbConn.prepareStatement(sql);
	        stmt.setString(1, driverId);
	        rs = stmt.executeQuery();
	        if (rs.next()) {
	        	rs.beforeFirst();
		        DBUtilities.printResult(rs);
	        	return 1;
			} else {
        		return 0;
			}
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	    	DBUtilities.close(rs);
	    	DBUtilities.close(stmt);
	    }	
		return row;
	}
 	
	public boolean isDriverPresent(Connection dbConn, String driverId) {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
		try {
        	if (!isValidParameter(driverId)) {
        		System.out.println("Incorrect DriverID parameter value");
        		return false;
        	}
            String sql = "SELECT count(*) FROM Driver WHERE driver_id = ?";
        	preparedStatement = dbConn.prepareStatement(sql);
            preparedStatement.setString(1, driverId);
            rs = preparedStatement.executeQuery();
            int count = 0;
            if (rs.next()) {
            	count = rs.getInt(1);
            	return count > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();;
        } finally {
        	DBUtilities.close(preparedStatement);
        }
		return false;
	}
	
	public void printAllDriverInfo(Connection dbConn) throws Exception {
	    Statement stmt = null;
	    ResultSet rs = null;
		try {
	        String sql = "SELECT * FROM Driver";
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
	
//	private void printResult(ResultSet result) {
//		try {
//			System.out.println("******************************");
//			System.out.println("Driver_Id, Driver_Name, Driver_Status, Is_Handicapped");
//			while (result.next()) {
//				String id = result.getString("driver_id");
//				String driverName = result.getString("driver_name");
//				String driverStatus = result.getString("driver_status");
//				boolean isHandicap = result.getBoolean("is_handicap");
//				System.out.println(id + ", " + driverName + ", " + driverStatus + ", " + isHandicap);
//			}
//			System.out.println("******************************");
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			close(result);
//		}
//	}
	
	private void close(Statement st) {
		if(st != null) {
			try { st.close(); } catch(Throwable whatever) {}
		}
	}

	private void close(ResultSet rs) {
		if(rs != null) {
			try { rs.close(); } catch(Throwable whatever) {}
		}
	}
	
}
