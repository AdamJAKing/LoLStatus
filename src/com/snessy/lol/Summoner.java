package com.snessy.lol;

import java.util.ArrayList;

public class Summoner {
	
	public enum SummonerEventType{
		UPDATE, DELETE, INSERT
	}
	
	private String name;
	private int id;
	private ArrayList<MatchHistory> matches = new ArrayList<MatchHistory>();
	private String server;
	private int maxDeaths;
	private int totalChampionsKilled;
	private int maxChampionKills;
	
	// This constructor is used for the matchhistory data as well don't want to store the information within the database, i.e: don't need kills etc
	public Summoner() {
	}
	
	// Standard constructor for storing information about the summoner that will be stored
	public Summoner(String name, int id, String server, int maxDeaths, int totalChampionsKilled, int maxChampionKills){
		this.name = name;
		this.id = id;
		this.server = server;
		this.maxDeaths = maxDeaths;
		this.totalChampionsKilled = totalChampionsKilled;
		this.maxChampionKills = maxChampionKills;
	}

	public String getName() {
		return name;
	}
	
	public String getServer(){
		return server;
	}

	public int getId() {
		return id;
	}

	public int getMaxDeaths() {
		return maxDeaths;
	}

	public int getTotalChampionsKilled() {
		return totalChampionsKilled;
	}

	public int getMaxChampionsKills() {
		return maxChampionKills;
	}
	
}
