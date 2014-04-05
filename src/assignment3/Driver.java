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

import org.joda.time.LocalDate;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	
	public static void lolo(int[] args){
		List<SoccerMatch> allMatches;
		try {
			allMatches = extractMatchesFromFile("Resource/Queries/Training-Data-2014");
			
			SoccerMatch soccerMatch = allMatches.get(0);
			
			
			JSONObject jsonMatch = new JSONObject(soccerMatch);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	public static void main(String[] args){
		try {
			List<SoccerMatch> allMatches = extractMatchesFromFile("Resource/Queries/Training-Data-2014");
			
			SoccerMatch soccerMatch = allMatches.get(0);
			LocalDate matchDate = soccerMatch.matchDate();
			LocalDate querySince = matchDate.minusDays(3);
			LocalDate queryUntil = matchDate.plusDays(3);
			
			List<String> homeTeamTweets = crawlOneTerm(soccerMatch.getHomeTeam().replaceAll(" ", "%20"),
					querySince.toString(), queryUntil.toString());
			
			List<String> awayTeamTweets = crawlOneTerm(soccerMatch.getAwayTeam().replaceAll(" ", "%20"),
					querySince.toString(), queryUntil.toString());
			
			String outputFile = "try-oneJson";
			File file = new File(outputFile);
			file.createNewFile();
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			
			writer.write(assembleOneMatchJson(soccerMatch, homeTeamTweets, awayTeamTweets).toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	public static JSONObject assembleOneMatchJson(SoccerMatch soccerMatch,
			List<String> homeTeamTweets, List<String> awayTeamTweets)
			throws JSONException {
		
		JSONObject jsonMatch = new JSONObject();
		JSONObject jsonSoccerMatch = new JSONObject(soccerMatch);
		
		jsonMatch.put("Match Details", jsonSoccerMatch);
		jsonMatch.put(soccerMatch.getHomeTeam(), homeTeamTweets);
		jsonMatch.put(soccerMatch.getAwayTeam(), awayTeamTweets);
		
		return jsonMatch;
	}
	
	public static List<String> crawlOneTerm(String query, String sinceDate, String untilDate){
		TweetCrawler tweetCrawler = new TweetCrawler(query, sinceDate, untilDate);
		return tweetCrawler.testIt();
	}
	
	public static List<SoccerMatch> extractMatchesFromFile(String filePath) throws IOException{
		File file = new File(filePath);
		List<SoccerMatch> soccerMatches = new ArrayList<SoccerMatch>();
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		String line;
		
		while( (line=reader.readLine()) != null ){
			if(!line.startsWith("//")){	//The file can contain comment
				soccerMatches.add(SoccerMatch.extractOneSoccerMatch(line));
			}
		}
		return soccerMatches;
	}
}
