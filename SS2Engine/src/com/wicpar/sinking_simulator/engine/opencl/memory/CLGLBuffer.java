package com.wicpar.sinking_simulator.engine.opencl.memory;

import com.wicpar.sinking_simulator.engine.opencl.core.CLCommandQueue;
import com.wicpar.sinking_simulator.engine.opencl.core.CLContext;
import com.wicpar.sinking_simulator.engine.opengl.structure.GLBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opencl.CL10GL;

import java.nio.IntBuffer;

import static com.wicpar.sinking_simulator.engine.opencl.util.InfoUtil.checkCLError;

/**
 * Created by Frederic on 08/01/2017.
 */
public class CLGLBuffer extends CLBuffer {

	private static final IntBuffer errcode_ret = BufferUtils.createIntBuffer(1);
	public CLGLBuffer(final CLContext context, final long flags, final GLBuffer glBuffer) {
		super(CL10GL.clCreateFromGLBuffer(context.getId(), flags, glBuffer.getId(), errcode_ret));
		checkCLError(errcode_ret);
	}

	public void acquire(final CLCommandQueue queue) {
		checkCLError(CL10GL.clEnqueueAcquireGLObjects(queue.getId(), id, null, null));
	}

	public void release(final CLCommandQueue queue) {
		checkCLError(CL10GL.clEnqueueReleaseGLObjects(queue.getId(), id, null, null));
	}

}
