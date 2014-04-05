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
	
	public static void main(String[] args){
		String query = "Fulham FC";
		int stopDate = 14;
		int stopMonth = 3;
		
		TweetCrawler tweetCrawler = new TweetCrawler(query, stopMonth, stopDate);
		tweetCrawler.testIt();
		
	}
}
