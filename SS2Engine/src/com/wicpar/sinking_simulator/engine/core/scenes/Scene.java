package com.wicpar.sinking_simulator.engine.core.scenes;

import com.wicpar.sinking_simulator.engine.glfw.Window;

/**
 * Created by Frederic on 06/12/2016.
 */
public abstract class Scene {

	protected final Window window;

	public Scene(final Window window) {
		this.window = window;
		window.useContext();
	}

	public Window getWindow() {
		return window;
	}

	private  boolean first = true;
	public boolean loop() {
		final boolean shouldClose = window.shouldClose();
		if (!shouldClose) {
			window.useContext();
			onLoop(window);
		}
		return shouldClose;
	}

	protected abstract void onLoop(final Window window);

}
