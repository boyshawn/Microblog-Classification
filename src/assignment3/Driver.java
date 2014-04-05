package assignment3;

import java.io.IOException;

import org.json.JSONException;

public class Driver {

	public static void main(String[] args){
		String queryFile = "Resource/Queries/Training-Data-2014";
		String outputFile = "Resource/Queries/Training-Data-2014-tweet-data";
		int numberOfDaysToCrawlBeforeActualMatch = 4;
		int numberOfDaysToCrawlAfterActualMatch = 3;

		try {
			FileHelper.theUltimate(queryFile, outputFile,
					numberOfDaysToCrawlBeforeActualMatch,
					numberOfDaysToCrawlAfterActualMatch);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}