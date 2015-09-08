package com.snessy.lol;

import org.json.JSONException;
import org.json.JSONObject;

public class MatchHistory {
	
	private String queueType;
	private int pentaKills;
	private int quadKills;
	private int tripleKills;
	private int doubleKills;
	private int kills;
	private int deaths;
	private int assists;
	private int towerKills;
	
	public MatchHistory(JSONObject jsonObject) {
		try {
			pentaKills = jsonObject.getInt("pentaKills");
			quadKills = jsonObject.getInt("quadraKills");
			tripleKills = jsonObject.getInt("tripleKills");
			doubleKills = jsonObject.getInt("doubleKills");
			kills = jsonObject.getInt("kills");
			deaths = jsonObject.getInt("deaths");
			assists = jsonObject.getInt("assists");
			towerKills = jsonObject.getInt("towerKills");
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
