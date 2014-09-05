package net.rebmos.ilearn.entities;

import android.graphics.drawable.Drawable;

public class DictionaryWord {
	public String word;
	public String description;
	public Drawable drawable;
	
	public DictionaryWord( String word, String description, Drawable drawable ) {
		this.word = word;
		this.description = description;
		this.drawable = drawable;
	}
}
