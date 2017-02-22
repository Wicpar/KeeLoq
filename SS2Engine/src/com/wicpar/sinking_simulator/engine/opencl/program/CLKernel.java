package com.wicpar.sinking_simulator.engine.opencl.program;

import com.wicpar.sinking_simulator.engine.opencl.core.CLCommandQueue;
import com.wicpar.sinking_simulator.engine.opencl.core.CLContext;
import com.wicpar.sinking_simulator.engine.opencl.core.CLDevice;
import com.wicpar.sinking_simulator.engine.opencl.memory.CLBuffer;
import com.wicpar.sinking_simulator.engine.opencl.structure.CLObject;
import com.wicpar.sinking_simulator.engine.opencl.sync.CLEvent;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.CL10;

import java.nio.IntBuffer;

import static com.wicpar.sinking_simulator.engine.opencl.util.InfoUtil.checkCLError;

/**
 * Created by Frederic on 29/11/2016.
 */
public class CLKernel extends CLObject {

	private final long id;
	private final CLProgram program;

	public CLKernel(final CLProgram program, final String mainFunctionName) {
		this.program = program;
		final IntBuffer errcode_ret = BufferUtils.createIntBuffer(1);
		id = CL10.clCreateKernel(program.getId(), mainFunctionName, errcode_ret);
		checkCLError(errcode_ret);
	}

	public CLKernel(final CLDevice device, final CLContext clContext, final String path, final String name) {
		this(new CLProgram(device, clContext, path), name);
	}

	public void setArg(final int index, final CLBuffer... arg) {
		switch (arg.length) {
			case 1:
				checkCLError(CL10.clSetKernelArg1p(id, index, arg[0].getId()));
				break;
			case 2:
				checkCLError(CL10.clSetKernelArg2p(id, index, arg[0].getId(), arg[1].getId()));
				break;
			case 3:
				checkCLError(CL10.clSetKernelArg3p(id, index, arg[0].getId(), arg[1].getId(), arg[2].getId()));
				break;
			case 4:
				checkCLError(CL10.clSetKernelArg4p(id, index, arg[0].getId(), arg[1].getId(), arg[2].getId(), arg[3].getId()));
				break;
			default:
				throw new RuntimeException("Maximum 4 vector components, " + arg.length + " given.");
		}
	}

	public void setArg(final int index, final float... arg) {
		switch (arg.length) {
			case 1:
				checkCLError(CL10.clSetKernelArg1f(id, index, arg[0]));
				break;
			case 2:
				checkCLError(CL10.clSetKernelArg2f(id, index, arg[0], arg[1]));
				break;
			case 3:
				checkCLError(CL10.clSetKernelArg3f(id, index, arg[0], arg[1], arg[2]));
				break;
			case 4:
				checkCLError(CL10.clSetKernelArg4f(id, index, arg[0], arg[1], arg[2], arg[3]));
				break;
			default:
				throw new RuntimeException("Maximum 4 vector components, " + arg.length + " given.");
		}
	}

	public void setArg(final int index, final double... arg) {
		switch (arg.length) {
			case 1:
				checkCLError(CL10.clSetKernelArg1d(id, index, arg[0]));
				break;
			case 2:
				checkCLError(CL10.clSetKernelArg2d(id, index, arg[0], arg[1]));
				break;
			case 3:
				checkCLError(CL10.clSetKernelArg3d(id, index, arg[0], arg[1], arg[2]));
				break;
			case 4:
				checkCLError(CL10.clSetKernelArg4d(id, index, arg[0], arg[1], arg[2], arg[3]));
				break;
			default:
				throw new RuntimeException("Maximum 4 vector components, " + arg.length + " given.");
		}
	}

	public void setArg(final int index, final int... arg) {
		switch (arg.length) {
			case 1:
				checkCLError(CL10.clSetKernelArg1i(id, index, arg[0]));
				break;
			case 2:
				checkCLError(CL10.clSetKernelArg2i(id, index, arg[0], arg[1]));
				break;
			case 3:
				checkCLError(CL10.clSetKernelArg3i(id, index, arg[0], arg[1], arg[2]));
				break;
			case 4:
				checkCLError(CL10.clSetKernelArg4i(id, index, arg[0], arg[1], arg[2], arg[3]));
				break;
			default:
				throw new RuntimeException("Maximum 4 vector components, " + arg.length + " given.");
		}
	}

	public void setArg(final int index, final long... arg) {
		switch (arg.length) {
			case 1:
				checkCLError(CL10.clSetKernelArg1l(id, index, arg[0]));
				break;
			case 2:
				checkCLError(CL10.clSetKernelArg2l(id, index, arg[0], arg[1]));
				break;
			case 3:
				checkCLError(CL10.clSetKernelArg3l(id, index, arg[0], arg[1], arg[2]));
				break;
			case 4:
				checkCLError(CL10.clSetKernelArg4l(id, index, arg[0], arg[1], arg[2], arg[3]));
				break;
			default:
				throw new RuntimeException("Maximum 4 vector components, " + arg.length + " given.");
		}
	}

	private final PointerBuffer localSize = PointerBuffer.allocateDirect(1);
	private final PointerBuffer globalSize = PointerBuffer.allocateDirect(1);


	public CLEvent enqueue(final CLCommandQueue queue, final long globalWorkSize, final long localWorkSize) {
		return enqueue(queue, globalWorkSize, localWorkSize, null);
	}

	public CLEvent enqueue(final CLCommandQueue queue, final long globalWorkSize, final long localWorkSize, final CLEvent await) {
		globalSize.put(0, globalWorkSize);
		localSize.put(0, localWorkSize);
		final CLEvent event = new CLEvent();
		checkCLError(CL10.clEnqueueNDRangeKernel(queue.getId(), id, 1, null, globalSize, localSize, await != null ? await.getAddr() : null, event.getAddr()));
		return event;
	}

	public CLEvent enqueue(final CLCommandQueue queue, final long globalWorkSize) {
		return enqueue(queue, globalWorkSize, null);
	}

	public CLEvent enqueue(final CLCommandQueue queue, final long globalWorkSize, final CLEvent await) {
		globalSize.put(0, globalWorkSize);
		final CLEvent event = new CLEvent();
		checkCLError(CL10.clEnqueueNDRangeKernel(queue.getId(), id, 1, null, globalSize, null, await != null ? await.getAddr() : null, event.getAddr()));
		return event;
	}

	public long getId() {
		return id;
	}

	public CLProgram getProgram() {
		return program;
	}

	@Override
	protected void destroy() {
		checkCLError(CL10.clReleaseKernel(id));
	}
}
