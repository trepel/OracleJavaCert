package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Savepoint;
import java.sql.Statement;

public class DatabaseChecker {
	
	public static void main(String[] args) throws Exception {
		Connection conn = getConnection();
	    conn.setAutoCommit(false);
	    Statement st = conn.createStatement();

	    st.executeUpdate("create table survey (id int,myURL CHAR);");
	    st.executeUpdate("insert into survey(id) values(01)");
	    st.executeUpdate("insert into survey(id) values(02)");

	    Savepoint mySavepoint = conn.setSavepoint("MYSAVEPOINT");
	    
//	    conn.rollback(); // only id:3 remains

	    st.executeUpdate("insert into survey(id) values(03)");
	    
//	    conn.rollback(); // nothing remains
	    conn.rollback(mySavepoint); // the rollback is just to mySavepoing, thus the id:1 and id:2 remains, the id:3 is rolled back
	    
	    
	    st.executeUpdate("insert into survey(id) values(04)");

	    Savepoint mySavepoint2 = conn.setSavepoint("MYSAVEPOINT2");
	    
	    st.executeUpdate("insert into survey(id) values(05)");
	    
	    conn.rollback(mySavepoint2); // the id:5 is rolled back
	    
	    conn.commit();
	    
//	    conn.rollback (mySavepoint); // after commit all the savepoints are released, SQLException is thrown
	    
	    ResultSet rs = st.executeQuery("Select id from survey");
	    System.out.println("Results:");
	    while(rs.next()) {
	    	System.out.println("id:" + rs.getInt(1)); // the counting starts from 1, not zero!!!!!
	    }
	    
	    st.close();
	    conn.close();
	}
	
	private static Connection getConnection() throws Exception {
//	    Class.forName("org.hsqldb.jdbcDriver"); // 1. way how to register driver
		DriverManager.registerDriver(new org.hsqldb.jdbcDriver()); // 2. way how to register driver
	    String url = "jdbc:hsqldb:mem:data/tutorial";

	    return DriverManager.getConnection(url, "sa", "");
	  }

}
