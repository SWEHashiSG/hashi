package ch.ntb.swehashisg.hashi.controller;

public class gametime {

	private long startTime;
	private long currentTime;
	private long gameTime;
	
	public gametime(){
		currentTime = 0;
		startTime = 0;
		gameTime = 0;
	}
	
	public void startTime(){
		startTime = System.currentTimeMillis();
	}
	
	public void resetTime(){
		startTime = 0;
		currentTime = 0;
	}
	
	public void stopTime(){
		currentTime = System.currentTimeMillis();
	}
		
	
	public long getTime(){
		if (currentTime > 0){
			gameTime = currentTime - startTime;
		}else{
			gameTime = System.currentTimeMillis() - startTime;
		}
		return gameTime;
	}
}
