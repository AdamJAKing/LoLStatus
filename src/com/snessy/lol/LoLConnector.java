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

	public static final String SERVER_SLUGS[] = { "euw", "na", "eune", "tr",
			"oce" };

	private InputStream url;
	private BufferedReader reader;
	private ServerInformation regionToCheck;
	// Arrays vs arraylist?
	private ArrayList<JSONObject> rootObjects;
	private ArrayList<JSONObject> rootObjectsBannedChampions;
	private ArrayList<JSONObject> rootObjectSummoner;
	
	// Scanner for user input
	private Scanner scanner;
	// Listener for making changes to the database
	private DatabaseListener databaseListener;

	public LoLConnector() {
		rootObjects = new ArrayList<JSONObject>();
		rootObjectsBannedChampions = new ArrayList<JSONObject>();
		rootObjectSummoner = new ArrayList<JSONObject>();
		scanner = new Scanner(System.in);
	}

	public void downloadData(int choice) {
		
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

	private void generateLiveGameInfo() throws MalformedURLException,
			IOException, JSONException {

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
		url = new URL("https://"+server.getRegion()+".api.pvp.net/api/lol/"+server.getRegion()+"/v1.3/stats/by-summoner/"+id+"/ranked?season=SEASON2015&api_key=" + JsonFileNames.API_KEY).openStream();
		generateJson(rootObjectSummoner);
		
		// Tell the listener what changes should be made to the database
		databaseListener.summonerEvent(new Summoner(username, id), SummonerEventType.INSERT);
		
		JSONArray jArray = rootObjectSummoner.get(0).getJSONArray("champions");
		
		for(int i=0; i < jArray.length(); i++){
			JSONObject jOb = (JSONObject) jArray.get(i);
			
			if(jOb.getInt("id") == 0){
				// Getting the stats object for the player
				JSONObject ob2 = jOb.getJSONObject("stats");
				// Print out their max deaths from 1 game
				System.out.println(ob2.getInt("maxNumDeaths"));
			}
		}
		
		/*JSONArray jArray = rootObjectSummoner.get(0).getJSONArray("champions");
		JSONObject jOb = (JSONObject) jArray.get(0);
		System.out.println(id);
		System.out.println(jOb.getInt("id"));*/
	}
	
	private void displayCurrentGameData() throws JSONException, MalformedURLException, IOException{
		//TODO
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

		for (String server : SERVER_SLUGS) {
			url = new URL(serverUrl + server).openStream();
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

				System.out.println(root.get(JsonFileNames.SERVER_NAME) + ": "
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