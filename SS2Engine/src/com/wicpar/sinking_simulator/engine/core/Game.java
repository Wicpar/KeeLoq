package com.wicpar.sinking_simulator.engine.core;

import com.wicpar.sinking_simulator.engine.core.scenes.Scene;
import com.wicpar.sinking_simulator.engine.glfw.Window;
import com.wicpar.sinking_simulator.engine.memory.ADestructible;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;

/**
 * Created by Frederic on 06/12/2016.
 */
public abstract class Game {

	private final ArrayList<Scene> scenes = new ArrayList<>();
	private boolean vsync;

	public Game(final boolean vsync) {
		this.vsync = vsync;
	}

	public void setVsync(final boolean vsync) {
		this.vsync = vsync;
		scenes.get(scenes.size() - 1)
		      .getWindow()
		      .setSwapInterval(vsync ? 1 : 0);
	}

	public void addScene(final Scene scene) {
		if (vsync) {
			if (scenes.size() > 0)
				scenes.get(scenes.size() - 1)
				      .getWindow()
				      .setSwapInterval(0);
			scene.getWindow()
			     .setSwapInterval(1);
		} else {
			scene.getWindow()
			     .setSwapInterval(0);
		}
		scenes.add(scene);
	}

	public void removeScene(final Scene scene) {
		scenes.remove(scene);
		if (vsync) {
			scenes.get(scenes.size() - 1)
			      .getWindow()
			      .setSwapInterval(1);
		}
	}

	public void start() {
		onStart();
		while (scenes.size() > 0) {
			loop();
		}
		onEnd();
	}

	private void loop() {
		preLoop();
		render();
		postLoop();
		glfwPollEvents();
		ADestructible.destroyFailedInMainThread();
	}

	public void render() {
		scenes.removeIf(scene -> {
			if (scene.loop()) {
				scene.getWindow().destroy();
				return true;
			}
			return false;
		});
		scenes.stream()
		      .map(Scene::getWindow)
		      .forEach(Window::swapBuffers);
	}

	protected abstract void onStart();

	protected abstract void preLoop();

	protected abstract void postLoop();

	protected abstract void onEnd();

}
