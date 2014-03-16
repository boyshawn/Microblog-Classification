package training;

import java.io.File;
import java.util.Map;

import model.SocialClassifier;
import model.SocialSentiment;
import model.Tweet;
import model.Tweets;

import org.apache.commons.collections15.map.HashedMap;

public class TemporalDriver {

	public static void main(String[] args) {
		String baseFilePath = "C:\\Users\\Francis Pang\\Google Drive\\Acadmic folder\\CS4242 Social Media\\Assignment 2 classified Tweet JSON\\";
		
//		//Social Sentiment
//		SocialSentiment socialSentiment = new SocialSentiment(baseFilePath);
//		socialSentiment.writeSocialVectorFile();
		
		Tweets tweets = new Tweets(baseFilePath + "neg_apple.txt");
		System.out.println(tweets.get(0).date());
	}
}
