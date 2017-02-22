package com.wicpar.sinking_simulator.engine.glfw;

import com.wicpar.sinking_simulator.engine.memory.ADestructible;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by Frederic on 03/12/2016.
 */
public class GLFW extends ADestructible {

	public static GLFW init() {
		return new GLFW();
	}

	private final GLFWErrorCallback errorCallback;

	private GLFW() {
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");
		errorCallback = GLFWErrorCallback.createPrint(System.err).set();
	}

	@Override
	protected void destroy() {
		errorCallback.free();
		glfwTerminate();
	}
}
