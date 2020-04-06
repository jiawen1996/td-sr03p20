package JDBC;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConfigConnection {

	public static Connection getConnection(String nomFichierProp) throws IOException, ClassNotFoundException, SQLException {
		Properties props = new Properties();
		URL urlFichierPropUrl = ConfigConnection.class.getResource(nomFichierProp);
		BufferedInputStream bis = null;
		
		try {
			bis = new BufferedInputStream(urlFichierPropUrl.openStream());
			props.load(bis);
			
			String driver = props.getProperty("driver");
			String url = props.getProperty("url");
			String user = props.getProperty("user");
			String pwd = props.getProperty("pwd");
			
			Class.forName(driver);
			
			return DriverManager.getConnection(url, user, pwd);
			
		} finally {
			if (bis != null) {
				bis.close();
			}
		}
	}
}
