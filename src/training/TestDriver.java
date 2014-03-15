package training;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import model.SocialClassifier;
import model.Tweet;
import model.Tweets;

public class TestDriver {

	public static void main(String[] args){
		final String fileName = "C:\\Users\\Francis Pang\\Google Drive\\Acadmic folder\\CS4242 Social Media\\Assignment 2 classified Tweet JSON\\pos_twitter.txt";
		Tweets tweets = new Tweets(fileName);
		String[] twitterKeyUser = { "twitter", "Support", "twitterapi",
				"twittermobile", "twittersearch", "design", "twittermedia",
				"TwitterStories", "TwitterEng", "TwitterBooks",
				"Nonprofits", "TwitterIndia", "TwitterForNews",
				"TwitterMusic", "TwitterAds", "international", "TwitterUK",
				"TwitterSmallBiz", "gov" };
		
		SocialClassifier socialClassifier= new SocialClassifier(tweets, twitterKeyUser); 
		
		if(socialClassifier == null){
			System.out.println("Social class is empty.");
		}
		
		System.out.println(socialClassifier.toString());
		
//		BufferedWriter writer = null;
//		//File
//		try {
//			writer = new BufferedWriter(
//					new FileWriter(
//							new File(
//									"C:\\Users\\Francis Pang\\git\\Microblog-Classification\\TRAIN\\train-text")));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		try {
//			for (int i = 0; i < tweets.size(); i++){
//				Tweet tweet = tweets.get(i);
//
//				writer.write(tweet.id() + ",");
//				writer.write(tweet.text() + ",");
//				writer.write(tweet.text() + ",");
//				writer.write(tweet.date());
//				writer.newLine();
//			} 
//			writer.close();
//		}
//		catch (IOException e) {
//			e.printStackTrace();
//		}
	}
}
