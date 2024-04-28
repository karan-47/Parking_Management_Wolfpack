import db.DBConnector;
import db_models.Driver;
import db_models.Permit;

import java.sql.Connection;

import org.junit.*;

import static org.junit.Assert.*;

public class TestPermit {
    private static Connection dbConn;
    private static Permit permit;
    private static Driver driver;

    private static final String driverEmpId = "111122221";
    private static final String driverStuId = "333344441";
    private static final String driverVisitorId = "9191234123";
    private static String driverEmpName = "Peter Parker";
    private static String driverStuName = "Hannah M";
    private static String driverVisitorName = "Gordon Ramsey";

    @BeforeClass
    public static void setUp() throws Exception {
        dbConn = DBConnector.getDbConnection();
        if(dbConn == null) {
            throw new Exception("Failed to connect to the database.");
        }

        driver = new Driver();
        driver.addDriverInfo(dbConn, driverEmpId, driverEmpName, "E", false);
        driver.addDriverInfo(dbConn, driverStuId, driverStuName, "S", false);
        driver.addDriverInfo(dbConn, driverVisitorId, driverVisitorName, "V", false);

        permit = new Permit();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        try{
            driver.deleteDriverInfo(dbConn, driverEmpId);
            driver.deleteDriverInfo(dbConn, driverStuId);
            driver.deleteDriverInfo(dbConn, driverVisitorId);
        } catch (Exception e) {
            System.err.println("Exception caught in method tearDown: " + e);
        } finally {
            DBConnector.close(dbConn);
        }
    }

    @Test
    public void testPermitInfo() {
        int status = 0;
        try {
            String permitType = "commuter";
            String zoneId = "A";
            String startTime = "2023-11-01 06:00";
            String expiryTime = "2024-11-01 06:00";

            int permitId = permit.addPermitInfo(dbConn, driverEmpId, permitType, zoneId, startTime, expiryTime);
            assertNotEquals(0, permitId);

            String newExpiryTime = "2024-12-01 23:59:59";
            status = permit.updatePermitInfo(dbConn, permitId, startTime, newExpiryTime);
            assertEquals(1, status);

            status = permit.deletePermitInfo(dbConn, permitId);
            assertEquals(1, status);
        } catch (Exception e) {
            fail("Exception occurred in testPermitInfo: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateZoneInPermit() {
        int status = 0;
        try {
            String permitType = "commuter";
            String zoneId = "A";
            String startTime = "2023-11-01 06:00";
            String expiryTime = "2024-11-01 06:00";

            int permitId = permit.addPermitInfo(dbConn, driverEmpId, permitType, zoneId, startTime, expiryTime);
            assertNotEquals(0, permitId);

            status = permit.updateZoneInPermit(dbConn, permitId, "C");
            assertEquals(1, status);

            status = permit.deletePermitInfo(dbConn, permitId);
            assertEquals(1, status);
        } catch (Exception e) {
            fail("Exception occurred in testUpdateZoneInPermit: " + e.getMessage());
        }
    }

    @Test
    public void testPermitCountForEmployee() {
        int status = 0;
        try {
            String zoneId = "A";
            String startTime = "2023-11-01 06:00";
            String expiryTime = "2024-11-01 06:00";

            // should succeed as employee can have 1 regular permit
            String permitAType = "commuter";
            int permitAId = permit.addPermitInfo(dbConn, driverEmpId, permitAType, zoneId, startTime, expiryTime);
            assertNotEquals(0, permitAId);

            // should succeed as employee can have 1 regular + 1 special permit
            String permitBType = "special event";
            int permitBId = permit.addPermitInfo(dbConn, driverEmpId, permitBType, zoneId, startTime, expiryTime);
            assertNotEquals(0, permitBId);
            // delete special permit
            status = permit.deletePermitInfo(dbConn, permitBId);
            assertEquals(1, status);

            // should succeed as employee can have 2 regular permits
            String permitCType = "residential";
            int permitCId = permit.addPermitInfo(dbConn, driverEmpId, permitCType, zoneId, startTime, expiryTime);
            assertNotEquals(0, permitCId);

            // should succeed as employee can have 2 regular + 1 special permit
            String permitDType = "park and ride";
            int permitDId = permit.addPermitInfo(dbConn, driverEmpId, permitDType, zoneId, startTime, expiryTime);
            assertNotEquals(0, permitDId);

            // should fail as employees cannot have more than 2 regular permits
            String permitEType = "peak hours";
            int permitEId = permit.addPermitInfo(dbConn, driverEmpId, permitEType, zoneId, startTime, expiryTime);
            assertEquals(0, permitEId);

            // should fail as employees cannot have more than 1 special permit
            String permitFType = "special event";
            int permitFId = permit.addPermitInfo(dbConn, driverEmpId, permitFType, zoneId, startTime, expiryTime);
            assertEquals(0, permitFId);

            // delete regular permits A & C, special permit D
            status = permit.deletePermitInfo(dbConn, permitAId);
            assertEquals(1, status);
            status = permit.deletePermitInfo(dbConn, permitCId);
            assertEquals(1, status);
            status = permit.deletePermitInfo(dbConn, permitDId);
            assertEquals(1, status);
        } catch (Exception e) {
            fail("Exception occurred in testPermitCountForEmployee: " + e.getMessage());
        }
    }

    @Test
    public void testPermitCountForStudent() {
        int status = 0;
        try {
            String zoneId = "AS";
            String startTime = "2023-11-01 06:00";
            String expiryTime = "2024-11-01 06:00";

            // should succeed as student can have 1 regular permit
            String permitAType = "commuter";
            int permitAId = permit.addPermitInfo(dbConn, driverStuId, permitAType, zoneId, startTime, expiryTime);
            assertNotEquals(0, permitAId);

            // should fail as students cannot 2 regular permits
            String permitBType = "residential";
            int permitBId = permit.addPermitInfo(dbConn, driverStuId, permitBType, zoneId, startTime, expiryTime);
            assertEquals(0, permitBId);

            // should succeed as student can have 1 regular + 1 special permit
            String permitCType = "special event";
            int permitCId = permit.addPermitInfo(dbConn, driverStuId, permitCType, zoneId, startTime, expiryTime);
            assertNotEquals(0, permitCId);

            // should fail as student cannot have 2 special permits
            String permitDType = "park and ride";
            int permitDId = permit.addPermitInfo(dbConn, driverStuId, permitDType, zoneId, startTime, expiryTime);
            assertEquals(0, permitDId);

            // should fail as students cannot 2 regular permits
            String permitEType = "peak hours";
            int permitEId = permit.addPermitInfo(dbConn, driverStuId, permitEType, zoneId, startTime, expiryTime);
            assertEquals(0, permitEId);

            // delete permits A & C
            status = permit.deletePermitInfo(dbConn, permitAId);
            assertEquals(1, status);
            status = permit.deletePermitInfo(dbConn, permitCId);
            assertEquals(1, status);
        } catch (Exception e) {
            fail("Exception occurred in testPermitCountForStudent: " + e.getMessage());
        }
    }

    @Test
    public void testPermitCountForVisitor() {
        int status = 0;
        try {
            String zoneId = "V";
            String startTime = "2023-11-01 06:00";
            String expiryTime = "2024-11-01 06:00";

            // should succeed as visitor can have 1 regular permit
            String permitAType = "commuter";
            int permitAId = permit.addPermitInfo(dbConn, driverVisitorId, permitAType, zoneId, startTime, expiryTime);
            assertNotEquals(0, permitAId);

            // should fail as visitor cannot 2 regular permits
            String permitBType = "residential";
            int permitBId = permit.addPermitInfo(dbConn, driverVisitorId, permitBType, zoneId, startTime, expiryTime);
            assertEquals(0, permitBId);

            // should fail as visitors cannot have special permits
            String permitCType = "special event";
            int permitCId = permit.addPermitInfo(dbConn, driverVisitorId, permitCType, zoneId, startTime, expiryTime);
            assertEquals(0, permitBId);

            // should fail as visitors cannot have special permits
            String permitDType = "park and ride";
            int permitDId = permit.addPermitInfo(dbConn, driverVisitorId, permitDType, zoneId, startTime, expiryTime);
            assertEquals(0, permitDId);

            // delete regular permit A
            status = permit.deletePermitInfo(dbConn, permitAId);
            assertEquals(1, status);
        } catch (Exception e) {
            fail("Exception occurred in testPermitCountForVisitor: " + e.getMessage());
        }
    }

    @Test
    public void testSpecialPermits() {
        int status = 0;
        try {
            String startTime = "2023-11-01 06:00";
            String expiryTime = "2024-11-01 06:00";
            String permitType = "special event";

            int permitAId = permit.addPermitInfo(dbConn, driverEmpId, permitType, "B", startTime, expiryTime);
            assertNotEquals(0, permitAId);

            int permitBId = permit.addPermitInfo(dbConn, driverStuId, permitType, "BS", startTime, expiryTime);
            assertNotEquals(0, permitBId);

            int permitCId = permit.addPermitInfo(dbConn, driverVisitorId, permitType, "V", startTime, expiryTime);
            assertEquals(0, permitCId);

            // delete special permits
            status = permit.deletePermitInfo(dbConn, permitAId);
            assertEquals(1, status);
            status = permit.deletePermitInfo(dbConn, permitBId);
            assertEquals(1, status);
        } catch (Exception e) {
            fail("Exception occurred in testPermitCountForVisitor: " + e.getMessage());
        }
    }

    @Test
    public void testCanAddVehicleToPermit() {
        int status = 0;
        boolean canAdd = false;
        try {
            String startTime = "2023-11-01 06:00";
            String expiryTime = "2024-11-01 06:00";
            String permitType = "commuter";

            int permitAId = permit.addPermitInfo(dbConn, driverEmpId, permitType, "B", startTime, expiryTime);
            assertNotEquals(0, permitAId);

            int permitBId = permit.addPermitInfo(dbConn, driverStuId, permitType, "BS", startTime, expiryTime);
            assertNotEquals(0, permitBId);

            int permitCId = permit.addPermitInfo(dbConn, driverVisitorId, permitType, "V", startTime, expiryTime);
            assertNotEquals(0, permitCId);

            // employee, student and visitor can have 1 vehicle on permit
            canAdd = permit.canAddVehicleToPermit(dbConn, permitAId);
            assertTrue(canAdd);
            canAdd = permit.canAddVehicleToPermit(dbConn, permitBId);
            assertTrue(canAdd);
            canAdd = permit.canAddVehicleToPermit(dbConn, permitCId);
            assertTrue(canAdd);

            // Only employee can have 2 vehicles on permit
            /*
            canAdd = permit.canAddVehicleToPermit(dbConn, permitAId);
            assertTrue(canAdd);
            canAdd = permit.canAddVehicleToPermit(dbConn, permitBId);
            assertFalse(canAdd);
            canAdd = permit.canAddVehicleToPermit(dbConn, permitCId);
            assertFalse(canAdd);
            */

            // delete permits
            status = permit.deletePermitInfo(dbConn, permitAId);
            assertEquals(1, status);
            status = permit.deletePermitInfo(dbConn, permitBId);
            assertEquals(1, status);
            status = permit.deletePermitInfo(dbConn, permitCId);
            assertEquals(1, status);
        } catch (Exception e) {
            fail("Exception occurred in testPermitCountForVisitor: " + e.getMessage());
        }
    }

    @Test
    public void testCheckParkingViolation() {
        String violation;
        try {
            violation = permit.checkParkingViolation(dbConn, "V001", "Lot A", "electric");
            assertEquals("no violation", violation);
            violation = permit.checkParkingViolation(dbConn, "V001", "Lot A", "regular");
            assertEquals("invalid permit", violation);
            violation = permit.checkParkingViolation(dbConn, "V001", "Lot B", "electric");
            assertEquals("invalid permit", violation);
            violation = permit.checkParkingViolation(dbConn, "V002", "Lot A", "regular");
            assertEquals("no violation", violation);
            violation = permit.checkParkingViolation(dbConn, "V003", "Lot C", "regular");
            assertEquals("expired permit", violation);
            violation = permit.checkParkingViolation(dbConn, "V005", "Lot A", "compact car");
            assertEquals("no permit", violation);
            violation = permit.checkParkingViolation(dbConn, "V998", "Lot A", "regular");
            assertEquals("no permit", violation);
        } catch (Exception e) {
            fail("Exception occurred in testPermitCountForVisitor: " + e.getMessage());
        }
    }
}