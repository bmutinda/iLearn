package net.rebmos.ilearn.entities;

import java.util.HashMap;

import net.rebmos.ilearn.utilities.AppLogger;
import net.rebmos.ilearn.utilities.Constants;
import net.rebmos.ilearn.utilities.ILearnApplication;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.parse.ParseObject;

public class Score {
	public DictionaryWord word;
	public boolean won;
	public String user = "";

	public Score(DictionaryWord word, boolean won) {
		this.word = word;
		this.won = won;
	}

	public Score(DictionaryWord word, boolean won, String user) {
		this.word = word;
		this.won = won;
		this.user = user;
	}

	@SuppressWarnings("unchecked")
	public void save() {
		if (TextUtils.isEmpty(this.user)) {
			this.user = ILearnApplication.database
					.getSettingInfo(Constants.USER_NAME_TAG);
		}

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(Constants.USER_NAME_TAG, this.user);
		map.put(Constants.WORD_TAG, this.word.word);
		map.put(Constants.WON_TAG, this.won);
		map.put(Constants.KEY_STATUS, 0);

		new SaveUser().execute(map);

	}

	class SaveUser extends AsyncTask<HashMap<String, Object>, String, String> {

		@Override
		protected String doInBackground(HashMap<String, Object>... params) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map = params[0];
			try {
				ILearnApplication.database.addScore(map);
			} catch (Exception e) {
			} finally {
				try {
					final ParseObject scoreObj = new ParseObject("Score");
					scoreObj.put(Constants.USER_NAME_TAG,
							map.get(Constants.USER_NAME_TAG));
					scoreObj.put(Constants.WORD_TAG,
							map.get(Constants.WORD_TAG));
					scoreObj.put(Constants.WON_TAG, map.get(Constants.WON_TAG));
					scoreObj.saveEventually();
				} catch (Exception e) {
					AppLogger.logError("Score add error --- parse::: "
							+ e.getLocalizedMessage());
				}

			}
			return null;
		}
	}

}