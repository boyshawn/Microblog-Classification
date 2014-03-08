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

public class Tweets {
	private Vector<Tweet> tweets;

	public Tweets(String pathname) {
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

				String text = newTweet.getString("text");
				String geolocation = newTweet.getString("geoposition");

				JSONObject dateObject = newTweet.getJSONObject("created_at");
				String date = dateObject.getString("$date");

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

					tweet = new Tweet(text, date, geolocation, tweetPoster,
							mentionedUsers);
				} else {
					tweet = new Tweet(text, date, geolocation, tweetPoster);
				}

				tweets.add(tweet);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private User constructUser(JSONObject tweeter) throws JSONException {
		int id = tweeter.getInt("id");
		String screenName = tweeter.getString("screen_name");
		String location = tweeter.getString("location");
		String accountName = tweeter.getString("name");

		User user = new User(id, screenName, location, accountName);
		return user;
	}
}
