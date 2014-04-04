package assignment3;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class Driver {

	public static void lolo(int args) throws IOException {	
		final long startTime = System.currentTimeMillis();
		
		String fileName = "Resource" + File.separator
				+ "Application_data_credential";
		
		try {
			List<Configuration> configurations = FileHelper
					.readConfigurationFromFile(fileName);
			
			final String baseDirectory = "Resource";
			String queryFilePath = baseDirectory + File.separator + "Queries" + File.separator + "Manchester_United";

			FileHelper.writeQueryFileToSingleFolder(baseDirectory, queryFilePath, configurations);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		final long endTime = System.currentTimeMillis();
		double timeTaken = (endTime - startTime)/ 100;
		System.out.println("Total execution time: " + timeTaken );
	}



	public static void main(String[] args) throws IOException{
		final long startTime = System.currentTimeMillis();
		
		String gogogo = "15/3/2014	15:00	Fulham FC	-	Newcastle United	1:0 (0:0) ";
				
		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
		configurationBuilder.setJSONStoreEnabled(true);
		
		configurationBuilder.setOAuthConsumerKey("cWi74LP44GSXTx9g2yCIY4Tyu");
		configurationBuilder.setOAuthConsumerSecret("AnQQX6O6LdY4oQHFBlwMl9rJZcQLgATon62aJmOh8mKjxs3L73");
		configurationBuilder.setOAuthAccessToken("2425629150-shkCsUbbsdgr3HuOGPGCO7j1YEqFRWwRIMXqgt8");
		configurationBuilder.setOAuthAccessTokenSecret("C8CMYzgM5WOhLIa8NWNNYmB59N3JZUqkAaD7bfKuubp6s");
		
		SoccerMatch soccerMatch = SoccerMatch.extractOneSoccerMatch(gogogo);
		try {
			JSONObject oneMatch = TweetSearch.searchTweetForMatch(soccerMatch, 3,
					3, 100, configurationBuilder.build());
			
			File directory = new File("Resource");
			File outFile = new File(directory, "oneMatch");
			
			if(!outFile.exists()){
				outFile.createNewFile();
			}
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
			writer.write(oneMatch.toString(4));
			
		} catch (TwitterException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		final long endTime = System.currentTimeMillis();
		double timeTaken = (endTime - startTime)/ 1000;
		System.out.println("Total execution time: " + timeTaken );
	}
}
