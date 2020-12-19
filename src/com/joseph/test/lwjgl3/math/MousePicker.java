package com.joseph.test.lwjgl3.math;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.joseph.test.lwjgl3.GLFWHandler;
import com.joseph.test.lwjgl3.entity.Camera;
import com.joseph.test.lwjgl3.util.Point;

/**
 * Really dont like what this is called, like mouse picker, wtf is that? its so
 * weird, also idk if i like this class fully, but for now its fine
 * 
 * class from TUT is based on this article
 * https://antongerdelan.net/opengl/raycasting.html
 * 
 * @author Joseph
 *
 */
public class MousePicker {
	private Vector3f curRay;
	private Matrix4f projMat;
	private Matrix4f viewMat;
	private Camera cam;
	
	public MousePicker(Camera cam, Matrix4f proj) {
		this.cam = cam;
		this.projMat = proj;
		this.viewMat = MathHelper.createViewMatrix(cam);
		this.cam = cam;
	}
	
	public Vector3f getCurRay() {
		return this.curRay;
	}
	
	public void update() {
		viewMat = MathHelper.createViewMatrix(cam);
		this.curRay = this.calcRay();
	}
	
	private Vector3f calcRay() {
		// find screen coords
		Point<Float> mousePos = GLFWHandler.getMousePos();
		// normalize those pixel coords to be inside [-1,1] in both dirs
		Vector2f normDeviceCoords = this.getNormalizedDeviceCoords(mousePos);
		// changed them down one level, where the vec is pointing into the screen
		Vector4f clipCoords = new Vector4f(normDeviceCoords, -1.0f, 1.0f);
		// ray out of the eyes
		Vector4f eyeCoords = this.toEyeCoords(clipCoords);
		// ray to the world in world space
		Vector3f worldRay = this.toWorldCoords(eyeCoords);
		return worldRay;
	}
	
	private Vector3f toWorldCoords(Vector4f eyeCoords) {
		Matrix4f invertedViewMat = new Matrix4f(this.viewMat);
		invertedViewMat.invert();
		Vector4f eyeCord = invertedViewMat.transform(eyeCoords);
		return new Vector3f(eyeCord.x, eyeCord.y, eyeCord.z).normalize();
	}
	
	private Vector4f toEyeCoords(Vector4f clipCoords) {
		Matrix4f invertedProjMat = new Matrix4f(this.projMat);
		invertedProjMat.invert();
		Vector4f eyeCord = invertedProjMat.transform(clipCoords);
		return new Vector4f(eyeCord.x, eyeCord.y, -1.0f, 0.0f);
	}
	
	private Vector2f getNormalizedDeviceCoords(Point<Float> pos) {
		float x = (2.0f * pos.getX()) / GLFWHandler.SCREEN_WIDTH - 1.0f;
		float y = (2.0f * pos.getY()) / GLFWHandler.SCREEN_HEIGHT - 1.0f;
		return new Vector2f(x, -y);
	}
}
