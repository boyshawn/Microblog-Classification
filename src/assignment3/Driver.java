package assignment3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.json.JSONException;
import org.json.JSONObject;

public class Driver {

	public static void main(String[] args){
		String queryFile = "Resource/Queries/Training-Data-2014";
		String outputFile = "Resource/Queries/Training-Data-2014-tweet-data";
		int numberOfDaysToCrawlBeforeActualMatch = 4;
		int numberOfDaysToCrawlAfterActualMatch = 3;

		try {
			theUltimate(queryFile, outputFile,
					numberOfDaysToCrawlBeforeActualMatch,
					numberOfDaysToCrawlAfterActualMatch);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static void theUltimate(String queryFile, String outputFile,
			int daysBeforeMatch, int daysAfterMatch)
			throws IOException, JSONException {
		
			List<SoccerMatch> allMatches = extractMatchesFromFile(queryFile);

			File file = new File(outputFile);
			if(file.exists()){
				file.delete();
			}
			file.createNewFile();

			BufferedWriter writer = new BufferedWriter(new FileWriter(file));

			for(SoccerMatch match : allMatches){
				writer.write(assembleOneMatchJson(match, daysBeforeMatch, daysAfterMatch).toString());
				writer.newLine();
			}
			
			writer.close();
	}

	public static JSONObject assembleOneMatchJson(SoccerMatch soccerMatch,
			int daysBeforeMatch, int daysAfterMatch)
			throws JSONException {

		LocalDate matchDate = soccerMatch.matchDate();
		LocalDate querySince = matchDate.minusDays(daysBeforeMatch);
		LocalDate queryUntil = matchDate.plusDays(daysAfterMatch);

		List<String> homeTeamTweets = crawlOneTerm(soccerMatch.getHomeTeam().replaceAll(" ", "%20"),
				querySince.toString(), queryUntil.toString());

		List<String> awayTeamTweets = crawlOneTerm(soccerMatch.getAwayTeam().replaceAll(" ", "%20"),
				querySince.toString(), queryUntil.toString());

		JSONObject jsonMatch = new JSONObject();
		JSONObject jsonSoccerMatch = new JSONObject(soccerMatch);

		jsonMatch.put("Match Details", jsonSoccerMatch);
		jsonMatch.put(soccerMatch.getHomeTeam(), homeTeamTweets);
		jsonMatch.put(soccerMatch.getAwayTeam(), awayTeamTweets);

		return jsonMatch;
	}

	public static List<String> crawlOneTerm(String query, String sinceDate,
			String untilDate) {
		
		TweetCrawler tweetCrawler = new TweetCrawler(query, sinceDate,
				untilDate);
		return tweetCrawler.testIt();
	}

	public static List<SoccerMatch> extractMatchesFromFile(String filePath)
			throws IOException {
		
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
