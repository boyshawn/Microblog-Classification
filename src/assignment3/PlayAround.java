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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayAround {

	public static void lolo(String[] args) {	
		final long startTime = System.currentTimeMillis();
		//Create the configuration file
		ConfigurationBuilder configBuilder = new ConfigurationBuilder();
		configBuilder.setOAuthConsumerKey("M05ZmqSEmbkF58R3lkXqkw");
		configBuilder.setOAuthConsumerSecret("7Ek6APNfrnDGjTd6C7MjywXPBHSsufXLXf3FEe2qMN8");
		configBuilder.setOAuthAccessToken("2416996622-FvpVEzCFF2zRKoVZ0ghSL8f2zPreZiHTALSMFyA");
		configBuilder.setOAuthAccessTokenSecret("S0rDfW0Vp8MoIGeHYZMWuBibvEnOFN384z35T8wb7aQOq");
		configBuilder.setJSONStoreEnabled(true);

		TweetSearch.singleQuerySearch("Manchester City", configBuilder.build());
		
		final long endTime = System.currentTimeMillis();
		double timeTaken = (endTime - startTime)/ 100;
		System.out.println("Total execution time: " + timeTaken );
		
	}
	
	public static void main(String[] args){
		String fileName = "src/assignment3/keywords";
		File queryFile = new File(fileName);
		System.out.println(queryFile.exists());

		//TweetSearch.search(queriesArray, configuration);
	}
	
	public static String[] retriveQueriesTermFromFile(String fileName){
		File queryFile = new File(fileName);

		String line;
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(queryFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		List<String> queries = new ArrayList<String>();

		try {
			while( (line = reader.readLine()) != null){
				if(!line.startsWith("//")){	//The text file contain some comments
					queries.add(line);
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e){
			e.printStackTrace();
		}

		return queries.toArray(new String[queries.size()]);
	}

}
