package com.wicpar.sinking_simulator.engine.glfw;

import com.wicpar.sinking_simulator.engine.memory.ADestructible;

/**
 * Created by Frederic on 03/12/2016.
 */
public abstract class GLFWObject extends ADestructible {

	protected final GLFW glfw;

	public GLFWObject(final GLFW instance) {
		if (instance == null)
			throw new RuntimeException("valid GLFW instance needed");
		glfw = instance;
	}

	public GLFW getGlfw() {
		return glfw;
	}
}
