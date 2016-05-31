package ch.ntb.swehashisg.hashi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameTime {

	private static final Logger logger = LoggerFactory.getLogger(GameTime.class);

	private static final int MILLISEC_TO_SEC = 1000;

	private long startTime;
	private long currentTime;
	private long gameTime;

	public GameTime() {
		currentTime = 0;
		startTime = 0;
		gameTime = 0;
	}

	public void startTime() { // start the Gametime
		logger.debug("start Timer");
		startTime = System.currentTimeMillis();

	}

	public void resetTime() { // reset the GameTime
		logger.debug("reset timer");
		startTime = 0;
		currentTime = 0;
	}

	public void stopTime() { // stop the GameTime
		logger.debug("stop timer");
		currentTime = System.currentTimeMillis();
	}

	public long getTime() { // return Gametime
		gameTime = currentTime - startTime;
		return gameTime / MILLISEC_TO_SEC;
	}

	public boolean isRunning() {
		return (startTime == 0) != (currentTime == 0);
	}
}
