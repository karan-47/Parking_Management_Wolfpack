import db.DBConnector;
import db_models.ParkingLot;

import java.sql.Connection;

import org.junit.*;

import static org.junit.Assert.*;

public class TestParkingLot {
    private static Connection dbConn;
    private static ParkingLot lot;

    @BeforeClass
    public static void setUp() throws Exception {
        dbConn = DBConnector.getDbConnection();
        if(dbConn == null) {
            throw new Exception("Failed to connect to the database.");
        }
        lot = new ParkingLot();
    }

    @AfterClass
    public static void tearDown() {
        DBConnector.close(dbConn);
    }

    @Test
    public void testParkingLotInfo() {
        int status = 0;
        try {
            String lotName = "Varsity Drive";
            String lotAddress = "Marcom St Raleigh NC 27606";
            status = lot.addParkingLotInfo(dbConn, lotName, lotAddress);
            assertEquals(1, status);

            String newLotName = "Varsity Drive Park";
            String newLotAddress = "1433 Marcom St Raleigh NC 27606";
            status = lot.updateParkingLot(dbConn, lotName, newLotName, newLotAddress);
            assertEquals(1, status);

            status = lot.deleteParkingLotInfo(dbConn, newLotName);
            assertEquals(1, status);
        } catch (Exception e) {
            fail("Exception occurred in testParkingLotInfo: " + e.getMessage());
        }
    }
}