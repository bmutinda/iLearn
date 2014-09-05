package net.rebmos.ilearn.utilities;

import java.util.Timer;
import java.util.TimerTask;

import net.rebmos.ilearn.callbacks.TimerCallback;

public class TickTimer {
	// Total Time the timer should take in milliseconds
	public int time = 0;
	// The tick interval
	public int tick = 500;

	private Timer timer;
	private TickTimerTask timerTask;
	public TimerCallback callback;

	// Time elapsed since the timer started
	private int elapsed = 0;

	public TickTimer(int time) {
		this.create(time, this.tick);
	}

	public TickTimer(int time, int tick) {
		this.create(time, tick);
	}

	private void create(int time, int tick) {
		this.time = time;
		this.tick = tick;
		timerTask = new TickTimerTask();
	}

	public void start( TimerCallback timerCallback ){
		this.stop();
		
		timer = new Timer();
		timer.schedule(timerTask, 0, this.tick );
		this.callback = timerCallback;
	}

	public void stop() {
		if (timer != null) {
			timer.cancel();
		}
	}

	class TickTimerTask extends TimerTask {

		@Override
		public void run() {
			if( elapsed>= time ){
				stop( );
				if( callback!=null ){
					callback.onComplete();
				}
			}else{
				if( callback!=null ){
					callback.onUpdate(elapsed);
				}
				elapsed+=tick;
			}
		}
	}
}
