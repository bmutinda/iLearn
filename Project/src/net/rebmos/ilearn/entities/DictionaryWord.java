package net.rebmos.ilearn.entities;

import android.graphics.drawable.Drawable;

public class DictionaryWord {
	public String word;
	public String description;
	public Drawable drawable;
	public Drawable animal;
	
	public DictionaryWord( ){
		
	}
	
	public DictionaryWord( String word, String description, Drawable drawable, Drawable animal ) {
		this.word = word;
		this.description = description;
		this.drawable = drawable;
		this.animal = animal;
	}
}
