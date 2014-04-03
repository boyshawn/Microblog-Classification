package assignment3;

import java.text.SimpleDateFormat;

import org.joda.time.LocalDate;


public class SoccerMatch {
	//Attributes
	//Date      Time    Home Team           Away Team   Full Time (Half Time) 
	private LocalDate date;
	private String time;
	private String homeTeam;
	private String awayTeam;
	private String halfTimeScore;
	private int homeTeamScore;
	private int awayTeamScore;
	
	//Constructor
	public SoccerMatch(LocalDate date, String time, String homeTeam,
			String awayTeam, int homeTeamScore, int awayTeamScore,
			String halfTimeScore) {
		
		this.date = date;
		this.time = time;
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.homeTeamScore = homeTeamScore;
		this.awayTeamScore = awayTeamScore;
		this.halfTimeScore = halfTimeScore;
	}

	public LocalDate getDate() {
		return date;
	}
	
	public String getDateString(){
		return date.toString();
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getHomeTeam() {
		return homeTeam;
	}

	public void setHomeTeam(String homeTeam) {
		this.homeTeam = homeTeam;
	}

	public String getAwayTeam() {
		return awayTeam;
	}

	public void setAwayTeam(String awayTeam) {
		this.awayTeam = awayTeam;
	}

	public String getHalfTimeScore() {
		return halfTimeScore;
	}

	public void setHalfTimeScore(String halfTimeScore) {
		this.halfTimeScore = halfTimeScore;
	}

	public int getHomeTeamScore() {
		return homeTeamScore;
	}

	public void setHomeTeamScore(int homeTeamScore) {
		this.homeTeamScore = homeTeamScore;
	}

	public int getAwayTeamScore() {
		return awayTeamScore;
	}

	public void setAwayTeamScore(int awayTeamScore) {
		this.awayTeamScore = awayTeamScore;
	}
	
	public String getWinner(){
		if(this.homeTeamScore > this.awayTeamScore){	//Home Team win
			return homeTeam;
		}
		else if(this.awayTeamScore > this.homeTeamScore){	//Away Team win
			return awayTeam;
		}
		else{	//Draw
			return "";
		}
	}
	
	@Override
	public String toString(){
		return date.toString() + " " + time + " " + homeTeam + " " + awayTeam + " "
				+ homeTeamScore + ":" + awayTeamScore + " " + halfTimeScore;
	}
	
}
