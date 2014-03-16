package model;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.collections15.map.HashedMap;

public class SocialSentiment extends HashMap<String, Integer>{
	private SocialClassifier socialClassifier;

	public void writeSocialVectorFile(){
		String baseFilePath = "C:\\Users\\Francis Pang\\Google Drive\\Acadmic folder\\CS4242 Social Media\\Assignment 2 classified Tweet JSON\\";
		String baseWritePath = "C:\\Users\\Francis Pang\\Google Drive\\Acadmic folder\\CS4242 Social Media\\Assignment 2 Social Vector\\";

		/************    Writing vector files *********************/
		File baseDir = new File(baseFilePath);
		File[] fileContent = baseDir.listFiles();

		for (int i = 1; i < fileContent.length; i++) {
			Tweets tweets = new Tweets(fileContent[i].getAbsolutePath());
			socialClassifier.buildSocialVectorFile(tweets, baseWritePath + fileContent[i].getName()); 
		}
	}
	
	public SocialSentiment(String baseDirectory){
		super();
		
		File baseDir = new File(baseDirectory);
		File[] fileContent = baseDir.listFiles();
		
		Tweets positiveTweets = new Tweets();
		Tweets negativeTweets = new Tweets();
		
		for (int i = 1; i < fileContent.length; i++) {
			Tweets tweets = new Tweets(fileContent[i].getAbsolutePath()); 
					
			if(fileContent[i].getName().contains("neg")){
				negativeTweets.addAll(tweets);
			}
			else if (fileContent[i].getName().contains("pos")){
				positiveTweets.addAll(tweets);
			}
		}
		
		Map<String, Tweets> map = new HashedMap<String, Tweets>();
		map.put("positive", positiveTweets);
		map.put("negative", negativeTweets);
		
		this.socialClassifier = new SocialClassifier(map);
	}
}
