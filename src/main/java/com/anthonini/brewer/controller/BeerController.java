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

import com.anthonini.brewer.model.Beer;
import com.anthonini.brewer.model.Flavor;
import com.anthonini.brewer.model.Origin;
import com.anthonini.brewer.repository.BeerRepository;
import com.anthonini.brewer.repository.StyleRepository;
import com.anthonini.brewer.service.BeerService;

@Controller
@RequestMapping("/beer")
public class BeerController {
	
	@Autowired
	private StyleRepository styleRepository;
	
	@Autowired
	private BeerService beerService;
	
	@Autowired
	private BeerRepository beerRepository;

	@GetMapping("/new")
	public ModelAndView form(Beer beer) {
		ModelAndView mv = new ModelAndView("beer/form");
		mv.addObject("flavors", Flavor.values());
		mv.addObject("styles", styleRepository.findAll());
		mv.addObject("origins", Origin.values());
		
		return mv;
	}
	
	@PostMapping("/new")
	public ModelAndView create(@Valid Beer beer, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if(bindingResult.hasErrors()) {
			return form(beer);
		}
		
		redirectAttributes.addFlashAttribute("successMessage", "Beer successfully saved!");
		beerService.save(beer);
		
		return new ModelAndView("redirect:new");
	}
	
	@GetMapping
	public ModelAndView list() {
		ModelAndView mv = new ModelAndView("beer/list");
		mv.addObject("flavors", Flavor.values());
		mv.addObject("styles", styleRepository.findAll());
		mv.addObject("origins", Origin.values());
		mv.addObject("beers", beerRepository.findAll());
		
		return mv;
	}
}
