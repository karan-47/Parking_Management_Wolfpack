package db_models;

import db.DBUtilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Space {
	private boolean isValidParameter(String parameter) {
        return parameter!=null && !parameter.trim().isEmpty();
    }
	public int addSpaceToLot(Connection dbConn, String lotName, int spaceNum, String spaceType) throws Exception {
        int rowsAffected = 0;
        PreparedStatement preparedStatement = null;
        try {
            if (!isValidParameter(lotName)) {
                System.out.println("Incorrect LotName parameter value");
                return rowsAffected;
            }
            if (spaceNum <= 0) {
                System.out.println("Incorrect SpaceNum parameter value");
                return rowsAffected;
            }
            if (!isValidParameter(spaceType)) {
                System.out.println("Incorrect SpaceType parameter value");
                return rowsAffected;
            }

            String sql = "INSERT INTO Space (lot_name, space_num, space_type) VALUES (?, ?, ?)";
            preparedStatement = dbConn.prepareStatement(sql);
            preparedStatement.setString(1, lotName);
            preparedStatement.setInt(2, spaceNum);
            preparedStatement.setString(3, spaceType);
            rowsAffected = preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            DBUtilities.close(preparedStatement);
        }
        return rowsAffected;
    }
	
	public int updateSpaceInfo(Connection dbConn, String lotName, int spaceNum, String newSpaceType) throws Exception {
        int rowsAffected = 0;
        PreparedStatement preparedStatement = null;
        try {
            if (!isValidParameter(lotName)) {
                System.out.println("Incorrect LotName parameter value");
                return rowsAffected;
            }
            if (spaceNum <= 0) {
                System.out.println("Incorrect SpaceNum parameter value");
                return rowsAffected;
            }
            if (!isValidParameter(newSpaceType)) {
                System.out.println("Incorrect NewSpaceType parameter value");
                return rowsAffected;
            }

            String sql = "UPDATE Space SET space_type = ? WHERE lot_name = ? AND space_num = ?";
            preparedStatement = dbConn.prepareStatement(sql);
            preparedStatement.setString(1, newSpaceType);
            preparedStatement.setString(2, lotName);
            preparedStatement.setInt(3, spaceNum);
            rowsAffected = preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw e;
        }  finally {
            DBUtilities.close(preparedStatement);
        }
        return rowsAffected;
    }
	public int deleteSpaceInfo(Connection dbConn, String lotName, int spaceNum) throws Exception {
        int rowsAffected = 0;
        PreparedStatement preparedStatement = null;
        try {
            if (!isValidParameter(lotName)) {
                System.out.println("Incorrect LotName parameter value");
                return rowsAffected;
            }
            if (spaceNum <= 0) {
                System.out.println("Incorrect SpaceNum parameter value");
                return rowsAffected;
            }

            String sql = "DELETE FROM Space WHERE lot_name = ? AND space_num = ?";
            preparedStatement = dbConn.prepareStatement(sql);
            preparedStatement.setString(1, lotName);
            preparedStatement.setInt(2, spaceNum);
            rowsAffected = preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw e;
        }  finally {
            DBUtilities.close(preparedStatement);
        }
        return rowsAffected;
    }
	public int updateSpaceType(Connection dbConn, String lotName, int spaceNum, String newSpaceType) throws Exception {
        int rowsAffected = 0;
        PreparedStatement preparedStatement = null;
        try {
            if (!isValidParameter(lotName)) {
                System.out.println("Incorrect LotName parameter value");
                return rowsAffected;
            }
            if (spaceNum <= 0) {
                System.out.println("Incorrect SpaceNum parameter value");
                return rowsAffected;
            }
            if (!isValidParameter(newSpaceType)) {
                System.out.println("Incorrect NewSpaceType parameter value");
                return rowsAffected;
            }

            String sql = "UPDATE Space SET space_type = ? WHERE lot_name = ? AND space_num = ?";
            preparedStatement = dbConn.prepareStatement(sql);
            preparedStatement.setString(1, newSpaceType);
            preparedStatement.setString(2, lotName);
            preparedStatement.setInt(3, spaceNum);
            rowsAffected = preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw e;
        }  finally {
            DBUtilities.close(preparedStatement);
        }
        return rowsAffected;
    }

    public void getSpaceInfo(Connection dbConn, String lotName) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            if (!isValidParameter(lotName)) {
                System.out.println("Incorrect LotName parameter value");
                return;
            }

            String sql = "SELECT * FROM Space WHERE lot_name = ?";
            preparedStatement = dbConn.prepareStatement(sql);
            preparedStatement.setString(1, lotName);
            rs = preparedStatement.executeQuery();
            DBUtilities.printResult(rs);
        } catch (Exception e) {
            throw e;
        } finally {
            DBUtilities.close(preparedStatement);
            DBUtilities.close(rs);
        }
    }

	public void getSpaceInfo(Connection dbConn, String lotName, int spaceNum) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            if (!isValidParameter(lotName) || spaceNum <= 0) {
                System.out.println("Incorrect LotName or SpaceNum parameter value");
                return;
            }

            String sql = "SELECT * FROM Space WHERE lot_name = ? AND space_num = ?";
            preparedStatement = dbConn.prepareStatement(sql);
            preparedStatement.setString(1, lotName);
            preparedStatement.setInt(2, spaceNum);
            rs = preparedStatement.executeQuery();
            DBUtilities.printResult(rs);

        } catch (Exception e) {
            throw e;
        } finally {
            DBUtilities.close(preparedStatement);
            DBUtilities.close(rs);
        }
    }
	
	public int updateAvailabilitySpaceStatus(Connection dbConn, String lotName, int spaceNum, int availStatus) throws Exception {
        return updateAvailabilitySpaceStatus(dbConn, lotName, spaceNum, availStatus, null);
    }

    public int updateAvailabilitySpaceStatus(Connection dbConn, String lotName, int spaceNum, int availStatus, String vehicleNum) throws Exception {
        int rowsAffected = 0;
        PreparedStatement preparedStatement = null;
        try {
            if (!isValidParameter(lotName) || spaceNum <= 0 || (availStatus != 0 && availStatus != 1)) {
                System.out.println("Incorrect LotName, SpaceNum, or AvailStatus parameter value");
                return rowsAffected;
            }

            String sql;

            if (isValidParameter(vehicleNum)) {
                // If vehicleNum is provided, set avail_status to 0
                sql = "UPDATE Space SET avail_status = 0, vehicle_num = ? WHERE lot_name = ? AND space_num = ?";
                preparedStatement = dbConn.prepareStatement(sql);
                preparedStatement.setString(1, vehicleNum);
                preparedStatement.setString(2, lotName);
                preparedStatement.setInt(3, spaceNum);
            } else {
                // If vehicleNum is not provided, set avail_status to the specified value
                sql = "UPDATE Space SET avail_status = ?, vehicle_num = NULL WHERE lot_name = ? AND space_num = ?";
                preparedStatement = dbConn.prepareStatement(sql);
                preparedStatement.setInt(1, availStatus);
                preparedStatement.setString(2, lotName);
                preparedStatement.setInt(3, spaceNum);
            }

            rowsAffected = preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            DBUtilities.close(preparedStatement);
        }
        return rowsAffected;
    }

	
	public void printAllSpaceInfo(Connection dbConn) throws Exception {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM Space";
            stmt = dbConn.createStatement();
            rs = stmt.executeQuery(sql);
            DBUtilities.printResult(rs);
        } catch (Exception e) {
            throw e;
        } finally {
            DBUtilities.close(stmt);
            DBUtilities.close(rs);
        }
    }


}