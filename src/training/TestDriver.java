package training;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import model.Tweet;
import model.Tweets;

public class TestDriver {

	public static void main(String[] args){
		final String fileName = "C:\\Users\\Francis Pang\\git\\Microblog-Classification\\TRAIN\\tweets_train.txt";
		Tweets tweets = new Tweets(fileName);
		
		if(tweets.size() < 1){
			System.out.println("Nothing is read.");
		}

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
