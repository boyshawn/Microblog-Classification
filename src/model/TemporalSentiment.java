package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class TemporalSentiment {
	private static String baseWritePath = "C:\\Users\\Francis Pang\\Google Drive\\Acadmic folder\\CS4242 Social Media\\Assignment 2 Temporal Tweet JSON\\";
	
	public static void main (String[] args){
		String baseFilePath = "C:\\Users\\Francis Pang\\Google Drive\\Acadmic folder\\CS4242 Social Media\\Assignment 2 classified Tweet JSON\\";
		
		File[] files = new File(baseFilePath).listFiles();
		for (int i = 0; i < files.length; i++) {
			
			//Reading
			Map<String, List<JSONObject>> map = divideTweetsByTime(files[i]);
			
			//Writing
			try {
				writeToFiles(files[i], map);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void writeToFiles(File file, Map<String, List<JSONObject>> map) throws Exception{
		String fileName = file.getName();
		String fileNameNoExtension = fileName.substring(0, fileName.lastIndexOf("."));
		String writtenFilePath;
		BufferedWriter writer;
		
		//Create Folder 
		File newDirectory = new File(baseWritePath + fileNameNoExtension);
		if(!newDirectory.isDirectory()){
			newDirectory.mkdir();
		}
		String newDirectoryPath = newDirectory.getAbsolutePath();
		
		writeOneFileInAFolder("Oct 15", newDirectoryPath, map);
		writeOneFileInAFolder("Oct 16", newDirectoryPath, map);
		writeOneFileInAFolder("Oct 17", newDirectoryPath, map);
		writeOneFileInAFolder("Oct 18", newDirectoryPath, map);
		writeOneFileInAFolder("Oct 19", newDirectoryPath, map);
		writeOneFileInAFolder("Oct 20", newDirectoryPath, map);
	}
	
	private static void writeOneFileInAFolder(String timeFrame,
			String folderDirectory, Map<String, List<JSONObject>> map) throws IOException {
		//Static
		List<JSONObject> jsonTweets = map.get(timeFrame);
		String writtenFilePath = folderDirectory + File.separator + timeFrame + ".txt";
		BufferedWriter writer = null;
		
		if(jsonTweets.size() > 0){
			File writtenFile = new File(writtenFilePath);
			if(!writtenFile.exists()){
				writtenFile.createNewFile();
			}
			writer = new BufferedWriter(new FileWriter(writtenFile));
			
			for(int i = 0; i < jsonTweets.size(); i++){
				writer.write(jsonTweets.get(i).toString());
				writer.newLine();
			}
			
			writer.close();
		}
	}
	
	private static Map<String, List<JSONObject>> divideTweetsByTime(File file){
		BufferedReader reader = null;
		
		List<JSONObject> oct15Tweets = new ArrayList<JSONObject>();
		List<JSONObject> oct16Tweets = new ArrayList<JSONObject>();
		List<JSONObject> oct17Tweets = new ArrayList<JSONObject>();
		List<JSONObject> oct18Tweets = new ArrayList<JSONObject>();
		List<JSONObject> oct19Tweets = new ArrayList<JSONObject>();
		List<JSONObject> oct20Tweets = new ArrayList<JSONObject>();
		
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				JSONObject newTweet = new JSONObject(line);
				
				String date = newTweet.getString("created_at");
				
				if (date.contains("Oct 15")) { // 15 Oct
					oct15Tweets.add(newTweet);
				} else if (date.contains("Oct 16")) { // 16 Oct
					oct16Tweets.add(newTweet);
				} else if (date.contains("Oct 17")) { // 17 Oct
					oct17Tweets.add(newTweet);
				}
				else if (date.contains("Oct 18")) { // 18 Oct
					oct18Tweets.add(newTweet);
				} else if (date.contains("Oct 19")) { // 19 Oct
					oct19Tweets.add(newTweet);
				} else if (date.contains("Oct 20")) { // 20 Oct
					oct20Tweets.add(newTweet);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		Map<String, List<JSONObject>> map = new HashMap<String, List<JSONObject>>();

		map.put("Oct 15", oct18Tweets);
		map.put("Oct 16", oct19Tweets);
		map.put("Oct 17", oct20Tweets);
		map.put("Oct 18", oct18Tweets);
		map.put("Oct 19", oct19Tweets);
		map.put("Oct 20", oct20Tweets);
		
		return map;
	}
}
