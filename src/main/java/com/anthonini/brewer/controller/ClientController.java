package com.anthonini.brewer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.anthonini.brewer.model.PersonType;
import com.anthonini.brewer.repository.StateRepository;

@Controller
@RequestMapping("/client")
public class ClientController {
	
	@Autowired
	private StateRepository stateRepository;

	@GetMapping("/new")
	public ModelAndView novo() {
		ModelAndView mv = new ModelAndView("client/form");
		mv.addObject("personTypes", PersonType.values());
		mv.addObject("states", stateRepository.findAllByOrderByName());
		
		return mv;
	}
}
