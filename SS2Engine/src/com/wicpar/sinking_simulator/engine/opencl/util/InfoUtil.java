/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package com.wicpar.sinking_simulator.engine.opencl.util;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;

import static org.lwjgl.opencl.CL10.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.memASCII;
import static org.lwjgl.system.MemoryUtil.memUTF8;

/**
 * OpenCL object info utilities.
 */
public final class InfoUtil {

	static String getPlatformInfoStringASCII(final long cl_platform_id, final int param_name) {
		try (MemoryStack stack = stackPush()) {
			final PointerBuffer pp = stack.mallocPointer(1);
			checkCLError(clGetPlatformInfo(cl_platform_id, param_name, (ByteBuffer) null, pp));
			final int bytes = (int) pp.get(0);

			final ByteBuffer buffer = stack.malloc(bytes);
			checkCLError(clGetPlatformInfo(cl_platform_id, param_name, buffer, null));

			return memASCII(buffer, bytes - 1);
		}
	}

	public static String getPlatformInfoStringUTF8(final long cl_platform_id, final int param_name) {
		try (MemoryStack stack = stackPush()) {
			final PointerBuffer pp = stack.mallocPointer(1);
			checkCLError(clGetPlatformInfo(cl_platform_id, param_name, (ByteBuffer) null, pp));
			final int bytes = (int) pp.get(0);

			final ByteBuffer buffer = stack.malloc(bytes);
			checkCLError(clGetPlatformInfo(cl_platform_id, param_name, buffer, null));

			return memUTF8(buffer, bytes - 1);
		}
	}

	public static int getDeviceInfoInt(final long cl_device_id, final int param_name) {
		try (MemoryStack stack = stackPush()) {
			final IntBuffer pl = stack.mallocInt(1);
			checkCLError(clGetDeviceInfo(cl_device_id, param_name, pl, null));
			return pl.get(0);
		}
	}

	public static long getDeviceInfoLong(final long cl_device_id, final int param_name) {
		try (MemoryStack stack = stackPush()) {
			final LongBuffer pl = stack.mallocLong(1);
			checkCLError(clGetDeviceInfo(cl_device_id, param_name, pl, null));
			return pl.get(0);
		}
	}

	public static long getDeviceInfoPointer(final long cl_device_id, final int param_name) {
		try (MemoryStack stack = stackPush()) {
			final PointerBuffer pp = stack.mallocPointer(1);
			checkCLError(clGetDeviceInfo(cl_device_id, param_name, pp, null));
			return pp.get(0);
		}
	}

	public static String getDeviceInfoStringUTF8(final long cl_device_id, final int param_name) {
		try (MemoryStack stack = stackPush()) {
			final PointerBuffer pp = stack.mallocPointer(1);
			checkCLError(clGetDeviceInfo(cl_device_id, param_name, (ByteBuffer) null, pp));
			final int bytes = (int) pp.get(0);

			final ByteBuffer buffer = stack.malloc(bytes);
			checkCLError(clGetDeviceInfo(cl_device_id, param_name, buffer, null));

			return memUTF8(buffer, bytes - 1);
		}
	}

	static long getMemObjectInfoPointer(final long cl_mem, final int param_name) {
		try (MemoryStack stack = stackPush()) {
			final PointerBuffer pp = stack.mallocPointer(1);
			checkCLError(clGetMemObjectInfo(cl_mem, param_name, pp, null));
			return pp.get(0);
		}
	}

	static long getMemObjectInfoInt(final long cl_mem, final int param_name) {
		try (MemoryStack stack = stackPush()) {
			final IntBuffer pi = stack.mallocInt(1);
			checkCLError(clGetMemObjectInfo(cl_mem, param_name, pi, null));
			return pi.get(0);
		}
	}

	static int getProgramBuildInfoInt(final long cl_program_id, final long cl_device_id, final int param_name) {
		try (MemoryStack stack = stackPush()) {
			final IntBuffer pl = stack.mallocInt(1);
			checkCLError(clGetProgramBuildInfo(cl_program_id, cl_device_id, param_name, pl, null));
			return pl.get(0);
		}
	}

	static String getProgramBuildInfoStringASCII(final long cl_program_id, final long cl_device_id, final int param_name) {
		try (MemoryStack stack = stackPush()) {
			final PointerBuffer pp = stack.mallocPointer(1);
			checkCLError(clGetProgramBuildInfo(cl_program_id, cl_device_id, param_name, (ByteBuffer) null, pp));
			final int bytes = (int) pp.get(0);

			final ByteBuffer buffer = stack.malloc(bytes);
			checkCLError(clGetProgramBuildInfo(cl_program_id, cl_device_id, param_name, buffer, null));

			return memASCII(buffer, bytes - 1);
		}
	}

	public static void checkCLError(final IntBuffer errcode) {
		checkCLError(errcode.get(errcode.position()));
	}

	public static void checkCLError(final int errcode) {
		if (errcode != CL_SUCCESS)
			throw new RuntimeException(String.format("OpenCL error [0x%S]", clewErrorString(errcode)));
	}

	private static String clewErrorString(final int error) {
		final String strings[] =
				{
						// Error Codes
						"CL_SUCCESS"                                  //   0
						, "CL_DEVICE_NOT_FOUND"                         //  -1
						, "CL_DEVICE_NOT_AVAILABLE"                     //  -2
						, "CL_COMPILER_NOT_AVAILABLE"                   //  -3
						, "CL_MEM_OBJECT_ALLOCATION_FAILURE"            //  -4
						, "CL_OUT_OF_RESOURCES"                         //  -5
						, "CL_OUT_OF_HOST_MEMORY"                       //  -6
						, "CL_PROFILING_INFO_NOT_AVAILABLE"             //  -7
						, "CL_MEM_COPY_OVERLAP"                         //  -8
						, "CL_IMAGE_FORMAT_MISMATCH"                    //  -9
						, "CL_IMAGE_FORMAT_NOT_SUPPORTED"               //  -10
						, "CL_BUILD_PROGRAM_FAILURE"                    //  -11
						, "CL_MAP_FAILURE"                              //  -12

						, ""    //  -13
						, ""    //  -14
						, ""    //  -15
						, ""    //  -16
						, ""    //  -17
						, ""    //  -18
						, ""    //  -19

						, ""    //  -20
						, ""    //  -21
						, ""    //  -22
						, ""    //  -23
						, ""    //  -24
						, ""    //  -25
						, ""    //  -26
						, ""    //  -27
						, ""    //  -28
						, ""    //  -29

						, "CL_INVALID_VALUE"                            //  -30
						, "CL_INVALID_DEVICE_TYPE"                      //  -31
						, "CL_INVALID_PLATFORM"                         //  -32
						, "CL_INVALID_DEVICE"                           //  -33
						, "CL_INVALID_CONTEXT"                          //  -34
						, "CL_INVALID_QUEUE_PROPERTIES"                 //  -35
						, "CL_INVALID_COMMAND_QUEUE"                    //  -36
						, "CL_INVALID_HOST_PTR"                         //  -37
						, "CL_INVALID_MEM_OBJECT"                       //  -38
						, "CL_INVALID_IMAGE_FORMAT_DESCRIPTOR"          //  -39
						, "CL_INVALID_IMAGE_SIZE"                       //  -40
						, "CL_INVALID_SAMPLER"                          //  -41
						, "CL_INVALID_BINARY"                           //  -42
						, "CL_INVALID_BUILD_OPTIONS"                    //  -43
						, "CL_INVALID_PROGRAM"                          //  -44
						, "CL_INVALID_PROGRAM_EXECUTABLE"               //  -45
						, "CL_INVALID_KERNEL_NAME"                      //  -46
						, "CL_INVALID_KERNEL_DEFINITION"                //  -47
						, "CL_INVALID_KERNEL"                           //  -48
						, "CL_INVALID_ARG_INDEX"                        //  -49
						, "CL_INVALID_ARG_VALUE"                        //  -50
						, "CL_INVALID_ARG_SIZE"                         //  -51
						, "CL_INVALID_KERNEL_ARGS"                      //  -52
						, "CL_INVALID_WORK_DIMENSION"                   //  -53
						, "CL_INVALID_WORK_GROUP_SIZE"                  //  -54
						, "CL_INVALID_WORK_ITEM_SIZE"                   //  -55
						, "CL_INVALID_GLOBAL_OFFSET"                    //  -56
						, "CL_INVALID_EVENT_WAIT_LIST"                  //  -57
						, "CL_INVALID_EVENT"                            //  -58
						, "CL_INVALID_OPERATION"                        //  -59
						, "CL_INVALID_GL_OBJECT"                        //  -60
						, "CL_INVALID_BUFFER_SIZE"                      //  -61
						, "CL_INVALID_MIP_LEVEL"                        //  -62
						, "CL_INVALID_GLOBAL_WORK_SIZE"                 //  -63
						, "CL_UNKNOWN_ERROR_CODE"
				};

		if (error >= -63 && error <= 0)
			return strings[-error];
		else
			return strings[64];
	}

}