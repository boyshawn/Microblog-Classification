package assignment3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class FileHelper {
	public static String[] retriveQueriesTermFromFile(File queryFile){

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
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e){
			e.printStackTrace();
		}

		return queries.toArray(new String[queries.size()]);	//Convert list to array
	}

	public static void writeQueryFileToSingleFolder(String baseDirectory,
			String queryFilePath, List<Configuration> configurations)
			throws IOException {
		File queryFile = new File(queryFilePath);

		//All the query from a query file will be stored in the same directory
		File queryDirectory = new File(baseDirectory + File.separator + queryFile.getName());

		if(!queryDirectory.isDirectory()){
			queryDirectory.mkdirs();
		}

		String[] queries = FileHelper.retriveQueriesTermFromFile(queryFile);

		Map<String, List<JSONObject>> queryResultMap = TweetSearch.search(queries, configurations);

		for(String query : queries){
			writeToSingleFile(queryDirectory.getAbsolutePath(), query, queryResultMap.get(query));
		}
	}

	public static void writeToSingleFile(String baseDirectory, String fileName,
			List<JSONObject> jsonQueryResult) throws IOException {

		fileName = fileName.replace(' ', '_');

		File outputFile = new File(baseDirectory + File.separator + fileName);

		if(outputFile.exists()){
			outputFile.delete();
		}

		outputFile.createNewFile();

		if (outputFile.exists()) {
			BufferedWriter writer = new BufferedWriter(
					new FileWriter(outputFile));

			for (JSONObject jsonStatus : jsonQueryResult) {
				writer.write(jsonStatus.toString());
				writer.newLine();
			}

			writer.close();
		}
	}

	public static List<Configuration> readConfigurationFromFile(String filePath) throws Exception{
		String line;
		BufferedReader reader = null;
		
		reader = new BufferedReader(new FileReader(new File(filePath)));
		List<Configuration> configurations = new ArrayList<Configuration>();
		
		String consumerKey = "";
		String consumerSecret = "";
		String accessToken = "";
		String accessTokenSecret = "";
		
		while( (line = reader.readLine()) != null){
			
			String[] lineToken = line.split(" ");

			if(line.contains("Consumer Key")){
				consumerKey = lineToken[lineToken.length - 1];
			}

			else if(line.contains("Consumer Secret")){
				consumerSecret = lineToken[lineToken.length - 1];
			}
			
			else if(line.contains("Access Token Secret")){
				accessTokenSecret = lineToken[lineToken.length - 1];
			}
			
			else if(line.contains("Access Token")){
				accessToken = lineToken[lineToken.length - 1];
			}
			
			
			if(	!consumerKey.isEmpty() && !consumerSecret.isEmpty() &&
				!accessToken.isEmpty() && !accessTokenSecret.isEmpty()){

				ConfigurationBuilder configBuilder = new ConfigurationBuilder();
				
				configBuilder.setOAuthConsumerKey(consumerKey);
				configBuilder.setOAuthConsumerSecret(consumerSecret);
				configBuilder.setOAuthAccessToken(accessToken);
				configBuilder.setOAuthAccessTokenSecret(accessTokenSecret);
				configBuilder.setJSONStoreEnabled(true);
				
				configurations.add(configBuilder.build());
				
				consumerKey = "";
				consumerSecret = "";
				accessToken = "";
				accessTokenSecret = "";
			}
		}
		
		reader.close();

		return configurations;
	}
}
