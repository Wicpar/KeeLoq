package com.wicpar.sinking_simulator.engine.opencl.sync;

import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.CL10;

import static com.wicpar.sinking_simulator.engine.opencl.util.InfoUtil.checkCLError;

/**
 * Created by Frederic on 31/01/2017.
 */
public class CLEvent {
	protected final PointerBuffer eventAddr;

	CLEvent(final PointerBuffer eventAddr) {
		this.eventAddr = eventAddr;
	}

	public CLEvent() {
		this.eventAddr = PointerBuffer.allocateDirect(1);
	}

	public PointerBuffer getAddr() {
		return eventAddr;
	}

	public void awaitCompletion() {
		checkCLError(CL10.clWaitForEvents(eventAddr));
	}
}
