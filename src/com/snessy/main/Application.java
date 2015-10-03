package com.snessy.main;

import java.util.Scanner;

import javax.swing.JFrame;

import com.snessy.database.Database;
import com.snessy.gui.MainPanel;
import com.snessy.handlers.DatabaseHandler;
import com.snessy.lol.LoLConnector;

public class Application {
	
	public static final String TITLE = "LoL stats and live match search";
	public static final double VERSION = 1.0;
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	
	public static void main(String[] args){
		
		// TODO Change the application to use GUI rather than console
		JFrame frame = new JFrame();
		frame.setVisible(true);
		frame.setSize(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle(TITLE);
		
		frame.add(new MainPanel());
		JFrame applicationFrame = new JFrame();
		applicationFrame.setVisible(true);
		applicationFrame.setTitle("LoLStatus " + VERSION);
		applicationFrame.setSize(WIDTH, HEIGHT);
		applicationFrame.add(new MainPanel());
		
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
			case 2:
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
