package model;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class SocialSentiment extends HashMap<String, Integer>{
	private static HashMap<String, Integer> socialSentiment = new HashMap<String, Integer>();

	public SocialSentiment(Tweets tweets, String[] handpickKeyUser){
		super(socialSentiment);

		final int TRUE = 1;
		final int FALSE = 0;

		for(int j = 0; j < tweets.size(); j ++){
			Tweet tweet = tweets.get(j);
			Vector<Integer> tweetSocialVector = new Vector<Integer>();

			for(int i = 0; i<handpickKeyUser.length; i++){
				if(isRelated(tweet, handpickKeyUser)){
					socialSentiment.put(tweet.tweeter().screenName(), TRUE);
				}
				else{
					socialSentiment.put(tweet.tweeter().screenName(), FALSE);
				}
			}
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
	private boolean isRelated(Tweet tweet, String[] keyUser){
		boolean related = false;

		String screenName = tweet.tweeter().screenName();
		for (int i = 0; i < keyUser.length; i++) {
			if(!(related = screenName.equals(keyUser[i]))){
				List<User> mentionedUsers = tweet.mentionUser();

				for (int j = 0; i < mentionedUsers.size() && !related; j++) {
					User user = mentionedUsers.get(j);
					related = user.screenName().equals(keyUser[i]);
				}
			}
		}
		return related;
	}
}
