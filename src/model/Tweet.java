package model;

import java.text.SimpleDateFormat;
import java.util.List;

public class Tweet {
	// Attribute
	private String text;
	private SimpleDateFormat date;

	private String geolocation;
	private User tweeter;

	private boolean isRetweet;
	private List<User> mentionedUsers;

	// Constructor
	public Tweet(String text, String date, String geolocation, User tweeter) {
		this.text = text;
		this.date = new SimpleDateFormat(date);
		this.geolocation = geolocation;
		this.tweeter = tweeter;
	}

	public Tweet(String text, String date, String geolocation, User tweeter,
			List<User> mentionedUsers) {
		this.text = text;
		this.date = new SimpleDateFormat(date);
		this.geolocation = geolocation;
		this.tweeter = tweeter;
		this.mentionedUsers = mentionedUsers;
		this.isRetweet = setRetweet();
	}

	// Method
	public String text() {
		return text;
	}

	public SimpleDateFormat date() {
		return date;
	}

	public String geolocation() {
		return geolocation;
	}

	public User tweeter() {
		return tweeter;
	}

	public boolean isRetweet() {
		return isRetweet;
	}

	public List<User> mentionUser() {
		return mentionedUsers;
	}

	private boolean setRetweet() {
		boolean isRetweet = false;

		if (this.text != null) {
			if (text.contains("RT @")) {
				isRetweet = true;
			}
		}
		return isRetweet;
	}
}
