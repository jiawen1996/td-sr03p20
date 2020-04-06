import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Properties;

public class ConfigConnection {
	 public static Connection getConnection(String nomFichierProp)
		      throws IOException, ClassNotFoundException, SQLException {
		    Properties props = new Properties();
		    URL urlFichierProp = ConfigConnection.class.getResource(nomFichierProp);
		    if (urlFichierProp == null) {
		      throw new IOException("Fichier " + nomFichierProp + " pas trouvé !");
		    }
		    BufferedInputStream bis = null;
		    try {
		      bis = new BufferedInputStream(urlFichierProp.openStream());
		      props.load(bis);
		      String driver = props.getProperty("driver");
		      String url = props.getProperty("url");
		      String utilisateur = props.getProperty("utilisateur");
		      String mdp = props.getProperty("mdp");
		      Class.forName(driver);
		      return DriverManager.getConnection(url, utilisateur, mdp);
		    }
		    finally {
		      if (bis != null) {
		        bis.close();
		      }
		    }
	 }

}
