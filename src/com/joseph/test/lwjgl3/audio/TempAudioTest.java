package com.joseph.test.lwjgl3.audio;

import java.io.IOException;

public class TempAudioTest {
	public static void main(String[] args) throws IOException {
		try {
			Audio.initAL();			
		} catch (IllegalStateException ise) {
			ise.printStackTrace();
			// for some reason this causes a problem?
			// ohhhhh its cause AL inits its self unless you disable it
		}
		Audio.setListenerData();
		
		int buffer = Audio.loadSound("res/provided/audio/bounce.wav");
		int airHord = Audio.loadSound("res/fun/airhorn.wav");
		AudioSource source = new AudioSource();
		AudioSource horn = new AudioSource();
		
		char c = ' ';
		while (c != 'q') {
			c = (char) System.in.read();
			
			if (c == 'p') {
				source.play(buffer);
			} else if (c == 'a') {
				horn.play(airHord);
			}
		}
		
		source.delete();
		Audio.cleanUp();
	}
}
