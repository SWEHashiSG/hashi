package ch.ntb.swehashisg.hashi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameTime {
	
	private static final Logger logger = LoggerFactory.getLogger(GameTime.class);

	private long startTime;
	private long currentTime;
	private long gameTime;
	
	public GameTime(){
		currentTime = 0;
		startTime = 0;
		gameTime = 0;
	}
	
	public void startTime(){	//start the Gametime
		logger.debug("start Timer");
		startTime = System.currentTimeMillis(); 
		
	}
	
	public void resetTime(){	//reset the GameTime
		logger.debug("reset timer");
		startTime = 0;
		currentTime = 0;
	}
	
	public void stopTime(){		//stop the GameTime
		logger.debug("stop timer");
		currentTime = System.currentTimeMillis();
	}
		
	
	public long getTime(){		//return Gametime 
		if (currentTime > 0){
			gameTime = currentTime - startTime;
		}else{
			gameTime = System.currentTimeMillis() - startTime;
		}
		return gameTime;
	}
	
	public boolean isRunning(){
		return (startTime != 0);
	}
}
