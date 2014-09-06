package net.rebmos.ilearn;

import java.util.ArrayList;

import net.rebmos.ilearn.callbacks.TimerCallback;
import net.rebmos.ilearn.entities.DictionaryWord;
import net.rebmos.ilearn.entities.ui.ImageViewOpacity;
import net.rebmos.ilearn.entities.ui.ToastAlert;
import net.rebmos.ilearn.utilities.AppLogger;
import net.rebmos.ilearn.utilities.SoundPlayer;
import net.rebmos.ilearn.utilities.Constants;
import net.rebmos.ilearn.utilities.ILearnApplication;
import net.rebmos.ilearn.utilities.TickTimer;
import net.rebmos.ilearn.utilities.Utils;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
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
	ImageView imgAnimal;
	
	ArrayList<ImageView> answerImages;

	// Holds all our dictionary words for the game
	ArrayList<DictionaryWord> dictionaryWords;

	// Holds the active word the player is answering
	DictionaryWord currentWord;

	// Keeeps the current visible question
	int currentQuestionNo = 1;

	// Plays sound from the application assets directory
	SoundPlayer soundPlayer;
	
	// keeps game state 
	boolean GAME_OVER = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		Utils.setFullScreen(activity);
		setContentView(R.layout.layout_game);

		utils = new Utils(activity);

		soundPlayer = new SoundPlayer(activity);
		answerImages = new ArrayList<ImageView>();
		dictionaryWords = new ArrayList<DictionaryWord>();

		txtTimer = (TextView) findViewById(R.id.txtTimer);
		imgAnimal = (ImageView) findViewById(R.id.animal );
		
		txtTimer.setText("0");

		// Add all our answer images to our main holder
		int[] ids = { R.id.answer1, R.id.answer2, R.id.answer3, R.id.answer4,
				R.id.answer5 };
		for (int i = 0; i < ids.length; i++) {
			answerImages.add((ImageView) findViewById(ids[i]));
		}

		restartGame();

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
		
		if( ! this.GAME_OVER ){
			final ImageView clickedImg = (ImageView) view;
			new ImageViewOpacity(clickedImg).apply(false);
			
			final boolean won = wonQuestion(view.getTag() + "");
			
			if( won ){
				this.playCorrectSound();
				this.goToNextQuestion();
			}else{
				AppLogger.logError( "Oops! failed..." );
				this.playIncorrectSound();
			}
			
			AppLogger.logError(view.getTag() + "::: won = "+won );
		}else{
			this.showSessionOver();
		}
	}

	/**
	 * Updates timer label to show exactly how many seconds are remaining to
	 * answer the question
	 * 
	 * @param elapsed
	 */
	public void updateTimerUI(int elapsed) {
		int countDown = (Constants.TIME_TO_ANSWER - elapsed) / 1000;
		txtTimer.setText(countDown + "");
	}

	/**
	 * Resets timer label after counter is complete
	 */
	public void resetTimerUI() {
		txtTimer.setText("10");
	}

	/**
	 * Creates a new game session
	 */
	public void restartGame() {

		clearGameSession();

		// Randomize the words we have in our dictionary so that we don't
		// have all the answers always positioned in the same place
		dictionaryWords = utils.generateRandomData(
				ILearnApplication.dictionary.getWords(),
				ILearnApplication.dictionary.getWords().size());

		// set answers imageview tags to be the question answer
		drawAllAnswerImages();

		for (DictionaryWord word : dictionaryWords) {
			AppLogger.logError(word.word + " ->" + word.description);
		}

		// Now continue to load the first question ready to play 
		loadCurrentQuestion();
	}

	public void clearGameSession() {
		this.currentQuestionNo = 1;
		this.currentWord = null;
		this.GAME_OVER = false;
	}

	public void drawAllAnswerImages() {
		// set answers imageview tags to be the question answer
		// one of them holds the correct answer 
		
		// To make sure nothing falls in the same place always
		// we also shuffle the answer images to get different positions 
		AppLogger.logError( "Before " +answerImages.toString() );
		ArrayList<ImageView> randomAnswerImages = new ArrayList<ImageView>( answerImages );
		randomAnswerImages = utils.generateRandomData(randomAnswerImages, randomAnswerImages.size() );
		AppLogger.logError( "After " +randomAnswerImages.toString() );
		try {
			for (int i = 0; i < randomAnswerImages.size(); i++) {
				randomAnswerImages.get(i).setTag(dictionaryWords.get(i).word);
				randomAnswerImages.get(i).setImageDrawable(
						dictionaryWords.get(i).drawable);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void loadCurrentQuestion() {
		currentWord = this.getCurrentQuestion();
		if( !TextUtils.isEmpty( currentWord.word )){
			imgAnimal.setImageDrawable( currentWord.animal );
			
			// shuffle answer images also 
			this.drawAllAnswerImages( );
		}else{
			AppLogger.logError( "No Current Question" );
			this.showSessionOver();
		}
	}

	public void goToNextQuestion() {
		if (!finishedSession()) {
			this.currentQuestionNo++;
			this.loadCurrentQuestion();
		} else {
			this.GAME_OVER = true;
			this.showSessionOver();
		}
	}

	public void repeatQuestion() {

	}
	
	public DictionaryWord getCurrentQuestion() {
		DictionaryWord currentQuestion = new DictionaryWord();
		try {
			currentQuestion = dictionaryWords.get(currentQuestionNo - 1);
		} catch (Exception e) {
			AppLogger
					.logError("Exception thrown while getting fact data..#source="
							+ e.getMessage());
		}
		return currentQuestion;
	}
	
	/**
	 * Determines whether the player has answered the current question correct 
	 * or not 
	 * 
	 * @return
	 */
	public boolean wonQuestion( String chosenAnswer ){
		return currentWord.word.equalsIgnoreCase( chosenAnswer) ? true: false;
	}

	public boolean finishedSession() {
		return currentQuestionNo < dictionaryWords.size() ? false : true;
	}

	public void playIncorrectSound() {
		soundPlayer.play("incorrect.mp3");
	}
	
	public void playCorrectSound() {
		soundPlayer.play("correct.mp3");
	}

	public void showSessionOver() {
		new ToastAlert(activity, "Game is Over...total score")
				.show(Gravity.CENTER);
	}

}