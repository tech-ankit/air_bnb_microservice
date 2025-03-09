package com.property.rest;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.property.entity.Country;
import com.property.entity.State;
import com.property.service.CountryService;
import com.property.service.StateService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/state")
@AllArgsConstructor
public class StateRestController {
	
	private final StateService stateService;
	

	@PostMapping(value = "/add")
	public State addState(@RequestBody State state) {
		return stateService.createState(state);
	}
	
	@GetMapping(value = "/all-state")
	public List<State> getAllState(){
		return stateService.allState();
	}
	
}
