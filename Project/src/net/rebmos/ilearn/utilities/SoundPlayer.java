package net.rebmos.ilearn.utilities;

import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

public class SoundPlayer {
	public String fileName;

	Activity activity;
	AssetFileDescriptor afd;
	MediaPlayer player;
	Utils utils;

	public SoundPlayer(Activity activity) {
		this.activity = activity;
		this.utils = new Utils(activity);
	}

	public void play(String fileName) {
		try {
			this.player = new MediaPlayer();
			afd = activity.getAssets().openFd(
					this.utils.getSoundsDirectory() + fileName);
			player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
					afd.getLength());
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

	}

}
