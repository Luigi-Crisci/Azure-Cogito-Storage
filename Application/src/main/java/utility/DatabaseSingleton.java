package utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DatabaseSingleton 
{
	private static DatabaseSingleton instance;
	private Connection connection;
	private Statement statement;
	public Statement getStatement() {return statement;}
	
    String connectionUrl = "Deleted";
	
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
				connection.setAutoCommit(false);
				statement = connection.createStatement();
			} 
			catch (SQLException e){
				e.printStackTrace();
			}
		}
	}
	
	public void ChiudiConnessione() throws SQLException
	{
		connection.close();
		connection = null;
		statement = null;
	}
	
	public int EseguiQueryUpdate(String stringa) throws SQLException
	{
		try{
			int i = statement.executeUpdate(stringa);
			connection.commit();
			return i;
		}
		catch (SQLException e){
			e.printStackTrace();
			connection.rollback();
			return 0;
		}
	}
	
	
	public ResultSet EseguiQuery(String query) throws SQLException
	{
		try{	
			ResultSet rs= statement.executeQuery(query);
			connection.commit();
			return rs;
		} 
		catch (SQLException e){
			
			connection.rollback();
			e.printStackTrace();
			return null;
		}
	}
}
