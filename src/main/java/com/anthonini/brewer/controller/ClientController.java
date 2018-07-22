package com.anthonini.brewer.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anthonini.brewer.model.Client;
import com.anthonini.brewer.model.PersonType;
import com.anthonini.brewer.repository.StateRepository;
import com.anthonini.brewer.service.ClientService;
import com.anthonini.brewer.service.exception.AlreadyRegisteredClientCpfCnpjException;

@Controller
@RequestMapping("/client")
public class ClientController {
	
	@Autowired
	private StateRepository stateRepository;
	
	@Autowired
	private ClientService clientService;

	@GetMapping("/new")
	public ModelAndView form(Client client) {
		ModelAndView mv = new ModelAndView("client/form");
		mv.addObject("personTypes", PersonType.values());
		mv.addObject("states", stateRepository.findAllByOrderByName());
		
		return mv;
	}
	
	@PostMapping("/new")
	public ModelAndView save(@Valid Client client, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if(bindingResult.hasErrors()) {
			return form(client);
		}
		
		try {
			clientService.save(client);
			
			redirectAttributes.addFlashAttribute("successMessage", "Client successfully saved!");		
			return new ModelAndView("redirect:new");

		}catch (AlreadyRegisteredClientCpfCnpjException e) {
			bindingResult.rejectValue("cpfCnpj", e.getMessage(), e.getMessage());
			return form(client);
		}
	}
}
