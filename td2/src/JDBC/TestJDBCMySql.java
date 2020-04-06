package JDBC;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TestJDBCMySql {

	Connection db = null;
	Statement sql = null;
	DatabaseMetaData dmbd;
	
	public TestJDBCMySql(String argv[]) throws ClassNotFoundException, SQLException, IOException {
		String database = argv[0];
		String username = argv[1];
		String password = argv[2];
		Class.forName("com.mysql.jdbc.Driver");
		db = DriverManager.getConnection("jdbc:mysql:"+database,username,password);
	}
}
