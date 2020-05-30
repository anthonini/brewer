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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anthonini.brewer.controller.page.PageWrapper;
import com.anthonini.brewer.dto.BeerDTO;
import com.anthonini.brewer.model.Beer;
import com.anthonini.brewer.model.Flavor;
import com.anthonini.brewer.model.Origin;
import com.anthonini.brewer.repository.BeerRepository;
import com.anthonini.brewer.repository.StyleRepository;
import com.anthonini.brewer.repository.filter.BeerFilter;
import com.anthonini.brewer.service.BeerService;
import com.anthonini.brewer.service.exception.NotPossibleDeleteEntityException;

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
	
	@PostMapping({"/new", "/{\\d+}"})
	public ModelAndView save(@Valid Beer beer, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if(bindingResult.hasErrors()) {
			return form(beer);
		}
		
		redirectAttributes.addFlashAttribute("successMessage", "Beer successfully saved!");
		beerService.save(beer);
		
		return new ModelAndView("redirect:new");
	}
	
	@GetMapping
	public ModelAndView list(BeerFilter beerFilter, BindingResult bindingResult,
			@PageableDefault(size = 2) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("beer/list");
		mv.addObject("flavors", Flavor.values());
		mv.addObject("styles", styleRepository.findAll());
		mv.addObject("origins", Origin.values());
		
		PageWrapper<Beer> pageWrapper = new PageWrapper<>(beerRepository.filter(beerFilter, pageable), httpServletRequest);
		mv.addObject("page", pageWrapper);
		
		return mv;
	}
	
	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<BeerDTO> filter(String skuOrName) {
		return beerRepository.findBySkuOrName(skuOrName);
	}
	
	@DeleteMapping("/{id}")
	public @ResponseBody ResponseEntity<?> delete(@PathVariable("id") Beer beer) {
		try {
			beerService.delete(beer);
		} catch (NotPossibleDeleteEntityException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/{id}")
	public ModelAndView update(@PathVariable("id") Beer beer) {
		ModelAndView mv = form(beer);
		mv.addObject(beer);
		
		return mv;
	}
}
