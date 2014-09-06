package net.rebmos.ilearn.entities;

import java.util.ArrayList;

public class Scores {
	
	public ArrayList<Score> scores;
	
	public Scores() {
		this.scores = new ArrayList<Score>();
	}
	
	public void addScore( Score score ){
		this.scores.add(score);
	}
	public void clearScores( ){
		this.scores.clear();
	}
	
	public int countPassed( ){
		int passed = 0;
		for( Score sc: this.scores ){
			passed+= sc.won ? 1 : 0;
		}
		return passed;
	}
	
	public int countFailed( ){
		int failed = 0;
		for( Score sc: this.scores ){
			failed+= (!sc.won) ? 1 : 0;
		}
		return failed;
	}

}
