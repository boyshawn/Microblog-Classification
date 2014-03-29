package assignment3;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayAround {

	public static void main(String[] args) {		
		//Create the configuration file
		ConfigurationBuilder configBuilder = new ConfigurationBuilder();
		configBuilder.setOAuthConsumerKey("nZlvDk5VT8eIvtDBV2kCtCiim");
		configBuilder.setOAuthConsumerSecret("z1KMbcOrTlSd4HrrYAaa0ZVefvzdb10nv50ZrEdZ91uNtW5eoX");
		configBuilder.setOAuthAccessToken("117599242-zOYzJqyg3S1twciaaMk5SsXKFGUnHxdSYYfwpZgC");
		configBuilder.setOAuthAccessTokenSecret("c8dsE4b4ID1LVQpEfCTpfp8r2OFwZgrVOSuNdHrt79Fcf");
		
		File queryFile = new File("src/assignment3/keywords");
		
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
				queries.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e){
			e.printStackTrace();
		}
		
		String[] queriesArray = queries.toArray(new String[queries.size()]);
		
		TweetSearch.search(queriesArray, configBuilder.build());
    }
}
