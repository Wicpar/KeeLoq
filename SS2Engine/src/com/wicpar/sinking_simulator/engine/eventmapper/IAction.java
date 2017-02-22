package com.wicpar.sinking_simulator.engine.eventmapper;

/**
 * Created by Frederic on 20/12/2016.
 */
public interface IAction<T> {
	void actuate(T dto);
}
