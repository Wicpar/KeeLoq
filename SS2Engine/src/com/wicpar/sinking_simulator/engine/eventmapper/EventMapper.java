package com.wicpar.sinking_simulator.engine.eventmapper;

import java.util.*;

/**
 * Created by Frederic on 20/12/2016.
 */
public class EventMapper<DTO> {

	private final Map<String, IAction<DTO>> actionLib = new HashMap<>();
	private final Map<IEventCondition<DTO>, IAction<DTO>> actionMap = new HashMap<>();
	private final Map<String, List<IEventCondition<DTO>>> conditionLib = new HashMap<>();

	/**
	 *
	 * @param name
	 * @param action
	 * @return the previous action under the name or null
	 */
	public IAction<DTO> registerAction(final String name, final IAction<DTO> action) {
		final IAction<DTO> ret = actionLib.put(name, action);
		final List<IEventCondition<DTO>> eventConditions = conditionLib.get(name);
		if (eventConditions != null) {
			eventConditions.forEach(event -> actionMap.put(event, action));
		}
		return ret;
	}

	public IAction<DTO> unregisterAction(final String name) {
		final IAction<DTO> action = actionLib.remove(name);
		actionMap.values().removeIf(val -> val.equals(action));
		return action;
	}

	/**
	 * adds a condition that will trigger the action. if that action is already triggered by another condition, it will remain so
	 * @param condition
	 * @param actionName
	 */
	public void mapCondition(final IEventCondition<DTO> condition, final String actionName) {
		final IAction<DTO> action = actionLib.get(actionName);
		final List<IEventCondition<DTO>> conditions = conditionLib.get(actionName);
		if (conditions == null) {
			final ArrayList<IEventCondition<DTO>> newConditions = new ArrayList<>(Collections.singletonList(condition));
			conditionLib.put(actionName, newConditions);
		} else {
			conditions.add(condition);
		}
		if (action != null) {
			mapAction(condition, action);
		}
	}

	/**
	 * adds a condition that will trigger the action. if that action is already triggered by another condition, it will be cleared
	 * @param condition
	 * @param actionName
	 */
	public void remapCondition(final IEventCondition<DTO> condition, final String actionName) {
		final IAction<DTO> action = actionLib.get(actionName);
		final List<IEventCondition<DTO>> conditions = conditionLib.get(actionName);
		if (conditions == null) {
			final ArrayList<IEventCondition<DTO>> newConditions = new ArrayList<>(Collections.singletonList(condition));
			conditionLib.put(actionName, newConditions);
		} else {
			actionMap.keySet().removeAll(conditions);
			conditions.clear();
			conditions.add(condition);
		}
		if (action != null) {
			mapAction(condition, action);
		}
	}

	public void unmapCondition(final IEventCondition<DTO> condition) {
		conditionLib.values().removeIf(iEventConditions -> iEventConditions.remove(condition) && iEventConditions.size() == 0);
		actionMap.remove(condition);
	}

	private void mapAction(final IEventCondition<DTO> condition, final IAction<DTO> action) {
		actionMap.put(condition, action);
	}

	public void trigger(final DTO dto) {
		actionMap.forEach((iEvent, iActions) -> {
			if (iEvent.shouldExecute(dto)) iActions.actuate(dto);
		});
	}


}
