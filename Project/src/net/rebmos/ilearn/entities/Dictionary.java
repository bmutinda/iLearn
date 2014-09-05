package net.rebmos.ilearn.entities;

import java.util.ArrayList;

public class Dictionary {

	ArrayList<DictionaryWord> words;

	public Dictionary() {
		words = new ArrayList<DictionaryWord>();
	}

	public boolean addWord(DictionaryWord word) {
		return this.words.add(word);
	}

	public boolean removeWord(DictionaryWord word) {
		return this.words.remove(word);
	}

	public ArrayList<DictionaryWord> getWords(){
		return words;
	}
}
