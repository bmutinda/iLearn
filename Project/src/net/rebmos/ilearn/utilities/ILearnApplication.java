package net.rebmos.ilearn.utilities;

import net.rebmos.ilearn.R;
import net.rebmos.ilearn.entities.Dictionary;
import net.rebmos.ilearn.entities.DictionaryWord;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

public class ILearnApplication extends Application {

	public static Context context;
	public static Dictionary dictionary;

	@Override
	public void onCreate() {
		ILearnApplication.context = getApplicationContext();
		ILearnApplication.dictionary = new Dictionary();
		
		Resources resources = getResources();
		/**
		 * Add in some words to our dictionary
		 */
		DictionaryWord zebra = new DictionaryWord("Zebra", "Zebra Description",
				resources.getDrawable(R.drawable.btn_zebra) , resources.getDrawable( R.drawable.zebra ));
		ILearnApplication.dictionary.addWord(zebra);

		DictionaryWord dog = new DictionaryWord("Dog",
				"Dog Description", resources.getDrawable(
						R.drawable.btn_dog), resources.getDrawable( R.drawable.dog ));
		ILearnApplication.dictionary.addWord( dog );

		DictionaryWord lion = new DictionaryWord("Lion", "Lion Description",
				resources.getDrawable(R.drawable.btn_lion) , resources.getDrawable( R.drawable.lion ));
		ILearnApplication.dictionary.addWord(lion);

		DictionaryWord cow = new DictionaryWord("Cow", "Cow Description",
				resources.getDrawable(R.drawable.btn_cow), resources.getDrawable( R.drawable.cow ));
		ILearnApplication.dictionary.addWord(cow);

		DictionaryWord ram = new DictionaryWord("Ram", "Ram Description",
				resources.getDrawable(R.drawable.btn_ram), resources.getDrawable( R.drawable.ram ));
		ILearnApplication.dictionary.addWord( ram);

	}
}
