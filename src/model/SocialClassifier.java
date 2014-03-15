package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class SocialClassifier extends HashMap<String, Vector<Integer>>{
	private Set<String> positiveUsers;
	private Set<String> negativeUsers;
	
	public SocialClassifier(Map<String, Tweets> allTweets){
		Tweets tweets = allTweets.get("negative");
		negativeUsers = new TreeSet<String>();
		for(int i =0; i < tweets.size(); i++){
			Tweet tweet = tweets.get(i);
			negativeUsers.add(tweet.tweeter().screenName());
		}
		
		tweets = allTweets.get("positive");
		positiveUsers = new TreeSet<String>();
		for(int i =0; i < tweets.size(); i++){
			Tweet tweet = tweets.get(i);
			positiveUsers.add(tweet.tweeter().screenName());
		}
	}
	
	public void buildSocialVectorFile(Tweets tweets, String outputFileName){
		List<Integer> vectorList = new ArrayList<Integer>();
		
		for (int i = 0; i < tweets.size(); i++) {
			Tweet tweet = tweets.get(i);
			vectorList.addAll(resultListOfATweet(tweet, negativeUsers));
		}
	}

	/**
	 * <p>
	 * Check if a tweet is related to the key user. A related tweet is defined
	 * to be one of the following: <br/>
	 * <ul>
	 * <li>a tweet sent by the key user himself</li>
	 * <li>The poster of the tweet retweet the tweet sent by key user</li>
	 * <li>The tweet mention the key user</li>
	 * </ul>
	 * </p>
	 * 
	 * @param tweet
	 *            the specific tweet to be checked upon
	 * @param keyUser
	 *            the key user that is to be checked against
	 * @return <b>true</b> if the tweet is related to <i>keyUser</i>,
	 *         <b>false</b> if otherwise.
	 */
	private List<Integer> resultListOfATweet(Tweet tweet, Set usersScreenNames){
		List<Integer> result = new ArrayList<Integer>();

		final int TRUE = 1;
		final int FALSE = 0;
		
		//Build a vector of all the user inside a tweet
		List<String> tweetUsers = allUsersOfATweet(tweet);
		
		Iterator<String> setNameIter = usersScreenNames.iterator();
		
		while(setNameIter.hasNext()){
			String screenName = setNameIter.next();
			if(tweetUsers.contains(screenName)){
				result.add(TRUE);
			}else{
				result.add(FALSE);
			}
		}
		return result;	//Stub
	}
	
	private List<String> allUsersOfATweet(Tweet tweet){
		List<String> users = new ArrayList<String>();

		//Poster
		users.add(tweet.tweeter().screenName());
		
		//Mentioned user
		List<User> mentionedUsers = tweet.mentionUser();
		
		if(mentionedUsers != null){
			for (int i = 0; i < mentionedUsers.size(); i++) {
				users.add(mentionedUsers.get(i).screenName());
			}
		}
		
		return users;
	}
}
