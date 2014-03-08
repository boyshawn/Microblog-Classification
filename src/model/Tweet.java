package model;

import java.text.SimpleDateFormat;

public class Tweet {
	//Attribute
	private String text;
	private SimpleDateFormat date;

	private String geolocation;
	private User tweeter;
	
	private boolean isRetweet;
	private int retweetCount;
	private User retweetFrom;
	
	private boolean isReplyTweet;
	private User replyTo;
	
	//Constructor
	public Tweet(String text, String date, String geolocation,
			boolean isRetweet, int retweetCount, boolean isReplyTweet) {
		this.text = text;
		this.date = new SimpleDateFormat(date);
		this.geolocation = geolocation;
		this.isRetweet = isReplyTweet;
		this.retweetCount = retweetCount;
		this.isReplyTweet = isReplyTweet;
	}
	
	public Tweet(String text, User tweeter, String date, String geolocation,
			boolean isRetweet, User retweetFrom, int retweetCount,
			boolean isReplyTweet, User replyTo) {
		this.text = text;
		this.date = new SimpleDateFormat(date);
		this.geolocation = geolocation;
		this.isRetweet = isReplyTweet;
		this.retweetCount = retweetCount;
		this.isReplyTweet = isReplyTweet;
		this.tweeter = tweeter;
		this.retweetFrom = retweetFrom;
		this.replyTo = replyTo;
	}
	
	public Tweet(String text, User tweeter, String date, String geolocation,
			boolean isRetweet, User retweetFrom, int retweetCount) {
		this.text = text;
		this.date = new SimpleDateFormat(date);
		this.geolocation = geolocation;
		this.isRetweet = isReplyTweet;
		this.retweetCount = retweetCount;
		this.tweeter = tweeter;
		this.retweetFrom = retweetFrom;
	}
	
	public Tweet(String text, User tweeter, String date, String geolocation,
			boolean isReplyTweet, User replyTo) {
		this.text = text;
		this.date = new SimpleDateFormat(date);
		this.geolocation = geolocation;
		this.isReplyTweet = isReplyTweet;
		this.tweeter = tweeter;
		this.replyTo = replyTo;
	}
	
	//Method
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
	public int retweetCount() {
		return retweetCount;
	}
	public User retweetFrom() {
		return retweetFrom;
	}
	public boolean isReplyTweet() {
		return isReplyTweet;
	}
	public User replyTo() {
		return replyTo;
	}
	
}
