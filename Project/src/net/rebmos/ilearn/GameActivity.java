package net.rebmos.ilearn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import net.rebmos.ilearn.callbacks.TimerCallback;
import net.rebmos.ilearn.entities.DictionaryWord;
import net.rebmos.ilearn.entities.Score;
import net.rebmos.ilearn.entities.Scores;
import net.rebmos.ilearn.entities.ui.ImageViewOpacity;
import net.rebmos.ilearn.utilities.AppLogger;
import net.rebmos.ilearn.utilities.Constants;
import net.rebmos.ilearn.utilities.ILearnApplication;
import net.rebmos.ilearn.utilities.SoundPlayer;
import net.rebmos.ilearn.utilities.TickTimer;
import net.rebmos.ilearn.utilities.Utils;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * @author Mutinda Boniface
 * 
 */
public class GameActivity extends Activity {
	Activity activity;
	Utils utils;
	Scores scores;

	TextView txtTimer, txtAnimalDescripstion, txtScore, txtScoreLabel;
	ImageView imgAnimal, btnStart, btnNext, btnExit, gameOver, correct,
			inCorrect;

	ArrayList<ImageView> answerImages;

	// Holds all our dictionary words for the game
	ArrayList<DictionaryWord> dictionaryWords;
	
	// Holds answered questions and status -won or failed
	// format - [ WORD->true/false ] , [ .... ]
	ArrayList<HashMap<DictionaryWord, Boolean>> answeredQuestions;

	// Holds the active word the player is answering
	DictionaryWord currentWord;

	// Keeeps the current visible question
	int currentQuestionNo = 1;

	// Plays sound from the application assets directory
	SoundPlayer soundPlayer;

	// keeps game state
	boolean GAME_OVER = false;
	boolean CAN_PLAY = false;

	// Popup for game over, animal short description, navigation buttons
	RelativeLayout popup;

	// Holds the current question timer
	TickTimer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		// Make full screen
		Utils.setFullScreen(activity);
		// keep screen awake always
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.layout_game);

		utils = new Utils(activity);
		scores = new Scores();

		soundPlayer = new SoundPlayer(activity);
		answerImages = new ArrayList<ImageView>();
		dictionaryWords = new ArrayList<DictionaryWord>();
		answeredQuestions = new ArrayList<HashMap<DictionaryWord,Boolean>>();

		txtTimer = (TextView) findViewById(R.id.txtTimer);
		txtAnimalDescripstion = (TextView) findViewById(R.id.txtAnimalDescripstion);
		txtScore = (TextView) findViewById(R.id.txtScore);
		txtScoreLabel = (TextView) findViewById(R.id.txtScoreLabel);
		imgAnimal = (ImageView) findViewById(R.id.animal);
		gameOver = (ImageView) findViewById(R.id.gameOver);
		btnStart = (ImageView) findViewById(R.id.btnStart);
		btnNext = (ImageView) findViewById(R.id.btnNext);
		btnExit = (ImageView) findViewById(R.id.btnExit);
		correct = (ImageView) findViewById(R.id.correct);
		inCorrect = (ImageView) findViewById(R.id.incorrect);
		popup = (RelativeLayout) findViewById(R.id.popup);

		txtTimer.setText("0");

		// Add all our answer images to our main holder
		int[] ids = { R.id.answer1, R.id.answer2, R.id.answer3, R.id.answer4,
				R.id.answer5 };
		for (int i = 0; i < ids.length; i++) {
			answerImages.add((ImageView) findViewById(ids[i]));
		}

		restartGame();
	}

	/**
	 * Triggered on answer clicked/touched
	 * 
	 * @param view
	 */
	public void onAnswer(View view) {

		if (!this.GAME_OVER && this.CAN_PLAY) {
			final ImageView clickedImg = (ImageView) view;
			new ImageViewOpacity(clickedImg).apply(false);

			final boolean won = this.wonQuestion(view.getTag() + "");
			
			saveAnsweredQuestion( won );
			
			if (won) {
				stopTimer();

				this.playCorrectSound();
				// this.goToNextQuestion();
				updateCurrentAnimalDescriptionUI();
				hideGameOver();
				hideInCorrect();

				showNextButton();
				showAnimalDescription();

				// The label is only visible for 1 second
				showCorrect();

				// Open popup after 1 second
				new TickTimer(1000, 500).start(new TimerCallback() {
					@Override
					public void onUpdate(int elapsed) {
					}

					@Override
					public void onComplete() {
						activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								showPopup();
							}
						});
					}
				});
			} else {
				// Only visible for 1 second
				showInCorrect();
				this.playIncorrectSound();
			}
		} else if (this.GAME_OVER) {
			this.showSessionOver();
		} else {
			/*new ToastAlert(activity, "Cannot play....waitt...")
				//	.show(Gravity.CENTER); */
		}
	}

	public void onStart(View view) {
		this.restartGame();
	}

	public void onNext(View view) {
		this.goToNextQuestion();
	}

	public void onExit(View view) {
		this.GAME_OVER = true;
		this.finish();
		moveTaskToBack(true);
	}
	
	public void onHome(View view) {
		this.GAME_OVER = true;
		utils.redictToActivity(activity, SplashActivity.class );
	}
	
	public void onSettings(View view) {
		this.GAME_OVER = true;
		utils.redictToActivity(activity, SettingsActivity.class );
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

		hidePopup();
		hideCorrect();
		hideInCorrect();
		hideScore();

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

		startTimer();

		this.CAN_PLAY = true;
	}

	public void clearGameSession() {
		this.currentQuestionNo = 1;
		this.currentWord = null;
		this.GAME_OVER = false;
		this.CAN_PLAY = false;
		this.answeredQuestions.clear();
		this.scores.clearScores();
	}

	public void drawAllAnswerImages() {
		// set answers imageview tags to be the question answer
		// one of them holds the correct answer

		// To make sure nothing falls in the same place always
		// we also shuffle the answer images to get different positions
		ArrayList<ImageView> randomAnswerImages = new ArrayList<ImageView>(
				answerImages);
		randomAnswerImages = utils.generateRandomData(randomAnswerImages,
				randomAnswerImages.size());
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
		if (!TextUtils.isEmpty(currentWord.word)) {
			imgAnimal.setImageDrawable(currentWord.animal);

			// shuffle answer images also
			this.drawAllAnswerImages();
		} else {
			AppLogger.logError("No Current Question");
			this.showSessionOver();
		}
	}

	public void goToNextQuestion() {
		if (!finishedSession()) {
			this.currentQuestionNo++;
			this.loadCurrentQuestion();
			this.CAN_PLAY = true;
			this.hidePopup();
			this.hideCorrect();
			this.hideInCorrect();
			startTimer();
		} else {
			this.GAME_OVER = true;
			this.showSessionOver();
		}
	}


	/**
	 * Keeps timer for each question such that someone does not stay forever in
	 * the same question -- max 10 seconds per question
	 */
	public void startTimer() {

		stopTimer();

		timer = new TickTimer(Constants.TIME_TO_ANSWER, Constants.TIMER_TICK);

		timer.start(new TimerCallback() {
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
						try {
							if( ! GAME_OVER ){
								resetTimerUI();
								playTimeOverSound();
								goToNextQuestion();

								saveAnsweredQuestion( false );
							}else{
								stopTimer();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				});
			}
		});
	}

	public void stopTimer() {
		try {
			timer.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	
	public void saveAnsweredQuestion( boolean won ){
		HashMap<DictionaryWord, Boolean> map = new HashMap<DictionaryWord, Boolean>();
		map.put( this.getCurrentQuestion() , won );
		answeredQuestions.add(map);
		
		// save this score 
		Score score = new Score( this.getCurrentQuestion(), won );
		score.save();
		scores.addScore(score);
	}
	
	public int calculatePercentageScore( ){
		double win_percentage = (double) (scores.countPassed()/5.0)*100;
		int percent = (int) Math.round(win_percentage);
		percent = percent>100? 100: percent;
		return percent;
	}

	/**
	 * Determines whether the player has answered the current question correct
	 * or not
	 * 
	 * @return
	 */
	public boolean wonQuestion(String chosenAnswer) {
		return currentWord.word.equalsIgnoreCase(chosenAnswer) ? true : false;
	}

	public boolean finishedSession() {
		return currentQuestionNo < dictionaryWords.size() ? false : true;
	}

	public void playIncorrectSound() {
		soundPlayer.play("correct.mp3");
	}
	
	public void playTimeOverSound() {
		soundPlayer.play("incorrect.mp3");
	}

	public void playCorrectSound() {
		//soundPlayer.play("correct.mp3");
		playAnimalSound( currentWord.word );
	}
	
	public void playAnimalSound( String animal ){
		AppLogger.logError( "Playing sound for "+animal );
		soundPlayer.play( animal.toLowerCase(Locale.getDefault()).trim()+".mp3" );
	}

	public void showSessionOver() {
		hideNextButton();
		hideAnimalDescription();
		showGameOver();
		showPercentageScore();
		showPopup();
	}

	/***** -----------------[ POPUP METHODS ]-------------------------- *******/

	public void showPopup() {
		popup.setVisibility(View.VISIBLE);
		this.CAN_PLAY = false;
	}

	public void hidePopup() {
		popup.setVisibility(View.GONE);
	}

	public void updateCurrentAnimalDescriptionUI() {
		String description = this.getCurrentQuestion().description;
		txtAnimalDescripstion.setText(TextUtils.isEmpty(description) ? ""
				: description);
	}

	public void showAnimalDescription() {
		txtAnimalDescripstion.setVisibility(View.VISIBLE);
	}

	public void hideAnimalDescription() {
		txtAnimalDescripstion.setVisibility(View.GONE);
	}

	public void showNextButton() {
		btnNext.setVisibility(View.VISIBLE);
	}

	public void hideNextButton() {
		btnNext.setVisibility(View.GONE);
	}

	public void showGameOver() {
		gameOver.setVisibility(View.VISIBLE);
	}

	public void hideGameOver() {
		gameOver.setVisibility(View.GONE);
	}

	public void showCorrect() {
		correct.setVisibility(View.VISIBLE);
		// Hide after 1 second
		new TickTimer(1000, 50).start(new TimerCallback() {
			@Override
			public void onUpdate(int elapsed) {
			}

			@Override
			public void onComplete() {
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						hideCorrect();
					}
				});
			}
		});
	}

	public void hideCorrect() {
		correct.setVisibility(View.GONE);
	}

	public void showInCorrect() {
		inCorrect.setVisibility(View.VISIBLE);
		// Hide after 1 second
		new TickTimer(1000, 50).start(new TimerCallback() {
			@Override
			public void onUpdate(int elapsed) {
			}

			@Override
			public void onComplete() {
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						hideInCorrect();
					}
				});
			}
		});
	}

	public void hideInCorrect() {
		inCorrect.setVisibility(View.GONE);
	}
	
	public void showScore() {
		txtScoreLabel.setVisibility(View.VISIBLE );
		txtScore.setVisibility(View.VISIBLE );
	}
	public void hideScore() {
		txtScoreLabel.setVisibility(View.GONE);
		txtScore.setVisibility(View.GONE);
	}
	
	public void showPercentageScore( ){
		txtScore.setText( this.calculatePercentageScore()+"%" );
		showScore();
	}

}