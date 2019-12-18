package utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DatabaseSingleton 
{
	private static DatabaseSingleton instance;
//	private static String db_url;
//	private static String db_user;
//	private static String db_password;
//	private static String driver;
	private Connection connection;
	private Statement statement;
	public Statement getStatement() {return statement;}
	String connectionUrl =
            "jdbc:sqlserver://database-ias.database.windows.net:1433;"
                    + "database=db-test-ias;"
                    + "user=gdipalma@database-ias;"
                    + "password=Password01;"
                    + "encrypt=true;"
                    + "trustServerCertificate=false;"
                    + "hostNameInCertificate=*.database.windows.net;"
                    + "loginTimeout=30;";	

	
	private DatabaseSingleton() throws ClassNotFoundException
	{
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		getConnect();
	}
	
	public static synchronized DatabaseSingleton getInstance() throws ClassNotFoundException
	{
		if(instance == null)
		{
			instance = new DatabaseSingleton();
		}
		return instance;
	}
	
	private void getConnect()
	{
		if(connection == null || statement == null)
		{
			try 
			{
				connection = DriverManager.getConnection(connectionUrl);
				statement = connection.createStatement();
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public void ChiudiConnessione()
	{
		connection = null;
		statement = null;
	}
	
	public int EseguiQueryUpdate(String stringa)
	{
		try
		{
			return statement.executeUpdate(stringa);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	
	public ResultSet EseguiQuery(String query)
	{
		try 
		{	
			return statement.executeQuery(query);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
}
