package ch.ntb.swehashisg.hashi.controller;

public class GameTime {

	private long startTime;
	private long currentTime;
	private long gameTime;
	
	public GameTime(){
		currentTime = 0;
		startTime = 0;
		gameTime = 0;
	}
	
	public void startTime(){	//start the Gametime
		startTime = System.currentTimeMillis(); 
		
	}
	
	public void resetTime(){	//reset the GameTime
		startTime = 0;
		currentTime = 0;
	}
	
	public void stopTime(){		//stop the GameTime
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
}
