package com.joseph.test.lwjgl3.audio;

import org.lwjgl.openal.AL10;

public class AudioSource {
	private int sourceId;
	
	public AudioSource() {
		this.sourceId = AL10.alGenSources();
	}
	
	public void play(int buffer) {
		this.stop();
		AL10.alSourcei(sourceId, AL10.AL_BUFFER, buffer);
		this.resume();
	}
	
	public void delete() {
		this.stop();
		AL10.alDeleteSources(sourceId);
	}
	
	public void pause() {
		AL10.alSourcePause(sourceId);
	}
	
	public void resume() {
		AL10.alSourcePlay(sourceId);
	}
	
	public void stop() {
		AL10.alSourceStop(sourceId);
	}
	
	public boolean isPlaying() {
		return AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}
	
	public void setLoop(boolean loop) {
		AL10.alSourcei(sourceId, AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE);
	}
	
	public void setVolume(float volume) {
		AL10.alSourcef(sourceId, AL10.AL_GAIN, volume);
	}
	
	public void setPitch(float pitch) {
		AL10.alSourcef(sourceId, AL10.AL_PITCH, pitch);
	}
	
	public void setPosition(float x, float y, float z) {
		AL10.alSource3f(sourceId, AL10.AL_POSITION, x, y, z);
	}
	
	public void setVelocity(float x, float y, float z) {
		AL10.alSource3f(sourceId, AL10.AL_POSITION, x, y, z);
	}
}
