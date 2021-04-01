package com.joseph.test.lwjgl3.audio;

import java.io.IOException;

import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;

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
		
		// setup distance model
		AL10.alDistanceModel(AL11.AL_INVERSE_DISTANCE_CLAMPED);
		
		int buffer = Audio.loadSound("res/provided/audio/bounce.wav");
		int airHord = Audio.loadSound("res/fun/airhorn.wav");
		AudioSource source = new AudioSource();
		source.setLoop(true);
		source.play(buffer);
		AudioSource source2 = new AudioSource();
		source2.setPitch(1.0f);
		
		char c = ' ';
		boolean disableInputLoop = true;
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
		float xPos = 0.0f;
		source.stop();
		source.setVolume(1.5f);
		source.play(buffer);
		source.setPosition(xPos, 0, 0);
		while (xPos >= -5.0f) {
			xPos -= 0.03f;
			source.setPosition(xPos, 0, 0);
			System.out.println(xPos);
			Thread.sleep(10);
		}
		
		source2.delete();
		source.delete();
		Audio.cleanUp();
		
		// Thats it for this series of TUTs
	}
}
