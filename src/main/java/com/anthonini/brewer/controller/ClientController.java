package com.anthonini.brewer.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anthonini.brewer.controller.page.PageWrapper;
import com.anthonini.brewer.model.Client;
import com.anthonini.brewer.model.PersonType;
import com.anthonini.brewer.repository.ClientRepository;
import com.anthonini.brewer.repository.StateRepository;
import com.anthonini.brewer.repository.filter.ClientFilter;
import com.anthonini.brewer.service.ClientService;
import com.anthonini.brewer.service.exception.AlreadyRegisteredClientCpfCnpjException;

@Controller
@RequestMapping("/client")
public class ClientController {
	
	@Autowired
	private StateRepository stateRepository;
	
	@Autowired
	private ClientService clientService;
	
	@Autowired
	private ClientRepository clientRepository;

	@GetMapping("/new")
	public ModelAndView form(Client client) {
		ModelAndView mv = new ModelAndView("client/form");
		mv.addObject("personTypes", PersonType.values());
		mv.addObject("states", stateRepository.findAllByOrderByName());
		
		return mv;
	}
	
	@PostMapping({"/new", "/{\\d+}"})
	public ModelAndView save(@Valid Client client, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if(bindingResult.hasErrors()) {
			return form(client);
		}
		
		try {
			clientService.save(client);
			
			redirectAttributes.addFlashAttribute("successMessage", "Cliente salvo com sucesso!");		
			return new ModelAndView("redirect:new");

		}catch (AlreadyRegisteredClientCpfCnpjException e) {
			bindingResult.rejectValue("cpfCnpj", e.getMessage(), e.getMessage());
			return form(client);
		}
	}
	
	@GetMapping
	public ModelAndView list(ClientFilter clientFilter, BindingResult bindingResult,
			@PageableDefault(size = 2) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("client/list");
		
		PageWrapper<Client> pageWrapper = new PageWrapper<>(clientRepository.filter(clientFilter, pageable), httpServletRequest);
		mv.addObject("page", pageWrapper);
		
		return mv;
	}
	
	@RequestMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
	public @ResponseBody List<Client> list(String name) {
		validateNameLength(name);
		return clientRepository.findByNameStartingWithIgnoreCase(name);
	}
	
	@GetMapping("/{id}")
	public ModelAndView update(@PathVariable("id") Client client) {
		ModelAndView mv = form(client);
		mv.addObject(client);
		
		return mv;
	}

	
	private void validateNameLength(String name) {
		if(StringUtils.isEmpty(name) || name.length() < 3) {
			throw new IllegalArgumentException();
		}
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Void> handleIllegalArgumentException(IllegalArgumentException e) {
		return ResponseEntity.badRequest().build();
	}
}
