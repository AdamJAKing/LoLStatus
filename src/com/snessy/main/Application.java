package com.snessy.main;

import java.util.Scanner;

import com.snessy.database.Database;
import com.snessy.handlers.DatabaseHandler;
import com.snessy.lol.LoLConnector;

public class Application {
	
	public static void main(String[] args){
		
		LoLConnector client = new LoLConnector();
		// Setting the database handler for listening to changes
		client.setDatabaseHandler(new DatabaseHandler());
		Database.getInstance().connect();
		
		Scanner scanner = new Scanner(System.in);
		
		while(true){
			
			System.out.println("Please select your command:\n1. Server status\n2. Player live search\n3. Player match history\n0. Exit program");
			String input = scanner.nextLine();
			
			int choice = -1;
			try{
				choice = Integer.parseInt(input);
			}catch(Exception e){
				System.out.println("Error, only numerical values are permitted!");
				// Skip this iteration
				continue;
			}
			
			switch(choice){
			case 1:
				client.downloadData(choice);
				break;
			case 2:
				client.downloadData(choice);
				break;
			case 3:
				client.downloadData(choice);
				break;
			case 0:
				System.out.println("Closing...");
				System.exit(0);
			default:
				System.out.println("Not a valid choice");
			}
		}
	}
}
