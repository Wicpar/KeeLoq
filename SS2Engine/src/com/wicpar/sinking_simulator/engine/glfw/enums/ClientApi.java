package com.wicpar.sinking_simulator.engine.glfw.enums;

import static org.lwjgl.glfw.GLFW.GLFW_NO_API;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_API;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_ES_API;

/**
 * Created by Frederic on 05/12/2016.
 */
public enum ClientApi {

	OPENGL_API(GLFW_OPENGL_API), OPENGL_ES_API(GLFW_OPENGL_ES_API ), NO_API(GLFW_NO_API);

	private final int id;

	ClientApi(final int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
