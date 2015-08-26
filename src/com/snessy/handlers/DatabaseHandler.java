package com.snessy.handlers;

import com.snessy.database.SummonerDAO;
import com.snessy.lol.Summoner;
import com.snessy.lol.Summoner.SummonerEventType;

public class DatabaseHandler implements DatabaseListener{
	
	SummonerDAO summonerDAO;
	
	 public DatabaseHandler() {
		summonerDAO = new SummonerDAO();
	}

	 // Implementing the interface methods which will be called as a listener and allows us to make decisions/changes to the database
	@Override
	public void summonerEvent(Summoner summoner, SummonerEventType summonerEvent) {
		switch(summonerEvent){
		case INSERT:
			summonerDAO.addSummoner(summoner.getName(), summoner.getId(), summoner.getServer(), summoner.getMaxDeaths(), summoner.getTotalChampionsKilled()
					, summoner.getMaxChampionsKills());
			break;
		case UPDATE:
			break;
		case DELETE:
			break;
		}
	}
}
