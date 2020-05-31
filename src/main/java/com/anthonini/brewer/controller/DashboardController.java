package com.anthonini.brewer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.anthonini.brewer.repository.SaleRepository;

@Controller
public class DashboardController {
	
	@Autowired
	private SaleRepository saleRepository;

	@GetMapping("/")
	public ModelAndView dashboard() {
		ModelAndView mv = new ModelAndView("Dashboard");
		
		mv.addObject("yearTotalValue", saleRepository.yearTotalValue());
		mv.addObject("monthTotalValue", saleRepository.monthTotalValue());
		mv.addObject("avgTicket", saleRepository.avgTicket());
		
		return mv;
	}
}
