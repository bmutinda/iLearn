package net.rebmos.ilearn.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

@SuppressLint("UseSparseArrays")
public class SoundManager {
	
	Context context;
	private SoundPool sound_pool;
	private AudioManager audio_manager;
	
	private int maximum_sounds = 0;
	
	// Sound playback interval
	int delay_interval = Constants.SOUND_DELAY_INTERVAL;
	
	// Sound remove from queue interval
	int repeat_interval = Constants.SOUND_REPEAT_INTERVAL;
	int ready_to_remove = repeat_interval;
	
	// Stores our sound assets in the form of 
	// element tag_id and the filename to the sound 
	HashMap<Integer, Integer> sound_pool_map;
	
	// We will use this to control when the 
	// the simulated sound should play after the previous one 
	Timer timer;
	
	// Our timertask object which will be playing our sounds 
	// in the background
	SoundPlayTimerTask sound_play_timer_task;
	
	// For simulation purposes, we need to play the next sound after
	// the previous one .. in that order to avoid ommitted sounds..
	// for this, we need an array to hold playable sounds queue 
	// such that our timer will be picking a sound from this queue, playing then removing it
	ArrayList<Integer> sounds_queue;
	
	// Keep Callbacks to be  called once all the sounds in the queue are played 
	Class<?> callback;
	
	
	public SoundManager( Context c, int total_sounds) {
		context = c;
		maximum_sounds = total_sounds;
		sound_pool_map = new HashMap<Integer, Integer>();
		sound_pool = new SoundPool(maximum_sounds, AudioManager.STREAM_MUSIC, 0 );
		audio_manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		
		sounds_queue = new ArrayList<Integer>();
	}
	
	public void addSound( int element_id, String filename ){
		sound_pool_map.put(element_id, sound_pool.load(filename, 1) );
	}
	
	public void playSound( int element_id, boolean do_queue ){
		doPlaySound( element_id, do_queue );
	}
	
	public void playSound( int element_id ){
		doPlaySound( element_id, false );
	}
	
	private void doPlaySound( int element_id, boolean queue ){
		if( queue == true ){
			createTimer( );
			// For every sound we play, we just push it in an array 
			sounds_queue.add(element_id );
		}else{
			stopTimer( );
			float stream_volume = audio_manager.getStreamVolume( AudioManager.STREAM_MUSIC );
			stream_volume = stream_volume/ audio_manager.getStreamMaxVolume( AudioManager.STREAM_MUSIC );
			sound_pool.play( sound_pool_map.get(element_id), stream_volume, stream_volume, 1, 0, 1f );
		}
	}
	
	public boolean hasQueue( ){
		return sounds_queue.size()>0 ? true: false;
	}
	
	public void setQueueCallback( Class<?> t ){
		callback = t;
	}
	
	public void createTimer( ){
		/*
		 * We need to use our timer to make sure that all our play events are 
		 * managed well to avoid app crashes 
		 */
		if( timer!= null ){
			timer.cancel();
		}
		
		timer = new Timer();
		sound_play_timer_task = new SoundPlayTimerTask( );
		// To register sound playbacks schedules,
		// for a one time we use a single shot otherwise we use cascaded schedules
		// then use our thread to stop if all the sounds are done playing
		
		//-- the playback time should have the time of the sound with the max length 
		// we delay for 1sec, repeat every 1sec
		timer.schedule(sound_play_timer_task, delay_interval, repeat_interval );
	}
	
	public void stopTimer( ){
		if( timer!= null ){
			timer.cancel();
			// clear our quue
			sounds_queue.clear();
		}
	}
	
	class SoundPlayTimerTask extends TimerTask{
		@Override
		public void run() {
			
			if(sounds_queue.size()>0 ){
				int current_id = sounds_queue.get( 0 );
				float stream_volume = audio_manager.getStreamVolume( AudioManager.STREAM_MUSIC );
				stream_volume = stream_volume/ audio_manager.getStreamMaxVolume( AudioManager.STREAM_MUSIC );
				sound_pool.play( sound_pool_map.get(current_id), stream_volume, stream_volume, 1, 0, 1f );
				
				AppLogger.logDebug("Playing sound.. "+sound_pool_map.get(current_id)+"" );
			}else{
				// We need to initiate our callback if we have nothing in the queue
				if( ! hasQueue() && callback !=null ){
					try{

						AppLogger.logDebug("callback ready. " );						
					}catch( Exception e){
						AppLogger.logDebug("Errror on callback "+e.getLocalizedMessage() );
					}
				}
				
				stopTimer();
			}
			
			ready_to_remove+=repeat_interval;
			/**
			 * For every second, 
			 * 	we remove a sound from our queue--starting from the first one 
			 *  And play the last element in this queue
			 */
			try{ 
				if( ready_to_remove %repeat_interval ==0 ){
					sounds_queue.remove( 0 );
				}
			}catch( Exception e ){
				AppLogger.logDebug("Exception on sound remove.. "+e.getLocalizedMessage() );
			}
		}
	}
	

}
