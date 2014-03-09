package model;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class SocialClassifier extends HashMap<String, Vector<Integer>>{
	private static HashMap<String, Vector<Integer>> socialVector = new HashMap<String, Vector<Integer>>();

	public SocialClassifier(Tweets tweets, String[] handpickKeyUser){
		super(socialVector);

		final int TRUE = 1;
		final int FALSE = 0;

		for(int j = 0; j < tweets.size(); j ++){
			Tweet tweet = tweets.get(j);
			Vector<Integer> tweetSocialVector = new Vector<Integer>();

			for(int i = 0; i<handpickKeyUser.length; i++){
				if(isRelated(tweet, handpickKeyUser[i])){
					tweetSocialVector.add(TRUE);
				}
				else{
					tweetSocialVector.add(FALSE);
				}
			}

			socialVector.put(tweet.tweeter().screenName(), tweetSocialVector);
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
	private boolean isRelated(Tweet tweet, String keyUser){
		boolean related = false;

		String screenName = tweet.tweeter().screenName();
		if(!(related = screenName.equals(keyUser))){
			List<User> mentionedUsers = tweet.mentionUser();

			for (int i = 0; i < mentionedUsers.size() && !related; i++) {
				User user = mentionedUsers.get(i);
				related = user.screenName().equals(keyUser);
			}
		}
		return related;
	}
}
