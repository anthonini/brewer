package com.anthonini.brewer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.anthonini.brewer.model.City;
import com.anthonini.brewer.repository.CityRepository;

@Controller
@RequestMapping("/city")
public class CityController {

	@Autowired
	private CityRepository cityRepository;
	
	@RequestMapping("/new")
	public String form() {
		return "city/form";
	}
	
	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<City> listByStateId(@RequestParam(name = "state", defaultValue = "-1") Long stateId){
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {}
		return cityRepository.findByStateIdOrderByName(stateId);
	}
}
