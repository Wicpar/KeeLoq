package com.wicpar.sinking_simulator.engine.eventmapper;

/**
 * Created by Frederic on 22/12/2016.
 */
public interface IEventCondition<T> {
	boolean shouldExecute(final T dto);
}
