package com.joseph.test.lwjgl3.test;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import com.joseph.test.lwjgl3.GLFWHandler;

public class FrustrumViewerFrameBuffer {
	private int fbo;
	private int texture;
	private int depthBuf;
	private final int width = GLFWHandler.SCREEN_WIDTH / 2;
	private final int height = GLFWHandler.SCREEN_HEIGHT / 2;
	
	public FrustrumViewerFrameBuffer() {
		// generate the frame buffer and bind it
		fbo = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);
		GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
		// generate the color texture that will be rendered to
		texture = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, texture, 0);
		// generate the depth buffer
		depthBuf = GL30.glGenRenderbuffers();
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuf);
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width, height);
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, depthBuf);
		// unbind current boi
		this.unbindCurrentFrameBuffer();
	}
	
	public void cleanUp() {
		// delete things
		GL30.glDeleteFramebuffers(fbo);
		GL11.glDeleteTextures(texture);
		GL30.glDeleteRenderbuffers(depthBuf);
	}
	
	public void bindBuffer() {
		// bind for rendering
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, fbo);
		GL11.glViewport(0, 0, width, height);
	}
	
	public void unbindCurrentFrameBuffer() {
		// go back to normal rendering
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL11.glViewport(0, 0, GLFWHandler.SCREEN_WIDTH, GLFWHandler.SCREEN_HEIGHT);
	}
	
	public int getTexture() {
		return this.texture;
	}
}
