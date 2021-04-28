package com.joseph.test.lwjgl3.grid;

import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL40;

import com.joseph.test.lwjgl3.entity.Camera;
import com.joseph.test.lwjgl3.entity.Entity;
import com.joseph.test.lwjgl3.math.MathHelper;
import com.joseph.test.lwjgl3.models.RawModel;
import com.joseph.test.lwjgl3.models.TexturedModel;
import com.joseph.test.lwjgl3.shaders.ShaderProgram;

// a combinded renderer and shader, to have less class bloat
public class GridRendererShader extends ShaderProgram {
	private static final String VERTEX_FILE = "/com/joseph/test/lwjgl3/grid/grid.vert";
	private static final String GEOMETRY_FILE = "/com/joseph/test/lwjgl3/grid/grid.geom";
	private static final String FRAGMENT_FILE = "/com/joseph/test/lwjgl3/grid/grid.frag";
	
	private static final Vector3f OFFSET1 = new Vector3f(-1.0f, 0.0f, 0.0f);
	private static final Vector3f OFFSET2 = new Vector3f(0.0f, 0.0f, -1.0f);
	
	private GridVao vao;
	
	private Matrix4f projMat;
	
	private int projViewMatLocation;
	private int offsetDirLocation;
	private int radiusLocation;
	private int colorLocation;
	
	public GridRendererShader(Matrix4f projMat) {
		super(VERTEX_FILE, GEOMETRY_FILE, FRAGMENT_FILE);
		// at this point in the code, both bind attributes and get uniform locations will have been called
		this.projMat = projMat;
		this.vao = GridVao.create();
		super.start();
		this.loadRadius(10);
		super.stop();
	}
	
	public void render(Camera cam) {
		float width = GL11.glGetFloat(GL11.GL_LINE_WIDTH);
		GL11.glLineWidth(3.0f);
		super.start();
		this.loadProjViewMat(cam);
		GL30.glBindVertexArray(vao.getVao());
		GL20.glEnableVertexAttribArray(0);
		super.loadVector(offsetDirLocation, OFFSET2);
		GL11.glDrawArrays(GL11.GL_POINTS, 0, vao.getVertCount() / 2);
		super.loadVector(offsetDirLocation, OFFSET1);
		GL11.glDrawArrays(GL11.GL_POINTS, vao.getVertCount() / 2, vao.getVertCount() / 2);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		super.stop();
		GL11.glLineWidth(width);
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	@Override
	protected void getUniformLocations() {
		this.projViewMatLocation = super.getUniformLocation("projectionViewMatrix");
		this.offsetDirLocation = super.getUniformLocation("offsetDir");
		this.radiusLocation = super.getUniformLocation("radius");
		this.colorLocation = super.getUniformLocation("color");
	}
	
	public void loadProjViewMat(Camera cam) {
		Matrix4f viewMat = MathHelper.createViewMatrix(cam);
		Matrix4f projView = projMat.mul(viewMat, viewMat);
		super.loadMatrix4(projViewMatLocation, projView);
	}
	
	public void loadRadius(int radius) {
		if (this.vao.getRadius() == radius) {
			return;
		}
		super.loadInt(radiusLocation, radius);
		this.vao.generateFresh(radius, 1);
	}
	
	public void loadColor(Vector3f color) {
		super.loadVector(colorLocation, color);
	}
}
