package com.snessy.lol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.snessy.handlers.DatabaseListener;
import com.snessy.lol.Summoner.SummonerEventType;

public class LoLConnector {

	private InputStream url;
	private BufferedReader reader;
	private ServerInformation regionToCheck;
	// Arrays or arraylist?
	private ArrayList<JSONObject> rootObjects;
	private ArrayList<JSONObject> rootObjectsBannedChampions;
	private ArrayList<JSONObject> rootObjectSummoner;
	private ArrayList<JSONObject> rootObjectMatchHistory;
	
	// Scanner for user input
	private Scanner scanner;
	// Listener for making changes to the database
	private DatabaseListener databaseListener;

	public LoLConnector() {
		// TODO each have own class and subclasses of data
		rootObjects = new ArrayList<JSONObject>();
		rootObjectsBannedChampions = new ArrayList<JSONObject>();
		rootObjectSummoner = new ArrayList<JSONObject>();
		rootObjectMatchHistory = new ArrayList<JSONObject>();
		scanner = new Scanner(System.in);
	}

	public void downloadData(int choice) {
		System.out.println(JsonFileNames.API_KEY);
		resetData();
		
		try {
			switch (choice) {
			case 1:
				generateServerInfo();
				displayServerData();
				break;
			case 2:
				generateLiveGameInfo();
				break;
			case 3:
				generateMatchHistory();
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out
					.println("Couldn't find user, please check the username/region. Is there a game in progress?\n");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private void generateMatchHistory() throws JSONException, MalformedURLException, IOException{
		// Generate the username for parsing to the url and the database name as the user enters it
		String[] names = parseUsername();
		System.out.println("Please select the region");
		regionToCheck = getServerChoice();
		
		// Need to grab the summonerId before we can make a call to the API
		String summonerIdUrl = getUsernameId(names[0], regionToCheck);
		url = new URL(summonerIdUrl).openStream();
		generateJson(rootObjects);
		int summonerId = rootObjects.get(0).getJSONObject(names[0]).getInt("id");
		
		// Generate the URL for the API call such as region and the username
		String rawUrl = "https://"+regionToCheck.getRegion()+".api.pvp.net/api/lol/"+regionToCheck.getRegion()+"/v2.2/matchhistory/"+summonerId+"/?api_key="+JsonFileNames.API_KEY;
		url = new URL(rawUrl).openStream();
		generateJson(rootObjectMatchHistory);
		
		// Get the first index of the arraylist to get the current search. 
		
		Summoner summoner = new Summoner();
		JSONArray array = rootObjectMatchHistory.get(0).getJSONArray("matches");
		for(int i=0; i < array.length(); i++){
			
			/* This will make it so we skip the first 5 games in the list as they matches are returned in reverse order. Only happens if the array
			 * has a length less than 5
			 */
			
			// TODO Likely change this to store 10 games and then simply limit it within the display data
			if(i < 5 && array.length() > 5){
				continue;
			}
			
			// Grabbing the object inside of the array, so we can get keys via the JSON to store inside our MatchHistory class
			JSONObject ob = array.getJSONObject(i);
			/* Get the array of participants and then get the first index as we only want the player info we are searching for. Then grab the stats 
			 * object.
			 */
			
			summoner.addMatch(new MatchHistory(ob.getJSONArray("participants").getJSONObject(0).getJSONObject("stats")));
		}
		
		displayMatchHistory(summoner);
	}
	
	private void displayMatchHistory(Summoner summoner) {
		if(!summoner.getMatchHistoryList().isEmpty()){
			int counter=0;
			for(MatchHistory match : summoner.getMatchHistoryList()){
			System.out.println("Match " + ++counter+": " + match.toString());
			}
		}
	}

	private String[] parseUsername(){
		System.out.println("Please enter summoner name.");
		String name = scanner.nextLine();
		String databaseName = name;
		name = name.toLowerCase().replace(" ", "");
		
		return new String[] {name, databaseName};
		
	}

	private void generateLiveGameInfo() throws MalformedURLException,
			IOException, JSONException {

		// TODO Change method to use the new parseUsername method
		
		System.out.println("Please enter summoner name.");
		String name = scanner.nextLine();
		// Storing the unedited name for storing into the database
		String databaseName = name; 
		name = name.toLowerCase().replace(" ", "");

		// TODO generate list of servers
		System.out.println("Please select the region.");
		regionToCheck = getServerChoice();
		
		// We have to get the summonerID before we can get the current game
		String summonerIdURL = getUsernameId(name, regionToCheck);

		// System.out.println("Please enter region");
		// Grabbing the url, opening the connection and generating the json
		url = new URL(summonerIdURL).openStream();
		generateJson(rootObjects);

		// Get the object (user) and then get the user id (variable)
		int summonerId = rootObjects.get(0).getJSONObject(name).getInt("id");
		// Grab the url of the current game that the user is in
		String gameUrl = getCurrentGameInfo(summonerId, regionToCheck);
		grabSummonerInfo(databaseName,summonerId, regionToCheck);
		
		url = new URL(gameUrl).openStream();
		// Clear the rootObjects as we only care about the current game.
		rootObjects.clear();
		generateJson(rootObjects);
		displayCurrentGameData();
	}
	
	private void grabSummonerInfo(String username, int id, ServerInformation server) throws MalformedURLException, IOException, JSONException{
		
		// Clear the list of summoners as we only want the first index
		rootObjectSummoner.clear();
		url = new URL("https://"+server.getRegion()+".api.pvp.net/api/lol/"+server.getRegion()+"/v1.3/stats/by-summoner/"+id+"/ranked?season=SEASON2015&api_key=" + JsonFileNames.API_KEY).openStream();
		generateJson(rootObjectSummoner);
		
		JSONArray jArray = rootObjectSummoner.get(0).getJSONArray("champions");
		
		int maxDeaths=0;
		int maxChampionsKilled=0;
		int totalChampionKills=0;;
		
		for(int i=0; i < jArray.length(); i++){
			JSONObject jObject = (JSONObject) jArray.get(i);
			if(jObject.getInt("id") == 0){
				
				JSONObject summonerObjectInfo = jObject.getJSONObject("stats");
				
				maxDeaths = summonerObjectInfo.getInt("maxNumDeaths");
				maxChampionsKilled = summonerObjectInfo.getInt("maxChampionsKilled");
				totalChampionKills = summonerObjectInfo.getInt("totalChampionKills");
			}
		}
		// Tell the listener what changes should be made to the database
		databaseListener.summonerEvent(new Summoner(username, id, server.getRegion(), maxDeaths, totalChampionKills, maxChampionsKilled), SummonerEventType.INSERT);
	}
	
	private void displayCurrentGameData() throws JSONException, MalformedURLException, IOException{
		JSONArray participants = rootObjects.get(0).getJSONArray("participants");

		System.out.println("Participants:");
		for (int i = 0; i < participants.length(); i++) {
			JSONObject jsonOb = (JSONObject) participants.get(i);
			System.out.println(jsonOb.get("summonerName"));
		}
		
		JSONArray bannedChampions = rootObjects.get(0).getJSONArray("bannedChampions");
		System.out.println("\nBanned champs:");
		
		for(int i=0; i < bannedChampions.length(); i++){
			
			JSONObject jsonOb = (JSONObject)bannedChampions.get(i);
			getChampionInformation(jsonOb.getInt("championId"));
		}
		
		for(JSONObject ob : rootObjectsBannedChampions){
			System.out.println(ob.getString("name"));
		}
		
		System.out.println();
	}
	
	private void getChampionInformation(int id) throws JSONException, MalformedURLException, IOException{
		url = new URL("https://global.api.pvp.net/api/lol/static-data/"+regionToCheck.getRegion()+"/v1.2/champion/"+id+"?api_key=" + JsonFileNames.API_KEY).openStream();
		generateJson(rootObjectsBannedChampions);
	}

	private ServerInformation getServerChoice() {
		// Prints out the regions in a list.

		for (ServerInformation serverInfo : ServerInformation.values()) {
			int indexOfEnum = serverInfo.ordinal() + 1;
			System.out.print(indexOfEnum + "." + serverInfo.getRegion() + " ");
		}

		int regionChoice = scanner.nextInt();
		// Have to call scanner.NextLine() as the nextInt method does not read
		// the last newline character of the input, so it consumes the next line
		scanner.nextLine();
		ServerInformation server = null;

		switch (regionChoice) {
		case 1:
			server = ServerInformation.EUW;
			break;
		case 2:
			server = ServerInformation.NA;
			break;
		case 3:
			server = ServerInformation.EUNE;
			break;
		case 4:
			server = ServerInformation.TR;
			break;
		case 5:
			server = ServerInformation.OCE;
			break;
		default:
			System.out.println("Region not valid");
		}
		
		return server;
	}

	private void generateServerInfo() throws MalformedURLException,
			IOException, JSONException {
		String serverUrl = "http://status.leagueoflegends.com/shards/";

		for (ServerInformation server : ServerInformation.values()) {
			url = new URL(serverUrl + server.getRegion()).openStream();
			generateJson(rootObjects);
		}
	}

	private String getCurrentGameInfo(int summonerId, ServerInformation server) {
		// TODO Allow the user to enter in the region
		return "https://"
				+ server.getRegion()
				+ ".api.pvp.net/observer-mode/rest/consumer/getSpectatorGameInfo/"
				+ server.getRegionId() + "/" + summonerId + "?api_key="
				+ JsonFileNames.API_KEY;

	}

	private void generateJson(ArrayList<JSONObject> objectList) throws JSONException {
		reader = new BufferedReader(new InputStreamReader(url));

		String jsonRaw = readData(reader);
		objectList.add(new JSONObject(jsonRaw));
	}

	private String getUsernameId(String username, ServerInformation server) {
		// TODO Allow the user to enter in the region
		return "https://" + server.getRegion() + ".api.pvp.net/api/lol/"
				+ server.getRegion() + "/v1.4/summoner/by-name/" + username
				+ "?api_key=" + JsonFileNames.API_KEY;
	}

	private void displayServerData() {

		for (JSONObject root : rootObjects) {
			try {
				JSONObject ob = (JSONObject) root.getJSONArray("services").get(
						1);
				
				// First name is the server, second is the name of the service such as game or forum
				System.out.println(root.get("name") + ": "
						+ ob.getString("name") + " " + ob.get("status"));

			} catch (JSONException e) {
				System.out.println(e);
			}
		}
		System.out.println("");
	}

	// Simply read the data and store it in a stringbuilder object for returning
	private String readData(BufferedReader reader) {
		StringBuilder sb = new StringBuilder();

		try {

			// Read the string from the web and store it inside the
			// StringBuilder
			String nextLine;
			while ((nextLine = reader.readLine()) != null) {
				sb.append(nextLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	public void resetData(){
		regionToCheck = null;
		rootObjects.clear();
		rootObjectsBannedChampions.clear();
	}

	public void setDatabaseHandler(DatabaseListener listener) {
		this.databaseListener = listener;
	}
}