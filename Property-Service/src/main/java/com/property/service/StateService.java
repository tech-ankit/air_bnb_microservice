package com.property.service;


import java.util.List;

import com.property.entity.State;

public interface StateService {

	State createState(State state);
	List<State> allState();
}
