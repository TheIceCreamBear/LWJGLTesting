package com.joseph.test.lwjgl3.particle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.joml.Matrix4f;

import com.joseph.test.lwjgl3.entity.Camera;

public class Particles {
	private static Map<ParticleTexture, List<Particle>> particles = new HashMap<ParticleTexture, List<Particle>>();
	private static ParticleRenderer renderer;
	
	public static void init(Matrix4f projectionMatrix) {
		renderer = new ParticleRenderer(projectionMatrix);
	}
	
	public static void update(Camera cam) {
		// okay so here is where i want to dis agree but who knows
		// the tut is about to use an Iterator to iterate through
		// the list and so it can remove the particles. my thought would
		// be to just add them to a seperate list when they have passed their life time
		// but maybe this is better????
		// iterators doesnt seem to bad rn tho
		Iterator<Entry<ParticleTexture, List<Particle>>> mapit = particles.entrySet().iterator();
		while (mapit.hasNext()) {
			List<Particle> list = mapit.next().getValue();
			Iterator<Particle> it = list.iterator();
			while (it.hasNext()) {
				Particle p = it.next();
				if (!p.update(cam)) {
					it.remove();
					if (list.isEmpty()) {
						mapit.remove();
					}
				}
			}
			ParticleInsertionSort.sortHighToLow(list);
		}
	}
	
	public static void addParticle(Particle particle) {
		List<Particle> list = particles.get(particle.getTexture());
		if (list == null) {
			list = new ArrayList<Particle>();
			particles.put(particle.getTexture(), list);
		}
		list.add(particle);
	}
	
	public static void renderParticles(Camera cam) {
		renderer.render(particles, cam);
	}
	
	public static void cleanUp() {
		renderer.cleanUp();
	}
	
	// this class was provided by the tut, in a seperate file so i put it in this file here, but yea
	// this is from the tut dude so might not be the best
	public static class ParticleInsertionSort {
		public static void sortHighToLow(List<Particle> list) {
			for (int i = 1; i < list.size(); i++) {
				Particle item = list.get(i);
				if (item.getDistance() > list.get(i - 1).getDistance()) {
					sortUpHighToLow(list, i);
				}
			}
		}

		private static void sortUpHighToLow(List<Particle> list, int i) {
			Particle item = list.get(i);
			int attemptPos = i - 1;
			while (attemptPos != 0 && list.get(attemptPos - 1).getDistance() < item.getDistance()) {
				attemptPos--;
			}
			list.remove(i);
			list.add(attemptPos, item);
		}
	}
}
