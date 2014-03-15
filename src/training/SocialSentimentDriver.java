package training;

import java.io.File;
import java.util.Map;

import model.SocialClassifier;
import model.Tweets;

import org.apache.commons.collections15.map.HashedMap;

public class SocialSentimentDriver {

	public static void main(String[] args) {
		String baseFilePath = "C:\\Users\\Francis Pang\\Google Drive\\Acadmic folder\\CS4242 Social Media\\Assignment 2 classified Tweet JSON\\";
		String baseWritePath = "C:\\Users\\Francis Pang\\Google Drive\\Acadmic folder\\CS4242 Social Media\\Assignment 2 Social Vector\\";
		
		/***********   Building Social Classifier *******************/
		//Apple
		SocialClassifier appleSocialClassifier = createSocialClassifier(baseFilePath + "pos_apple.txt", baseFilePath + "neg_apple.txt");
		//Google
		SocialClassifier googleSocialClassifier = createSocialClassifier(baseFilePath + "pos_google.txt", baseFilePath + "neg_google.txt");
		//Microsoft
		SocialClassifier microsoftSocialClassifier = createSocialClassifier(baseFilePath + "pos_microsoft.txt", baseFilePath + "neg_microsoft.txt");
		//Twitter
		SocialClassifier twitterSocialClassifier = createSocialClassifier(baseFilePath + "pos_twitter.txt", baseFilePath + "neg_twitter.txt");
		
		/************    Writing vector files *********************/
		File baseDir = new File(baseFilePath);
		File[] fileContent = baseDir.listFiles();
		
		for (int i = 1; i < fileContent.length; i++) {
			Tweets tweets = new Tweets(fileContent[i].getAbsolutePath());
			
			String[] split = fileContent[i].getName().split("[_.]");
			
			if("apple".equals(split[1])){
				appleSocialClassifier.buildSocialVectorFile(tweets, baseWritePath + fileContent[i].getName());
			}
			else if("google".equals(split[1])){
				googleSocialClassifier.buildSocialVectorFile(tweets, baseWritePath + fileContent[i].getName());
			}
			else if("microsoft".equals(split[1])){
				microsoftSocialClassifier.buildSocialVectorFile(tweets, baseWritePath + fileContent[i].getName());
			}
			else if("twitter".equals(split[1])){
				twitterSocialClassifier.buildSocialVectorFile(tweets, baseWritePath + fileContent[i].getName());
			}
		}
		
		//socialClassifier.buildSocialVectorFile(tweets, "C:\\Users\\Francis Pang\\Google Drive\\Acadmic folder\\CS4242 Social Media\\Assignment 2 Social Vector\\pos_apple(social).txt");
	}

	private static SocialClassifier createSocialClassifier(String positiveFileName, String negativeFileName){
		Map<String, Tweets> allTweets = new HashedMap<String, Tweets>();

		Tweets tweets = new Tweets(negativeFileName);
		allTweets.put("negative", tweets);

		tweets = new Tweets(positiveFileName);
		allTweets.put("positive", tweets);

		return new SocialClassifier(allTweets);
	}

}
