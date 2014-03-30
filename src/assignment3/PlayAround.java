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

import org.json.JSONObject;

public class PlayAround {

	public static void main(String[] args) throws IOException {	
		final long startTime = System.currentTimeMillis();
		
		String fileName = "Resource" + File.separator
				+ "Application_data_credential";
		
		try {
			ConfigurationBuilder configBuilder = FileHelper
					.readConfigurationBuilderFromFile(fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		final String baseDirectory = "Resource";
//		String queryFilePath = baseDirectory + File.separator + "Queries" + File.separator + "Manchester_United";
//
//		FileHelper.writeQueryFileToSingleFolder(baseDirectory, queryFilePath, configBuilder.build());

		final long endTime = System.currentTimeMillis();
		double timeTaken = (endTime - startTime)/ 100;
		System.out.println("Total execution time: " + timeTaken );
	}



	public static void lolo(String wsdcs) throws IOException{
		final long startTime = System.currentTimeMillis();

		String baseDirectory = "Resource";
		String queryFileName = "Manchester_United";

		//Test creating all the different file and folder


		final long endTime = System.currentTimeMillis();
		double timeTaken = (endTime - startTime)/ 100;
		System.out.println("Total execution time: " + timeTaken );
	}
}
