package com.wicpar.sinking_simulator.engine.opencl.sync;

import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.CL10;

import static com.wicpar.sinking_simulator.engine.opencl.util.InfoUtil.checkCLError;

/**
 * Created by Frederic on 31/01/2017.
 */
public class CLEventList extends CLEvent {

	public CLEventList(final int capacity) {
		super(PointerBuffer.allocateDirect(capacity));
	}

	public CLEvent getEvent(final int index) {
		if (index < eventAddr.capacity()) {
			return new CLEvent(eventAddr.getPointerBuffer(index, 1));
		} else
			return null;
	}

	public void awaitCompletion() {
		checkCLError(CL10.clWaitForEvents(eventAddr));
	}
}
