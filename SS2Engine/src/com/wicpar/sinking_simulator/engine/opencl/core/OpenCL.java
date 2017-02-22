package com.wicpar.sinking_simulator.engine.opencl.core;

import com.wicpar.sinking_simulator.engine.glfw.Window;
import org.lwjgl.opencl.CLContextCallback;

import java.util.ArrayList;
import java.util.Arrays;

import static org.lwjgl.system.MemoryUtil.memUTF8;

/**
 * Created by Frederic on 28/11/2016.
 */
public class OpenCL {

	public static final CLContextCallback clContextCallback = CLContextCallback.create((errinfo, private_info, cb, user_data) -> System.out.println(String.format("cl_context_callback\n\tInfo: %s", memUTF8(errinfo))));

	public static final ArrayList<CLPlatform> platforms = new ArrayList<>();

	static {
		reload();
	}

	public static CLContext createCLContextFromLWJGLContext(final Window window) {
		printCL();
		return CLContext.fromLWJGLContext(window.getId(), clContextCallback, 0);
	}

	public static void reload() {
		platforms.clear();
		platforms.addAll(CLPlatform.getAllPlatforms());
	}

	public static void printCL() {
		System.out.println(Arrays.toString(platforms.toArray()));
	}

	public static CLPlatform getGLInteropPlatform() {
		return platforms.stream().filter(platform -> platform.getPlatformCaps().cl_khr_gl_sharing || platform.getPlatformCaps().cl_APPLE_gl_sharing).findFirst().orElse(null);
	}

}
