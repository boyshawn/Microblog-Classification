package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Tweets extends Vector<Tweet>{

	Vector<Tweet> tweets;
	
	public Tweets(String pathname) {
		Vector<Tweet> tweets = new Vector<Tweet>();
		File file = new File(pathname);
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			System.out.println(pathname + "cannot be found.");
			e.printStackTrace();
		}

		String line;
		try {
			while ((line = reader.readLine()) != null) {
				JSONObject newTweet = new JSONObject(line);
				
				long id = newTweet.getLong("id");
				String text = newTweet.getString("text");
				String date = newTweet.getString("created_at");

				JSONObject newTweeter = newTweet.getJSONObject("user");
				User tweetPoster = constructUser(newTweeter);

				JSONObject entityObject = newTweet.getJSONObject("entities");
				JSONArray mentionArray = entityObject
						.getJSONArray("user_mentions");
				Tweet tweet;

				if (mentionArray.length() > 0) {
					List<User> mentionedUsers = new Vector<User>();

					for (int i = 0; i < mentionArray.length(); i++) {
						JSONObject mentionedUser = (JSONObject) mentionArray
								.get(i);
						User user = constructUser(mentionedUser);
						mentionedUsers.add(user);
					}
					//FIXME: Write in report: use poster location because there is no geolocation for this data set.
					tweet = new Tweet(id, text, date, tweetPoster.location(), tweetPoster,
							mentionedUsers);
				} else {
					tweet = new Tweet(id, text, date, tweetPoster.location(), tweetPoster);
				}
				
				tweets.add(tweet);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		super.elementData = new Tweet[tweets.size()];
		java.lang.System.arraycopy(tweets.toArray(), 0, super.elementData, 0, tweets.size());
		super.elementCount = tweets.size();
	}

	private User constructUser(JSONObject tweeter) throws JSONException {
		int id = tweeter.getInt("id");
		String screenName = tweeter.getString("screen_name");
		
		String location = "";
		if(tweeter.has("location")){
			location = tweeter.getString("location");
		}
		
		String accountName = "";
		
		if(tweeter.has("name")){
			accountName = tweeter.getString("name");
		}

		User user = new User(id, screenName, location, accountName);
		return user;
	}

}
