package db_models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import db.DBUtilities;
import java.sql.Connection;

public class Reports {

	LotContainsZone lotContainsZone;
	
	public Reports(LotContainsZone lotContainsZone) {
		this.lotContainsZone = lotContainsZone;
	}
	
	private boolean isValidParameter(String parameter) {
	    return parameter != null && !parameter.trim().isEmpty();
	}
	
	public void getZonesInLot(Connection dbConn) {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM LotContainsZone";
			stmt = dbConn.createStatement();
			rs = stmt.executeQuery(sql);
			DBUtilities.printResult(rs);
		} catch (Exception e) {
			System.err.println("Caught exception in method getZonesInLot: " + e);
		} finally {
			DBUtilities.close(stmt);
			DBUtilities.close(rs);
		}
	}

	public int getEmployeesWithPermitInZone(Connection dbConn, String ZoneId) {
		int numEmployees = 0;
		PreparedStatement preparedStatement = null;
		try {
			String sql = "SELECT COUNT(DISTINCT(Driver.driver_id)) AS Num_Employees FROM Driver" +
					" JOIN Permit ON Driver.driver_id = Permit.driver_id" +
					" WHERE Driver.driver_status = 'E' AND Permit.zone_id = ?";

			preparedStatement = dbConn.prepareStatement(sql);
			preparedStatement.setString(1, ZoneId);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()) {
				numEmployees = rs.getInt("Num_Employees");
			}
		} catch (Exception e) {
			System.err.println("Caught exception in method getPermitInfo: " + e);
		} finally {
			DBUtilities.close(preparedStatement);
		}
		return numEmployees;
	}

	public void getCitationForDuration(Connection dbConn, String lotName, String startTime, String endTime) throws Exception {
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			if (!isValidParameter(lotName) || !isValidParameter(startTime) || !isValidParameter(endTime)) {
				System.out.println("Incorrect LotName, StartTime, or EndTime parameter value");
				return;
			}

			String sql = "SELECT * FROM Citation WHERE lot_name = ? AND citation_time >= ? AND citation_time <= ?";
			preparedStatement = dbConn.prepareStatement(sql);
			preparedStatement.setString(1, lotName);
			preparedStatement.setString(2, startTime);
			preparedStatement.setString(3, endTime);

			rs = preparedStatement.executeQuery();
			DBUtilities.printResult(rs);
		} catch (Exception e) {
			throw e;
		} finally {
			DBUtilities.close(preparedStatement);
			DBUtilities.close(rs);
		}
	}

	public int getVehiclesInViolation(Connection dbConn) throws Exception {
		int carsInViolation = 0;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String sql = "SELECT COUNT(DISTINCT(vehicle_num)) FROM Citation WHERE citation_valid = 1 AND citation_paid = 0";
			preparedStatement = dbConn.prepareStatement(sql);

			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				carsInViolation = resultSet.getInt(1);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			DBUtilities.close(preparedStatement);
			DBUtilities.close(resultSet);
		}
		return carsInViolation;
	}

	public int getSpaceNumberForType(Connection dbConn, String lotName, String spaceType) throws Exception {
		int spaceNum = -1;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			if (!isValidParameter(lotName) || !isValidParameter(spaceType)) {
				System.out.println("Incorrect LotName or SpaceType parameter value");
				return spaceNum;
			}

			String sql = "SELECT space_num FROM Space WHERE lot_name = ? AND space_type = ? AND avail_status = 1 LIMIT 1";
			preparedStatement = dbConn.prepareStatement(sql);
			preparedStatement.setString(1, lotName);
			preparedStatement.setString(2, spaceType);

			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				spaceNum = rs.getInt("space_num");
			}
		} catch (Exception e) {
			throw e;
		}  finally {
			DBUtilities.close(preparedStatement);
			DBUtilities.close(rs);
		}
		return spaceNum;
	}

	public void getPermitRecordsForDriver(Connection dbConn, String driverId) {
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			if (!isValidParameter(driverId)) {
				System.out.println("Incorrect driver id parameter value");
				return;
			}
			String sql = "SELECT * FROM Permit WHERE driver_id = ?";
			preparedStatement = dbConn.prepareStatement(sql);
			preparedStatement.setString(1, driverId);
			rs = preparedStatement.executeQuery();
			DBUtilities.printResult(rs);
		} catch (Exception e) {
			System.err.println("Caught exception in method getPermitRecordsForDriver: " + e);
		} finally {
			DBUtilities.close(preparedStatement);
			DBUtilities.close(rs);
		}
	}
	
}