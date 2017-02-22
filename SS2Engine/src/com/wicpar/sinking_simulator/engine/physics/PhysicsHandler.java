package com.wicpar.sinking_simulator.engine.physics;

import java.util.LinkedList;

/**
 * Created by Frederic on 08/01/2017.
 */
public class PhysicsHandler {

	private long tick;
	private long nanoTimeFromStart = 0;
	private final LinkedList<IPhysical> physicals = new LinkedList<>();

	public PhysicsHandler(final long tickNano) {
		tick = tickNano;
	}

	public void setTickLength(final long tickNano) {
		tick = tickNano;
	}

	public boolean addPhysical(final IPhysical physical) {
		return physicals.add(physical);
	}

	public boolean removePhysical(final IPhysical physical) {
		return physicals.remove(physical);
	}

	public void tick() {
		nanoTimeFromStart += tick;
		physicals.forEach(physical -> physical.update(tick, nanoTimeFromStart));
	}

}
