package com.joseph.test.lwjgl3.particle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joml.Matrix4f;

import com.joseph.test.lwjgl3.entity.Camera;

public class Particles {
	private static List<Particle> particles = new ArrayList<Particle>();
	private static ParticleRenderer renderer;
	
	public static void init(Matrix4f projectionMatrix) {
		renderer = new ParticleRenderer(projectionMatrix);
	}
	
	public static void update() {
		// okay so here is where i want to dis agree but who knows
		// the tut is about to use an Iterator to iterate through
		// the list and so it can remove the particles. my thought would
		// be to just add them to a seperate list when they have passed their life time
		// but maybe this is better????
		Iterator<Particle> it = particles.iterator();
		while (it.hasNext()) {
			Particle p = it.next();
			if (!p.update()) {
				it.remove();
			}
		}
	}
	
	public static void addParticle(Particle particle) {
		particles.add(particle);
	}
	
	public static void renderParticles(Camera cam) {
		renderer.render(particles, cam);
	}
	
	public static void cleanUp() {
		renderer.cleanUp();
	}
}
