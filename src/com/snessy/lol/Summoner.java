package com.snessy.lol;

public class Summoner {
	
	public enum SummonerEventType{
		UPDATE, DELETE, INSERT
	}
	
	private String name;
	private int id;
	private String server;
	private int maxDeaths;
	private int totalChampionsKilled;
	private int maxChampionKills;
	
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
