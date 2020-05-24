package com.anthonini.brewer.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.anthonini.brewer.model.Beer;
import com.anthonini.brewer.repository.BeerRepository;
import com.anthonini.brewer.session.SaleItemsTableSession;

@Controller
@RequestMapping("/sale")
public class SaleController {
	
	@Autowired
	private BeerRepository beerRepository;
	
	@Autowired
	private SaleItemsTableSession saleItemsTableSession;

	@GetMapping("/new")
	public ModelAndView form() {
		ModelAndView mv = new ModelAndView("sale/form");
		mv.addObject("uuid", UUID.randomUUID());
		return mv;
	}
	
	@PostMapping("/item")
	public ModelAndView addItem(Long beerId, String uuid) {
		Beer beer = beerRepository.findOne(beerId);
		saleItemsTableSession.addItem(uuid, beer, 1);
		
		return mvSaleItemsTable(uuid);
	}
	
	@PutMapping("/item/{beerId}")
	public ModelAndView changeItemQuantity(@PathVariable Long beerId, Integer quantity, String uuid) {
		Beer beer = new Beer();
		beer.setId(beerId);
		saleItemsTableSession.changeQuantity(uuid, beer, quantity);
		
		return mvSaleItemsTable(uuid);
	}
	
	@DeleteMapping("/item/{uuid}/{beerId}")
	public ModelAndView deleteItem(@PathVariable String uuid, @PathVariable Long beerId) {
		Beer beer = new Beer();
		beer.setId(beerId);
		saleItemsTableSession.deleteItem(uuid, beer);
		
		return mvSaleItemsTable(uuid);
	}

	private ModelAndView mvSaleItemsTable(String uuid) {
		ModelAndView mv = new ModelAndView("sale/SaleItemsTable");
		mv.addObject("items", saleItemsTableSession.getItems(uuid));
		
		return mv;
	}
}
