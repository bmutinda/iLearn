package net.rebmos.ilearn.utilities;

import net.rebmos.ilearn.R;
import net.rebmos.ilearn.entities.Dictionary;
import net.rebmos.ilearn.entities.DictionaryWord;
import android.app.Application;
import android.content.Context;

public class ILearnApplication extends Application {

	public static Context context;
	public static Dictionary dictionary;

	@Override
	public void onCreate() {
		ILearnApplication.context = getApplicationContext();
		ILearnApplication.dictionary = new Dictionary();

		/**
		 * Add in some words to our dictionary
		 */
		DictionaryWord zebra = new DictionaryWord("Zebra", "Zebra Description",
				getResources().getDrawable(R.drawable.btn_answer));
		ILearnApplication.dictionary.addWord(zebra);

		DictionaryWord elephant = new DictionaryWord("Elephant",
				"Elephant Description", getResources().getDrawable(
						R.drawable.btn_answer));
		ILearnApplication.dictionary.addWord(elephant);

		DictionaryWord lion = new DictionaryWord("Lion", "Lion Description",
				getResources().getDrawable(R.drawable.btn_answer));
		ILearnApplication.dictionary.addWord(lion);

		DictionaryWord cow = new DictionaryWord("Cow", "Cow Description",
				getResources().getDrawable(R.drawable.btn_answer));
		ILearnApplication.dictionary.addWord(cow);

		DictionaryWord ram = new DictionaryWord("Ram", "Ram Description",
				getResources().getDrawable(R.drawable.btn_answer));
		ILearnApplication.dictionary.addWord( ram);

	}
}
