package com.divrep.samples.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.TimeZone;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class Context {
	protected Connection connection;
	private String context_name;
	
	public Connection getConnection() throws SQLException
	{
		if(!isValid()) {
			connect();
		}
		
		return connection;
	}
	
	public boolean isValid() {
		if (connection == null)
			return false;
		try {
			if (connection.isClosed())
				return false;
			connection.getMetaData();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	
	public Context(String _context_name)
	{
		context_name = _context_name;
	}
	public void connect() throws SQLException
	{
		try {
			javax.naming.Context initContext = new InitialContext();
			javax.naming.Context envContext = (javax.naming.Context)initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource)envContext.lookup(context_name);
		
			System.out.println("Connecting to " + context_name);
			connection = ds.getConnection();
			System.out.println("Success!");

			initContext.close();
			TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		} catch (NamingException e) {
			System.out.println(e.toString());
		}
	}
	protected void finalize() 
	{
		//just in case user forget to do this
		try {
			close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close() throws SQLException 
	{
		if(connection != null && !connection.isClosed()) {
			System.out.println("Closing connection");
			connection.close();

		}

	}
}
