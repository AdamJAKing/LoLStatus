package com.snessy.lol;

public enum ServerInformation {
	EUW ("euw", "EUW1"),
	NA ("na", "NA1"),
	EUNE ("eune", "EUN1"),
	TR ("tr", "TR1"),
	OCE("oce", "OC1");
		
	private String region;
	private String regionId;
	public static final int SIZE = ServerInformation.values().length;
		
	ServerInformation(String region, String regionId){
		this.region = region;
		this.regionId = regionId;
	}
		
	public String getRegion(){
		return region;
	}
		
	public String getRegionId(){
		return regionId;
	}
}
