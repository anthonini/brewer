package com.anthonini.brewer.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.anthonini.brewer.model.Client;
import com.anthonini.brewer.model.PersonType;
import com.anthonini.brewer.repository.StateRepository;

@Controller
@RequestMapping("/client")
public class ClientController {
	
	@Autowired
	private StateRepository stateRepository;

	@GetMapping("/new")
	public ModelAndView form(Client client) {
		ModelAndView mv = new ModelAndView("client/form");
		mv.addObject("personTypes", PersonType.values());
		mv.addObject("states", stateRepository.findAllByOrderByName());
		
		return mv;
	}
	
	@PostMapping("/new")
	public ModelAndView save(@Valid Client client, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return form(client);
		}
		
		ModelAndView mv = new ModelAndView("redirect:new");		
		return mv;
	}
}
