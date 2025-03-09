package com.property.service.impl;

import java.util.List;


import org.springframework.stereotype.Service;
import com.property.entity.State;
import com.property.repository.StateRepository;
import com.property.service.StateService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StateServiceImpl implements StateService{
	
	private final StateRepository stateRepository;
	
	@Override
	public State createState(State state) {
		return stateRepository.save(state);
	}

	@Override
	public List<State> allState() {
		return stateRepository.findAll();
	}

}
