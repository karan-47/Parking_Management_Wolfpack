import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class UnitTestRunner {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(TestParkingLot.class, TestPermit.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println("Unit tests passed: " + result.wasSuccessful());
    }
}