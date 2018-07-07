package com.anthonini.brewer.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anthonini.brewer.controller.page.PageWrapper;
import com.anthonini.brewer.model.Style;
import com.anthonini.brewer.repository.StyleRepository;
import com.anthonini.brewer.repository.filter.StyleFilter;
import com.anthonini.brewer.service.StyleService;
import com.anthonini.brewer.service.exception.StyleNameAlreadyRegisteredException;

@Controller
@RequestMapping("/style")
public class StyleController {
	
	@Autowired
	StyleService styleService;
	
	@Autowired
	StyleRepository styleRepository;

	@GetMapping("/new")
	public String form(Style style) {
		return "style/form";
	}
	
	@PostMapping("/new")
	public String save(@Valid Style style, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if(bindingResult.hasErrors()) {
			return form(style);
		}
		
		try {
			styleService.save(style);
			
			redirectAttributes.addFlashAttribute("successMessage", "Style successfully saved!");
			return "redirect:new";
		}catch (StyleNameAlreadyRegisteredException e) {
			bindingResult.rejectValue("name", e.getMessage(), e.getMessage());
			return form(style);
		}
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<?> save(@Valid @RequestBody Style style, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(bindingResult.getFieldError("name").getDefaultMessage());
		}

		styleService.save(style);
		
		return ResponseEntity.ok(style);
	}
	
	@GetMapping
	public ModelAndView list(StyleFilter styleFilter, BindingResult bindingResult,
			@PageableDefault(size = 2) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("style/list");
		
		PageWrapper<Style> pageWrapper = new PageWrapper<>(styleRepository.filter(styleFilter, pageable), httpServletRequest);
		mv.addObject("page", pageWrapper);
		
		return mv;
	}
}
