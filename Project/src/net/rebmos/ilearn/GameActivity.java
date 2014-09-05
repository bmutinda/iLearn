package net.rebmos.ilearn;

import java.util.ArrayList;

import net.rebmos.ilearn.callbacks.TimerCallback;
import net.rebmos.ilearn.entities.DictionaryWord;
import net.rebmos.ilearn.entities.ui.ImageViewOpacity;
import net.rebmos.ilearn.utilities.AppLogger;
import net.rebmos.ilearn.utilities.AssetsSoundPlayer;
import net.rebmos.ilearn.utilities.Constants;
import net.rebmos.ilearn.utilities.ILearnApplication;
import net.rebmos.ilearn.utilities.TickTimer;
import net.rebmos.ilearn.utilities.Utils;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author Mutinda Boniface
 * 
 */
public class GameActivity extends Activity {
	Activity activity;
	Utils utils;

	TextView txtTimer;
	ArrayList<ImageView> answerImages;
	ArrayList<DictionaryWord> dictionaryWords;

	AssetsSoundPlayer soundPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		Utils.setFullScreen(activity);
		setContentView(R.layout.layout_game);

		utils = new Utils(activity);

		soundPlayer = new AssetsSoundPlayer(activity);
		answerImages = new ArrayList<ImageView>();
		dictionaryWords = new ArrayList<DictionaryWord>();

		txtTimer = (TextView) findViewById(R.id.txtTimer);
		txtTimer.setText("0");

		// Add all our answer images to our main holder
		int[] ids = { R.id.answer1, R.id.answer2, R.id.answer3, R.id.answer4,
				R.id.answer5 };
		for (int i =0; i<ids.length ; i++ ) {
			answerImages.add((ImageView) findViewById(ids[i]));
		}
		
		// Randomize the words we have in our dictionary so that we don't 
		// have all the answers always positioned in the same place 
		dictionaryWords = utils.generateRandomData(
				ILearnApplication.dictionary.getWords(),
				ILearnApplication.dictionary.getWords().size());
		
		// set answers imageview tags to be the question answer 
		// one of them holds the correct answer 
		try{
			for (int i =0; i<ids.length ; i++ ) {
				answerImages.get( i ).setTag( dictionaryWords.get( i ).word );
				answerImages.get( i ).setImageDrawable( dictionaryWords.get( i ).drawable );
			}
		}catch( Exception e ){
			e.printStackTrace();
		}
		
		for (DictionaryWord word : dictionaryWords ) {
			AppLogger.logError(word.word + " ->" + word.description);
		}

		new TickTimer(Constants.TIME_TO_ANSWER, Constants.TIMER_TICK)
				.start(new TimerCallback() {

					@Override
					public void onUpdate(final int elapsed) {
						activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								updateTimerUI(elapsed);
							}
						});
					}

					@Override
					public void onComplete() {
						activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								resetTimerUI();
							}
						});
					}
				});
	}

	/**
	 * Triggered on answer clicked/touched
	 * 
	 * @param view
	 */
	public void onAnswer(View view) {
		final ImageView clickedImg = (ImageView) view;
		new ImageViewOpacity(clickedImg).apply(false);
		
		AppLogger.logError( view.getTag()+"" );
	}

	public void updateTimerUI(int elapsed) {
		int countDown = (Constants.TIME_TO_ANSWER - elapsed) / 1000;
		txtTimer.setText(countDown + "");
	}

	public void resetTimerUI() {
		txtTimer.setText("10");
	}

	public void playIncorrectSound() {
		soundPlayer.play("incorrect.mp3");
	}

}