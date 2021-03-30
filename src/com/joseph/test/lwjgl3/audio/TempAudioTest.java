package com.joseph.test.lwjgl3.audio;

import java.io.IOException;

public class TempAudioTest {
	public static void main(String[] args) throws IOException, InterruptedException {
		try {
			Audio.initAL();	
		} catch (IllegalStateException ise) {
			ise.printStackTrace();
			// for some reason this causes a problem?
			// ohhhhh its cause AL inits its self unless you disable it
		}
		Audio.setListenerData(0, 0, 0);
		
		int buffer = Audio.loadSound("res/provided/audio/bounce.wav");
		int airHord = Audio.loadSound("res/fun/airhorn.wav");
		AudioSource source = new AudioSource();
		source.setLoop(true);
		source.play(buffer);
		AudioSource source2 = new AudioSource();
		source2.setPitch(2.0f);
		
		char c = ' ';
		boolean disableInputLoop = false;
		if (disableInputLoop) {
			c = 'q';
		}
		while (c != 'q') {
			c = (char) System.in.read();
			
			if (c == 'p') {
				if (source.isPlaying()) {
					source.pause();
				} else {
					source.resume();
				}
			}
			
			if (c == 'o') {
				source2.play(airHord);
			}
		}
		
		// 3d audio example
		float xPos = 8.0f;
		source.stop();
		source.setVolume(1.5f);
		source.play(buffer);
		source.setPosition(xPos, 0, 2);
		while (xPos >= -8.0f) {
			xPos -= 0.03f;
			source.setPosition(xPos, 0, 2);
			Thread.sleep(10);
		}
		
		source2.delete();
		source.delete();
		Audio.cleanUp();
	}
}
