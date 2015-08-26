package com.snessy.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Database {
	
	public static final String DATABASE_USERNAME = "root";
	public static final String DATABASE_PASS = "password";
	public static final String DATABASE = "league_of_legends";
	private static Database instance = new Database();
	
	private Connection connection;
	
	// Private constructor to stop more objects from being created
	private Database(){
		
	}
	
	// Singleton design for our database so that we always use the same object
	public static Database getInstance(){
		if(instance == null){
			instance = new Database();
		}
		return instance;
	}
	
	public Connection getConnection(){
		return connection;
	}
	
	public void connect(){
		try {
			
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+DATABASE, DATABASE_USERNAME, DATABASE_PASS);
			
			System.out.println("Connected to database.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}