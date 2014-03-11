package model;

public class User implements Comparable<User> {
	// User Particular
	private int id;
	private String screenName;
	private String location;
	private String accountName;

	public User(int id, String screenName, String location, String accountName) {
		this.id = id;
		this.screenName = screenName;
		this.location = location;
		this.accountName = accountName;
	}

	public String accountName() {
		return accountName;
	}

	public int id() {
		return this.id;
	}

	public String screenName() {
		return this.screenName;
	}

	public String location() {
		return this.location;
	}

	@Override
	public int compareTo(User o) {
		return (this.id - o.id);
	}

	public boolean hasLocation() {
		if (this.location == null) {
			return false;
		} else {
			return true;
		}
	}
}
