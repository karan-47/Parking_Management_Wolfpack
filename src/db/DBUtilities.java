package db;

import java.sql.ResultSet;
import java.sql.Statement;

public class DBUtilities {
	
    public static void printResult(ResultSet result) throws Exception {
        int columnCount = result.getMetaData().getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            System.out.printf("%-30s", result.getMetaData().getColumnName(i));
        }
        System.out.println();

        while (result.next()) {
            for (int i = 1; i <= columnCount; i++) {
                System.out.printf("%-30s", result.getString(i));
            }
            System.out.println();
        }
    }
    
	public static void close(Statement st) {
		if(st != null) {
			try { st.close(); st = null; } catch(Throwable whatever) {}
		}
	}

	public static void close(ResultSet rs) {
		if(rs != null) {
			try { rs.close(); rs = null; } catch(Throwable whatever) {}
		}
	}
    
}