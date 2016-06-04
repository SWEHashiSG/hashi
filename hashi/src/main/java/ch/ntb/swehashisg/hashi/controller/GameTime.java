package ch.ntb.swehashisg.hashi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Timer to measure a time in seconds.
 * 
 * @author Joel
 *
 */
public class GameTime {

	private static final Logger logger = LoggerFactory.getLogger(GameTime.class);

	/**
	 * Factor to calculate from milliseconds to seconds
	 */
	private static final int MILLISEC_TO_SEC = 1000;

	/**
	 * system time, when the timer has been started in milliseconds
	 */
	private long startTime;

	/**
	 * system time, when the timer has been stopped in milliseconds
	 */
	private long stopTime;

	/**
	 * Used to see if timer is running.
	 */
	private boolean running;

	/**
	 * Constructor for GameTime. Initialize start, stop, and runningTime with
	 * zero
	 */
	public GameTime() {
		stopTime = 0;
		startTime = 0;
		running = false;
	}

	/**
	 * start the timer with save the actual system time to startTime.
	 */
	public void startTime() {
		logger.debug("start Timer");
		startTime = System.currentTimeMillis();
		running = true;
	}

	/**
	 * reset the timer by set all back to zero
	 */
	public void resetTime() {
		logger.debug("reset timer");
		startTime = 0;
		stopTime = 0;
		running = false;
	}

	/**
	 * stop the timer with save the actual system time to stopTime.
	 */
	public void stopTime() {
		logger.debug("stop timer");
		stopTime = System.currentTimeMillis();
		running = false;
	}

	/**
	 * 
	 * @return how long the timer has been running in seconds
	 */
	public long getTime() {
		long runningTime = stopTime - startTime;
		return runningTime / MILLISEC_TO_SEC;
	}

	public boolean isRunning() {
		return running;
	}
}
