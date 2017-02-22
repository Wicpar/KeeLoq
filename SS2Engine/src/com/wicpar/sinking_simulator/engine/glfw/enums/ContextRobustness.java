package com.wicpar.sinking_simulator.engine.glfw.enums;

import static org.lwjgl.glfw.GLFW.GLFW_LOSE_CONTEXT_ON_RESET;
import static org.lwjgl.glfw.GLFW.GLFW_NO_RESET_NOTIFICATION;
import static org.lwjgl.glfw.GLFW.GLFW_NO_ROBUSTNESS;

/**
 * Created by Frederic on 05/12/2016.
 */
public enum ContextRobustness {

	NO_RESET_NOTIFICATION (GLFW_NO_RESET_NOTIFICATION ), LOSE_CONTEXT_ON_RESET(GLFW_LOSE_CONTEXT_ON_RESET), NO_ROBUSTNESS (GLFW_NO_ROBUSTNESS );

	private final int id;

	ContextRobustness(final int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
