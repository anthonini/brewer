package com.anthonini.brewer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.anthonini.brewer.model.Beer;
import com.anthonini.brewer.repository.BeerRepository;
import com.anthonini.brewer.session.SaleItemsTable;

@Controller
@RequestMapping("/sale")
public class SaleController {
	
	@Autowired
	private BeerRepository beerRepository;
	
	@Autowired
	private SaleItemsTable saleItemsTable;

	@GetMapping("/new")
	public String form() {
		return "sale/form";
	}
	
	@PostMapping("/item")
	public ModelAndView addItem(Long beerId) {
		Beer beer = beerRepository.findOne(beerId);
		saleItemsTable.addItem(beer, 1);
		
		ModelAndView mv = new ModelAndView("sale/SaleItemsTable");
		mv.addObject("items", saleItemsTable.getItems());
		
		return mv;
	}
	
	@PutMapping("/item/{beerId}")
	public ModelAndView changeItemQuantity(@PathVariable Long beerId, Integer quantity) {
		Beer beer = new Beer();
		beer.setId(beerId);
		saleItemsTable.changeQuantity(beer, quantity);
		
		ModelAndView mv = new ModelAndView("sale/SaleItemsTable");
		mv.addObject("items", saleItemsTable.getItems());
		
		return mv;
	}
}
