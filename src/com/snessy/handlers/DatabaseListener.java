package com.snessy.handlers;

import com.snessy.lol.Summoner;
import com.snessy.lol.Summoner.SummonerEventType;

// Listener for the database that allows handling of updating SQL
public interface DatabaseListener {
	
	public void summonerEvent(Summoner summoner, SummonerEventType summonerEvent);
}
