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

public class FileHelper {
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
		reader.close();
		return soccerMatches;
	}

	public static void theUltimate(String queryFile, String outputFile,
			int daysBeforeMatch, int daysAfterMatch)
					throws IOException, JSONException {

		List<SoccerMatch> allMatches = FileHelper.extractMatchesFromFile(queryFile);

		File file = new File(outputFile);
		if(file.exists()){
			file.delete();
		}
		file.createNewFile();

		BufferedWriter writer = new BufferedWriter(new FileWriter(file), Integer.MAX_VALUE/30);

		for(SoccerMatch match : allMatches){
			writer.write(assembleOneMatchJson(match, daysBeforeMatch, daysAfterMatch).toString());
			writer.newLine();
		}

		writer.close();
	}
	
	private static JSONObject assembleOneMatchJson(SoccerMatch soccerMatch,
			int daysBeforeMatch, int daysAfterMatch)
			throws JSONException {

		LocalDate matchDate = soccerMatch.matchDate();
		LocalDate querySince = matchDate.minusDays(daysBeforeMatch);
		LocalDate queryUntil = matchDate.plusDays(daysAfterMatch);

		List<String> homeTeamTweets = TweetCrawler.crawlOneTerm(soccerMatch
				.getHomeTeam().replaceAll(" ", "%20"), querySince.toString(),
				queryUntil.toString());

		List<String> awayTeamTweets = TweetCrawler.crawlOneTerm(soccerMatch
				.getAwayTeam().replaceAll(" ", "%20"), querySince.toString(),
				queryUntil.toString());

		JSONObject jsonMatch = new JSONObject();
		JSONObject jsonSoccerMatch = new JSONObject(soccerMatch);

		jsonMatch.put("Match Details", jsonSoccerMatch);
		jsonMatch.put(soccerMatch.getHomeTeam(), homeTeamTweets);
		jsonMatch.put(soccerMatch.getAwayTeam(), awayTeamTweets);

		return jsonMatch;
	}
}
