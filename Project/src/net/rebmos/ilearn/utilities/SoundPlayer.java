package net.rebmos.ilearn.utilities;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;

public class SoundPlayer {
	public String fileName;

	Activity activity;
	AssetFileDescriptor afd;
	MediaPlayer player;
	Utils utils;
	private AudioManager audio_manager;

	public SoundPlayer(Activity activity) {
		this.activity = activity;
		this.utils = new Utils(activity);
		audio_manager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
	}

	public void play(String fileName) {
		Boolean soundEnabled = Boolean.parseBoolean( ILearnApplication.database.getSettingInfo( Constants.SOUND_TAG ) );

		if( soundEnabled ){
			try {
				this.player = new MediaPlayer();
				afd = activity.getAssets().openFd(
						this.utils.getSoundsDirectory() + fileName);
				player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
						afd.getLength());
				
				float stream_volume = audio_manager.getStreamVolume( AudioManager.STREAM_MUSIC );
				stream_volume = stream_volume/ audio_manager.getStreamMaxVolume( AudioManager.STREAM_MUSIC );
				player.setVolume( stream_volume, stream_volume );
				
				player.prepare();
				player.start();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			AppLogger.logError( "Sound is disabled....");
		}
		
	}

}
