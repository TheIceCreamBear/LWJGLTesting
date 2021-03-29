package com.joseph.test.lwjgl3.audio;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.lwjgl.openal.AL10;

import com.sun.media.sound.WaveFileReader;

/** 
 * A port of the LWJGL 2 Wave Data class. original can be found here
 * https://github.com/LWJGL/lwjgl/blob/2df01dd762e20ca0871edb75daf670ccacc89b60/src/java/org/lwjgl/util/WaveData.java
 */
public class WaveSample {
	public final ByteBuffer data;
	public final int format;
	public final int samplerate;
	
	private WaveSample(ByteBuffer data, int format, int samplerate) {
		this.data = data;
		this.format = format;
		this.samplerate = samplerate;
	}
	
	public void dispose() {
		data.clear();
	}
	
	public static WaveSample create(String path) {
		// this doesnt work?
		BufferedInputStream bis = new BufferedInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream(path));
		WaveFileReader wfr = new WaveFileReader();
		AudioInputStream ais = null;
		try {
			ais = wfr.getAudioInputStream(bis);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				ais = AudioSystem.getAudioInputStream(new File(path));
			} catch (Exception e2) {
				e2.printStackTrace();
				return null;
			}
		}
		
		AudioFormat format = ais.getFormat();
		
		int channels = 0;
		if (format.getChannels() == 1) {
			if (format.getSampleSizeInBits() == 8) {
				channels = AL10.AL_FORMAT_MONO8;
			} else if (format.getSampleSizeInBits() == 16) {
				channels = AL10.AL_FORMAT_MONO16;
			} else {
				return null;
			}
		} else if (format.getChannels() == 2) {
			if (format.getSampleSizeInBits() == 8) {
				channels = AL10.AL_FORMAT_STEREO8;
			} else if (format.getSampleSizeInBits() == 16) {
				channels = AL10.AL_FORMAT_STEREO16;
			} else {
				return null;
			}
		} else {
			return null;
		}
		
		ByteBuffer buf = null;
		try {
			int available = ais.available();
			if (available < 0) { 
				available = format.getChannels() * (int) ais.getFrameLength() * (format.getSampleSizeInBits() / 8);
			}
			byte[] data = new byte[available];
			int read = 0;
			int total = 0;
			while ((read = ais.read(data, total, data.length - total)) != -1 && total < data.length) {
				total += read;
			}
			buf = convertAudioBytes(data, format.getSampleSizeInBits() == 16, format.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return null;
		}
		
		WaveSample sample = new WaveSample(buf, channels, (int) format.getSampleRate());
		
		try {
			ais.close();
		} catch (IOException ioe) {}
		
		return sample;
	}
	
	private static ByteBuffer convertAudioBytes(byte[] audio, boolean twoBytes, ByteOrder order) {
		ByteBuffer dest = ByteBuffer.allocateDirect(audio.length);
		dest.order(ByteOrder.nativeOrder());
		ByteBuffer src = ByteBuffer.wrap(audio);
		src.order(order);
		if (twoBytes) {
			ShortBuffer shortDest = dest.asShortBuffer();
			ShortBuffer shortSrc = src.asShortBuffer();
			while (shortSrc.hasRemaining()) {
				shortDest.put(shortSrc.get());
			}
		} else {
			while (src.hasRemaining()) {
				dest.put(src.get());
			}
		}
		dest.rewind();
		return dest;
	}
}
