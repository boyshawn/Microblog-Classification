package model;

import java.text.SimpleDateFormat;
import java.util.List;

public class Tweet {
	// Attribute
	private String text;
	private String date;

	private String geolocation;
	private User tweeter;
	private long id;

	private boolean isRetweet;
	private List<User> mentionedUsers;

	// Constructor
	public Tweet(long id2, String text, String date, String geolocation, User tweeter) {
		this.id = id2;
		this.text = text;
		this.date = date;
		this.geolocation = geolocation;
		this.tweeter = tweeter;
	}

	public Tweet(long id2, String text, String date, String geolocation, User tweeter,
			List<User> mentionedUsers) {
		this.id = id2;
		this.text = text;
		this.date = date;
		this.geolocation = geolocation;
		this.tweeter = tweeter;
		this.mentionedUsers = mentionedUsers;
		this.isRetweet = setRetweet();
	}

	// Method
	public long id(){
		return id;
	}
	
	public String text() {
		return text;
	}

	public String date() {
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
