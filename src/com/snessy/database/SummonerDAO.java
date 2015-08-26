package com.snessy.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// Database access object pattern that allows us to create a way of getting/removing/inputting/updating data for summoners in the summoner table
// Works with the databasehandler class for listening to changes to the database
public class SummonerDAO {
	
	public static final String TABLE_NAME = "summoner_searches";
	
	public void addSummoner(String name, int id){
		Connection connection = Database.getInstance().getConnection();
		try {
			PreparedStatement myStm = connection.prepareStatement("INSERT into "+TABLE_NAME+" (username, id) VALUES (?, ?)");
			
			int paramIndex=1;
			myStm.setString(paramIndex++, name);
			myStm.setInt(paramIndex++, id);
			
			myStm.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void getSummoner(String name){
		
	}
	
	public void deleteSummoner(String name){
		
	}
	
	public void updateSummoner(){
		
	}
}
