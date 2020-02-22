package utility;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

@Component
@ApplicationScope
public class DriverManagerConnectionPool implements InitializingBean {
	
	@Autowired
	private Environment env;
	private static List<Connection> freeConnections;
	private static String connectionUrl;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		freeConnections = new LinkedList<Connection>();
		connectionUrl = (String) env.getProperty("azure.jdbc.connection-string");
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	}
	
	public static Connection getConnection() throws SQLException {
		Connection conn;
		if(!freeConnections.isEmpty()) {
			//Get an istance from a the pool
			Connection c = freeConnections.get(0); //Get the last connection added
			freeConnections.remove(0);
			
			if(!c.isClosed())
				conn = c;
			else 
				conn = getConnection();
		}
		else {
			conn =  DriverManager.getConnection(connectionUrl);
		}
		
		return conn;
	}
	
	
	public static void freeConnection(Connection c) {
		try {
			if(!c.isClosed())
				freeConnections.add(c);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	
	

}
