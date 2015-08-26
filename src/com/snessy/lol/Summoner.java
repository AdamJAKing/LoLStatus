package com.snessy.lol;

public class Summoner {
	
	public enum SummonerEventType{
		UPDATE, DELETE, INSERT
	}
	
	private String name;
	private int id;
	
	public Summoner(String name, int id){
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}
}
