League of Legends - Java Application

Simple application that grabs JSON data from the League API. 

Currently displays information about server status and live games as well as storing information in a SQL database

Current features
* Live server status
* Live player searches supported in 5 regions
* SQL database for storing player information

Planned features
* Match history
* Item information

Possible features
* Champion information
* Player search (non-live game version) -- Ties in with match history

===================================================================================

Setup:

This wiki assumes that you're using the MySQL workbench as well as the standard JDBC driver for Java

SQL
1. Import the league_of_legends_summoner_searches.sql it into your MySQL workbench - the dump will handle all the required creation

LOL-DEVELOPER
1. You will need a League of Legends developer account to develop the application. Go to: https://developer.riotgames.com and sign in - navigate to your API key (this is generated when you create your account)

JAVA
1. Naviagte to the res folder inside of the project and file named ApiKey.txt. Simply paste your key into this file
2. Open up Database.Java and change DATABASE_USERNAME/PASS to your settings. If you're using a different port, don't forget to change this as well.
