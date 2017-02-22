package com.wicpar.sinking_simulator.engine.glfw.enums;

import static org.lwjgl.glfw.GLFW.GLFW_ANY_RELEASE_BEHAVIOR;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE_BEHAVIOR_FLUSH;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE_BEHAVIOR_NONE;

/**
 * Created by Frederic on 05/12/2016.
 */
public enum ReleaseBehavior {

	ANY_RELEASE_BEHAVIOR (GLFW_ANY_RELEASE_BEHAVIOR ), RELEASE_BEHAVIOR_FLUSH (GLFW_RELEASE_BEHAVIOR_FLUSH ), RELEASE_BEHAVIOR_NONE(GLFW_RELEASE_BEHAVIOR_NONE);

	private final int id;

	ReleaseBehavior(final int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
