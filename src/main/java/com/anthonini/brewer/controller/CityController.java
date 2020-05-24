package com.anthonini.brewer.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anthonini.brewer.controller.page.PageWrapper;
import com.anthonini.brewer.model.City;
import com.anthonini.brewer.repository.CityRepository;
import com.anthonini.brewer.repository.StateRepository;
import com.anthonini.brewer.repository.filter.CityFilter;
import com.anthonini.brewer.service.CityService;
import com.anthonini.brewer.service.exception.CityAlreadyRegisteredException;

@Controller
@RequestMapping("/city")
public class CityController {

	@Autowired
	private CityRepository cityRepository;
	
	@Autowired
	private StateRepository stateRepository;
	
	@Autowired
	private CityService cityService;
	
	@GetMapping("/new")
	public ModelAndView form(City city) {
		ModelAndView mv = new ModelAndView("city/form");
		mv.addObject("states", stateRepository.findAllByOrderByName());
		
		return mv;
	}
	
	@Cacheable(value = "cities", key = "#stateId")
	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<City> listByStateId(@RequestParam(name = "state", defaultValue = "-1") Long stateId){
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {}
		return cityRepository.findByStateIdOrderByName(stateId);
	}
	
	@PostMapping("/new")
	@CacheEvict(value = "cities", key = "#city.state.id", condition = "#city.hasState()")
	public ModelAndView save(@Valid City city, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if(bindingResult.hasErrors()) {
			return form(city);
		}
		
		try {
			cityService.save(city);
			redirectAttributes.addFlashAttribute("successMessage", "Cidade cadastrada com sucesso!");
			return new ModelAndView("redirect:new");
		}catch (CityAlreadyRegisteredException e) {
			bindingResult.rejectValue("name", e.getMessage(), e.getMessage());
			return form(city);
		}
	}
	
	@GetMapping
	public ModelAndView list(CityFilter cityFilter, BindingResult bindingResult,
			@PageableDefault(size = 10) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("city/list");
		mv.addObject("states", stateRepository.findAllByOrderByName());

		PageWrapper<City> pageWrapper = new PageWrapper<>(cityRepository.filter(cityFilter, pageable), httpServletRequest);
		mv.addObject("page", pageWrapper);
		
		return mv;
	}
}
