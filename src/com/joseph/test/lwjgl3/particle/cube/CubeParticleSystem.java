package com.joseph.test.lwjgl3.particle.cube;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.joseph.test.lwjgl3.Main;

public class CubeParticleSystem {
	private static final float PPS = 50.0f;
	private static final Vector3f POSZ = new Vector3f(0.0f, 0.0f, 1.0f);
	
	private List<CubeParticle> particles = new ArrayList<CubeParticle>();
	
	public void update() {
		this.generateParticles();
		Iterator<CubeParticle> it = particles.iterator();
		while (it.hasNext()) {
			CubeParticle p = it.next();
			if (!p.update()) {
				it.remove();
			}
		}
	}
	
	private void generateParticles() {
		float particlesToCreate = PPS * Main.delta;
		int count = (int) particlesToCreate;
		float partialParticle = particlesToCreate % 1;
		for (int i = 0; i < count; i++) {
			particles.add(new CubeParticle(null));
		}
	}
	
	public List<CubeParticle> getParticles() {
		return this.particles;
	}
	
	public static Vector3f generateRandomUnitVectorWithinCode(Vector3f coneDirection, float angle) {
		float cosAngle = (float) Math.cos(angle);
		Random r = new Random();
		float theta = (float) (r.nextFloat() * 2.0f * Math.PI);
		float z = cosAngle + (r.nextFloat() * (1 - cosAngle));
		float rootOneMinusZSquared = (float) Math.sqrt(1 - z * z);
		float x = (float) (rootOneMinusZSquared * Math.cos(theta));
		float y = (float) (rootOneMinusZSquared * Math.sin(theta));
		
		Vector4f direction = new Vector4f(x, y, z, 1.0f);
		if (coneDirection.x != 0 || coneDirection.y != 0 || (coneDirection.z != 1 && coneDirection.z != -1)) {
			Vector3f rotateAxis = coneDirection.cross(POSZ, new Vector3f());
			rotateAxis.normalize();
			float rotateAngle = (float) Math.acos(coneDirection.dot(POSZ));
			Matrix4f rotationMatrix = new Matrix4f().rotate(-rotateAngle, rotateAxis);
			rotationMatrix.transform(direction);
		} else if (coneDirection.z == -1) {
			direction.z *= -1;
		}
		
		return new Vector3f(direction.x, direction.y, direction.z);
	}
}
