package com.wicpar.sinking_simulator.engine.core.scenes;

import com.wicpar.sinking_simulator.engine.glfw.Window;
import com.wicpar.sinking_simulator.engine.physics.IPhysical;
import com.wicpar.sinking_simulator.engine.physics.PhysicsHandler;

/**
 * Created by Frederic on 09/01/2017.
 */
public abstract class PhysicsScene extends DrawingScene {

	private final PhysicsHandler physicsHandler;
	private final long physicsUpdatesPerFrame;
	private double speed = 1;

	public PhysicsScene(final Window window, final long physicsUpdatesPerFrame) {
		super(window);
		this.physicsUpdatesPerFrame = physicsUpdatesPerFrame;
		physicsHandler = new PhysicsHandler(1000000000 / window.getRefreshRate() / physicsUpdatesPerFrame);
	}

	public void setSpeed(final double speed) {
		this.speed = speed;
	}

	@Override
	public void addComponent(final Object o) {
		super.addComponent(o);
		if (o instanceof IPhysical)
			physicsHandler.addPhysical((IPhysical) o);
	}

	@Override
	public void removeComponent(final Object o) {
		super.removeComponent(o);
		if (o instanceof IPhysical)
			physicsHandler.removePhysical((IPhysical) o);
	}

	@Override
	public boolean loop() {
		final boolean shouldClose = super.loop();
		if (!shouldClose) {
			final int iter = (int) (physicsUpdatesPerFrame * speed);
			for (int i = 0; i < iter; i++) {
				physicsHandler.tick();
			}
		}
		return shouldClose;
	}
}
