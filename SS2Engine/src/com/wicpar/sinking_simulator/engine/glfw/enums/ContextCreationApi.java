package com.wicpar.sinking_simulator.engine.glfw.enums;

import static org.lwjgl.glfw.GLFW.GLFW_EGL_CONTEXT_API;
import static org.lwjgl.glfw.GLFW.GLFW_NATIVE_CONTEXT_API;

/**
 * Created by Frederic on 05/12/2016.
 */
public enum ContextCreationApi {

	NATIVE_CONTEXT_API(GLFW_NATIVE_CONTEXT_API), EGL_CONTEXT_API(GLFW_EGL_CONTEXT_API );

	private final int id;

	ContextCreationApi(final int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
