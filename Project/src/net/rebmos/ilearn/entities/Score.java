package net.rebmos.ilearn.entities;

public class Score {
	public DictionaryWord word;
	public boolean won;
	
	public Score( DictionaryWord word, boolean won ) {
		this.word = word;
		this.won = won;
	}
	
	public void save( ){
		
	}

}
