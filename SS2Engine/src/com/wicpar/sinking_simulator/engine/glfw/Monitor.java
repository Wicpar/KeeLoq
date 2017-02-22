package com.wicpar.sinking_simulator.engine.glfw;

import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWVidMode;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by Frederic on 04/12/2016.
 */
public class Monitor extends GLFWObject {

	private final long id;
	private final int physicalWidthMM, physicalHeightMM;
	private final int xPos, yPos;
	private final String name;
	private final List<GLFWVidMode> supportedVideoModes = new ArrayList<>();

	public static List<Monitor> getMonitors(final GLFW glfw) {
		final PointerBuffer buf = glfwGetMonitors();
		final int cap = buf.capacity();
		final List<Monitor> monitors = new ArrayList<>(cap);
		for (int i = 0; i < cap; ++i) {
			monitors.add(new Monitor(glfw, buf.get(i)));
		}
		return monitors;
	}

	public static Monitor getPrimaryMonitor(final GLFW glfw) {
		return new Monitor(glfw, glfwGetPrimaryMonitor());
	}

	private Monitor(final GLFW instance, final long monitor) {
		super(instance);
		id = monitor;
		final int[] width = new int[1];
		final int[] height = new int[1];
		glfwGetMonitorPhysicalSize(id, width, height);
		physicalWidthMM = width[0];
		physicalHeightMM = height[0];
		name = glfwGetMonitorName(id);
		glfwGetMonitorPos(id, width, height);
		xPos = width[0];
		yPos = height[0];
		final GLFWVidMode.Buffer buf = glfwGetVideoModes(id);
		while (buf.hasRemaining()) {
			supportedVideoModes.add(buf.get());
		}
	}

	public long getId() {
		return id;
	}

	public int getPhysicalWidthMM() {
		return physicalWidthMM;
	}

	public int getPhysicalHeightMM() {
		return physicalHeightMM;
	}

	public int getxPos() {
		return xPos;
	}

	public int getyPos() {
		return yPos;
	}

	public String getName() {
		return name;
	}

	public GLFWVidMode getCurrentVidMode() {
		return glfwGetVideoMode(id);
	}

	public List<GLFWVidMode> getSupportedVideoModes() {
		return supportedVideoModes;
	}

	@Override
	protected void destroy() {

	}
}
