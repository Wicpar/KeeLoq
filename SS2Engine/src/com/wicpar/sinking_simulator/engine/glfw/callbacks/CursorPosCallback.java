package com.wicpar.sinking_simulator.engine.glfw.callbacks;

import com.wicpar.sinking_simulator.engine.glfw.Window;

/**
 * Created by Frederic on 06/12/2016.
 */
public interface CursorPosCallback {
	void invoke(Window window, double xpos, double ypos);
}
