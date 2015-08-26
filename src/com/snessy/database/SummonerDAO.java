package com.snessy.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Database access object pattern that allows us to create a way of getting/removing/inputting/updating data for summoners in the summoner table
// Works with the databasehandler class for listening to changes to the database
public class SummonerDAO {
	
	public static final String TABLE_NAME = "summoner_searches";
	
	public void addSummoner(String name, int id, String server, int maxDeaths, int totalChampionsKilled, int maxChampionKills){
		Connection connection = Database.getInstance().getConnection();
		
		// Checks if the username is already in the database
		if(getSummoner(id) == null){
			try {
				PreparedStatement myStm = connection.prepareStatement("INSERT into "+TABLE_NAME+" (username, id, server, max_deaths, total_champions_killed, max_champion_kills)"
						+ " VALUES (?, ?, ?, ?, ?, ?)");
			
				// Index for columns in the database
				int paramIndex=1;
				myStm.setString(paramIndex++, name);
				myStm.setInt(paramIndex++, id);
				myStm.setString(paramIndex++, server );
				myStm.setInt(paramIndex++, maxDeaths);
				myStm.setInt(paramIndex++, totalChampionsKilled);
				myStm.setInt(paramIndex++, maxChampionKills);
			
				myStm.executeUpdate();
			
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			// If the user is inside the database then we won't add it, and will display this message
			System.out.println("Failed to add summoner to database '"+name+"' already exists");
		}
	}
	
	public String getSummoner(int id){
		Connection connection = Database.getInstance().getConnection();
		
		try {
			PreparedStatement myStm = connection.prepareStatement("SELECT id FROM " + TABLE_NAME + " WHERE id=?");
			myStm.setInt(1, id);
			
			ResultSet rs = myStm.executeQuery();
			if(rs.next()){
				return rs.getString("id");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void deleteSummoner(String name){
		
	}
	
	public void updateSummoner(){
		
	}
}
