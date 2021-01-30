package com.joseph.test.lwjgl3.water;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL42;

import com.joseph.test.lwjgl3.GLFWHandler;

/**
 * This code handles the frame buffers that store the reflection and refraction information of the water.
 * I have issues with how the tut went about this code that i will write when i get to it.
 * 
 * any comment that occurs on the same line as { is one from the dude, and also not needed cause it should doccument its self
 *
 */
public class WaterFrameBuffers {
	
	protected static final int REFLECTION_WIDTH = 640;
	private static final int REFLECTION_HEIGHT = 360;
	
	protected static final int REFRACTION_WIDTH = 1280;
	private static final int REFRACTION_HEIGHT = 720;
	
	private int reflectionFrameBuffer;
	private int reflectionTexture;
	private int reflectionDepthBuffer;
	
	private int refractionFrameBuffer;
	private int refractionTexture;
	private int refractionDepthTexture;
	
	public WaterFrameBuffers() {// call when loading the game
		initialiseReflectionFrameBuffer();
		initialiseRefractionFrameBuffer();
	}
	
	public void cleanUp() {// call when closing the game
		GL30.glDeleteFramebuffers(reflectionFrameBuffer);
		GL11.glDeleteTextures(reflectionTexture);
		GL30.glDeleteRenderbuffers(reflectionDepthBuffer);
		GL30.glDeleteFramebuffers(refractionFrameBuffer);
		GL11.glDeleteTextures(refractionTexture);
		GL11.glDeleteTextures(refractionDepthTexture);
	}
	
	public void bindReflectionFrameBuffer() {// call before rendering to this FBO
		bindFrameBuffer(reflectionFrameBuffer, REFLECTION_WIDTH, REFLECTION_HEIGHT);
	}
	
	public void bindRefractionFrameBuffer() {// call before rendering to this FBO
		bindFrameBuffer(refractionFrameBuffer, REFRACTION_WIDTH, REFRACTION_HEIGHT);
	}
	
	public void unbindCurrentFrameBuffer() {// call to switch to default frame buffer
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL11.glViewport(0, 0, GLFWHandler.SCREEN_WIDTH, GLFWHandler.SCREEN_HEIGHT);
	}
	
	public int getReflectionTexture() {// get the resulting texture
		return reflectionTexture;
	}
	
	public int getRefractionTexture() {// get the resulting texture
		return refractionTexture;
	}
	
	public int getRefractionDepthTexture() {// get the resulting depth texture
		return refractionDepthTexture;
	}
	
	private void initialiseReflectionFrameBuffer() {
		reflectionFrameBuffer = createFrameBuffer();
		reflectionTexture = createTextureAttachment(REFLECTION_WIDTH, REFLECTION_HEIGHT);
		reflectionDepthBuffer = createDepthBufferAttachment(REFLECTION_WIDTH, REFLECTION_HEIGHT);
		unbindCurrentFrameBuffer();
	}
	
	private void initialiseRefractionFrameBuffer() {
		refractionFrameBuffer = createFrameBuffer();
		refractionTexture = createTextureAttachment(REFRACTION_WIDTH, REFRACTION_HEIGHT);
		refractionDepthTexture = createDepthTextureAttachment(REFRACTION_WIDTH, REFRACTION_HEIGHT);
		unbindCurrentFrameBuffer();
	}
	
	/**
	 * binds the specified frame buffer with witdh and height, maybe a frame buffer should be a class so you dont have to rely on
	 * storing the width and height in some random variable?
	 * @param frameBuffer
	 * @param width
	 * @param height
	 */
	private void bindFrameBuffer(int frameBuffer, int width, int height) {
		// makes sure not textures are bound
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0); // To make sure the texture isn't bound
		// binds the buffer
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		// tells OGL what the new view port is so that it behaves correctly when rendering
		GL11.glViewport(0, 0, width, height);
	}
	
	/**
	 * so the comments in this one are left over from the tut dude, and i honestly cannot believe that he comments
	 * on something AFTER it happens
	 * <p>
	 * its like oh lemme create a frame buffer
	 * <br><code> int frameBuffer = GL30.glGenFrameBuffers(); </code>
	 * <br><code> // generate a frame buffer </code>
	 * 
	 * LIKE WHAT?!?!???!!
	 * @return
	 */
	private int createFrameBuffer() {
		int frameBuffer = GL30.glGenFramebuffers();
		// generate name for frame buffer
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		// create the framebuffer
		GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
		// indicate that we will always render to color attachment 0
		return frameBuffer;
	}
	
	/**
	 * this, should be part of a frame buffer specific class and not a water frame buffer class (like it needs to be more generalized)
	 * @param width
	 * @param height
	 * @return
	 */
	private int createTextureAttachment(int width, int height) {
		// gen the tex
		int texture = GL11.glGenTextures();
		// bind it for a bit
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		// generate the texture memory area
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
		// set these filters as linear because that is what we are supposed to do 
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		// set this texture as a frame buffer texture as a color attachment
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, texture, 0);
		return texture;
	}

	/**
	 * this, should also be part of a frame buffer specific class and not a water frame buffer class (like it needs to be more generalized)
	 * @param width
	 * @param height
	 * @return
	 */
	private int createDepthTextureAttachment(int width, int height) {
		// gen the tex
		int texture = GL11.glGenTextures();
		// bind it for a bit
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		// generate the tex with depths components and no data its the memory area
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT32, width, height, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer) null);
		// set these filters as linear because that is what we are supposed to do 
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		// set this texture as a frame buffer texture as a depth attachment
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, texture, 0);
		return texture;
	}

	/**
	 * again, this should be part of a frame buffer specific class and not a water frame buffer class (like it needs to be more generalized)
	 * @param width
	 * @param height
	 * @return
	 */
	private int createDepthBufferAttachment(int width, int height) {
		// generate a buffer of into
		int depthBuffer = GL30.glGenRenderbuffers();
		// bind it for a bit
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
		// set that it will buffer depth information
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width, height);
		// attached it to the frame buffer (why do i feel like this assumes that you have already bound the frame buffer? This is sooo bad dude
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, depthBuffer);
		return depthBuffer;
	}
	
}
