package com.wicpar.sinking_simulator.engine.opencl.core;

import com.wicpar.sinking_simulator.engine.opencl.structure.CLObject;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.APPLEGLSharing;
import org.lwjgl.opencl.CL10;
import org.lwjgl.opencl.CLContextCallbackI;
import org.lwjgl.system.Platform;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.wicpar.sinking_simulator.engine.opencl.util.InfoUtil.checkCLError;
import static org.lwjgl.glfw.GLFWNativeGLX.glfwGetGLXContext;
import static org.lwjgl.glfw.GLFWNativeWGL.glfwGetWGLContext;
import static org.lwjgl.glfw.GLFWNativeX11.glfwGetX11Display;
import static org.lwjgl.opencl.CL10.CL_CONTEXT_PLATFORM;
import static org.lwjgl.opencl.CL10.clCreateContext;
import static org.lwjgl.opencl.KHRGLSharing.*;
import static org.lwjgl.opengl.CGL.CGLGetCurrentContext;
import static org.lwjgl.opengl.CGL.CGLGetShareGroup;
import static org.lwjgl.opengl.WGL.wglGetCurrentDC;

/**
 * Created by Frederic on 28/11/2016.
 */
public class CLContext extends CLObject {

	private final long id;
	private final List<CLDevice> devices = new ArrayList<>();

	public CLContext(final PointerBuffer properties, final CLDevice device, final CLContextCallbackI pfn_notify, final long user_data) {
		this.devices.add(device);
		final IntBuffer err = BufferUtils.createIntBuffer(1);
		id = clCreateContext(properties, device.getId(), pfn_notify, user_data, err);
		checkCLError(err);
	}

	public CLContext(final PointerBuffer properties, final List<CLDevice> devices, final CLContextCallbackI pfn_notify, final long user_data) {
		this.devices.addAll(devices);
		final IntBuffer err = BufferUtils.createIntBuffer(1);
		final PointerBuffer devicePtrs = PointerBuffer.allocateDirect(devices.size());
		devices.forEach(device -> devicePtrs.put(device.getId()));
		devicePtrs.flip();
		id = clCreateContext(properties, devicePtrs, pfn_notify, user_data, err);
		checkCLError(err);
	}

	public long getId() {
		return id;
	}

	public List<CLDevice> getDevices() {
		return devices;
	}

	public static CLContext fromLWJGLContext(final long window, final CLContextCallbackI pfn_notify, final long user_data) {

		final PointerBuffer ctxProps = BufferUtils.createPointerBuffer(7);
		final CLPlatform platform = OpenCL.getGLInteropPlatform();
		if (platform == null)
			throw new RuntimeException("No OpenGL-OpenCL interoperability available");
		switch (org.lwjgl.system.Platform.get()) {
			case WINDOWS:
				ctxProps.put(CL_GL_CONTEXT_KHR)
				        .put(glfwGetWGLContext(window))
				        .put(CL_WGL_HDC_KHR)
				        .put(wglGetCurrentDC())
						.put(CL_CONTEXT_PLATFORM)
						.put(platform.getId())
						.put(0)
						.flip();
				break;
			case LINUX:
				ctxProps.put(CL_GL_CONTEXT_KHR)
				        .put(glfwGetGLXContext(window))
				        .put(CL_GLX_DISPLAY_KHR)
				        .put(glfwGetX11Display())
						.put(CL_CONTEXT_PLATFORM)
						.put(platform.getId())
						.put(0)
						.flip();
				break;
			case MACOSX:
				ctxProps.put(APPLEGLSharing.CL_CONTEXT_PROPERTY_USE_CGL_SHAREGROUP_APPLE)
				        .put(CGLGetShareGroup(CGLGetCurrentContext()))
						.put(CL_CONTEXT_PLATFORM)
						.put(platform.getId())
						.put(0)
						.flip();
				final CLContext context = new CLContext(ctxProps, new ArrayList<>(), pfn_notify, user_data);
				context.getDevices().addAll(platform.getGPUDevices());
				return context;
		}
		final PointerBuffer size = PointerBuffer.allocateDirect(1);
		checkCLError(clGetGLContextInfoKHR(ctxProps, CL_DEVICES_FOR_GL_CONTEXT_KHR, (PointerBuffer) null, size));

		final PointerBuffer devicePtr = PointerBuffer.allocateDirect((int) size.get(0));
		checkCLError(clGetGLContextInfoKHR(ctxProps, CL_DEVICES_FOR_GL_CONTEXT_KHR, devicePtr, null));

		final List<CLDevice> devices = CLDevice.fromIdList(devicePtr, platform);
		return new CLContext(ctxProps, devices, pfn_notify, user_data);
	}


	@Override
	protected void destroy() {
		checkCLError(CL10.clReleaseContext(id));
	}
}
