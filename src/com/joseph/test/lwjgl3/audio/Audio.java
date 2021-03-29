package com.joseph.test.lwjgl3.audio;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.openal.EXTThreadLocalContext;

/**
 * This got much more complicated than i expected, LWJGL 3 is a bit more complex
 * @author Joseph
 *
 */
public class Audio {
	private static List<Integer> buffers = new ArrayList<Integer>();
	private static boolean useTLC = false;
	private static ALCapabilities caps;
	private static ALCCapabilities deviceCaps;
	private static long context;
	private static long device;
	
	public static void initAL() {
//		ALC.create();
		device = ALC10.alcOpenDevice((String) null);
		if (device == 0) {
			throw new IllegalStateException("failed to open device");
		}
		
		deviceCaps = ALC.createCapabilities(device);
		
		if (!deviceCaps.OpenALC10) {
			throw new IllegalStateException("no al");
		}
		
		context = ALC10.alcCreateContext(device, (int[]) null);
		checkALCError(device);
		
		useTLC = deviceCaps.ALC_EXT_thread_local_context && EXTThreadLocalContext.alcSetThreadContext(context);
        if (!useTLC) {
            if (!ALC10.alcMakeContextCurrent(context)) {
                throw new IllegalStateException();
            }
        }
        checkALCError(device);
		
		caps = AL.createCapabilities(deviceCaps);
	}
	
	public static void setListenerData() {
		AL10.alListener3f(AL10.AL_POSITION, 0, 0, 0);
		AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
	}
	
	public static int loadSound(String file) { 
		int buffer = AL10.alGenBuffers();
		buffers.add(buffer);
		
		// TODO the use of the WaveSample class is temporary, instead the data should be loaded directly without the class
		WaveSample sample = WaveSample.create(file);
		AL10.alBufferData(buffer, sample.format, sample.data, sample.samplerate);
		sample.dispose();
		
		return buffer;
	}
	
	public static void cleanUp() {
		for (int buffer : buffers) {
			AL10.alDeleteBuffers(buffer);
		}
		
		ALC10.alcMakeContextCurrent(0);
		if (useTLC) {
			AL.setCurrentThread(null);
		} else {
			AL.setCurrentProcess(null);
		}
		
		ALC10.alcDestroyContext(context);
		ALC10.alcCloseDevice(device);
		
//		ALC.destroy();
	}
	
	public static void checkALCError(long device) {
		int err = ALC10.alcGetError(device);
		if (err != ALC10.ALC_NO_ERROR) {
			throw new RuntimeException(ALC10.alcGetString(device, err));
		}
	}
}
