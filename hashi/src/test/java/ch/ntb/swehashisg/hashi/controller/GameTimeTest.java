package ch.ntb.swehashisg.hashi.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class GameTimeTest {

	private GameTime gameTime;

	@Before
	public void testGameTime() {
		gameTime = new GameTime();
	}

	@Test
	public void testStartTime() {
		assertFalse(gameTime.isRunning());
		gameTime.startTime();
		assertTrue(gameTime.isRunning());
		gameTime.stopTime();
		gameTime.resetTime();
	}

	@Test
	public void testResetTime() {
		gameTime.startTime();
		int time = 1100;
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		gameTime.stopTime();
		assertEquals("Meassured Time should be 1 Second", time / 1000, gameTime.getTime());
		gameTime.resetTime();
		assertEquals(0, gameTime.getTime());
	}

	@Test
	public void testStopTime() {
		assertFalse(gameTime.isRunning());
		gameTime.startTime();
		assertTrue(gameTime.isRunning());
		gameTime.stopTime();
		assertFalse(gameTime.isRunning());
		gameTime.resetTime();
	}

	@Test
	public void testGetTime() {
		gameTime.startTime();
		int time = 1100;
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		gameTime.stopTime();
		assertEquals(time / 1000, gameTime.getTime());
	}
}
