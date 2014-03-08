package model;

public class User implements Comparable<User>{
	//User Particular
	private String id;
	private String screenName;
	private String location;
	private String accountName;
	
	public User(String id, String screenName, String location, String accountName){
		this.id = id;
		this.screenName = screenName;
		this.location = location;
		this.accountName = accountName;
	}
	
	public String accountName() {
		return accountName;
	}

	public String id(){
		return this.id;
	}
	
	public String screenName(){
		return this.screenName;
	}
	
	public String location(){
		return this.location;
	}

	@Override
	public int compareTo(User o) {
		return this.accountName.compareTo(o.accountName);
	}
}
