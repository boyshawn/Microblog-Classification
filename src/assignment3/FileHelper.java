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

	public static void writeQueryFileToSingleFolder(String baseDirectory, String queryFilePath, Configuration configuration) throws IOException{
		File queryFile = new File(queryFilePath);
		
		//All the query from a query file will be stored in the same directory
		File queryDirectory = new File(baseDirectory + File.separator + queryFile.getName());

		if(!queryDirectory.isDirectory()){
			queryDirectory.mkdirs();
		}
		
		String[] queries = FileHelper.retriveQueriesTermFromFile(queryFile);
		
		Map<String, List<JSONObject>> queryResultMap = TweetSearch.search(queries, configuration);
		
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
}
