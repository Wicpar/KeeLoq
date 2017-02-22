package com.wicpar.sinking_simulator.engine.glfw.enums;

import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_ANY_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_COMPAT_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;

/**
 * Created by Frederic on 05/12/2016.
 */
public enum OpenglProfile {

	OPENGL_CORE_PROFILE(GLFW_OPENGL_CORE_PROFILE), OPENGL_COMPAT_PROFILE(GLFW_OPENGL_COMPAT_PROFILE), OPENGL_ANY_PROFILE(GLFW_OPENGL_ANY_PROFILE);

	private final int id;

	OpenglProfile(final int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
