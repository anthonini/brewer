package com.anthonini.brewer.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anthonini.brewer.controller.page.PageWrapper;
import com.anthonini.brewer.controller.validator.SaleValidator;
import com.anthonini.brewer.model.Beer;
import com.anthonini.brewer.model.Sale;
import com.anthonini.brewer.model.SaleStatus;
import com.anthonini.brewer.repository.BeerRepository;
import com.anthonini.brewer.repository.SaleRepository;
import com.anthonini.brewer.repository.filter.SaleFilter;
import com.anthonini.brewer.security.SystemUser;
import com.anthonini.brewer.service.SaleService;
import com.anthonini.brewer.session.SaleItemsTableSession;

@Controller
@RequestMapping("/sale")
public class SaleController {
	
	@Autowired
	private BeerRepository beerRepository;
	
	@Autowired
	private SaleRepository saleRepository;
	
	@Autowired
	private SaleItemsTableSession saleItemsTableSession;
	
	@Autowired
	private SaleService saleService;
	
	@Autowired
	private SaleValidator saleValidator;
	
	@InitBinder("sale")
	public void iniializateValidator(WebDataBinder binder) {
		binder.setValidator(saleValidator);
	}

	@GetMapping("/new")
	public ModelAndView form(Sale sale) {
		ModelAndView mv = new ModelAndView("sale/form");
		
		if(StringUtils.isEmpty(sale.getUuid())) {
			sale.setUuid(UUID.randomUUID().toString());
		}
		
		mv.addObject("items", sale.getItems());
		mv.addObject("shippingValue", sale.getShippingValue());
		mv.addObject("discountValue", sale.getDiscountValue());
		mv.addObject("totalValue", saleItemsTableSession.getTotalValue(sale.getUuid()));
		
		return mv;
	}
	
	@PostMapping(value = "/new", params = "save")
	public ModelAndView save(Sale sale, BindingResult bindingResult, RedirectAttributes redirectAttributes, @AuthenticationPrincipal SystemUser systemUser) {
		validateSale(sale, bindingResult);
		if(bindingResult.hasErrors()) {
			return form(sale);
		}
		
		sale.setUser(systemUser.getUser());
		
		saleService.save(sale);
		redirectAttributes.addFlashAttribute("successMessage", "Venda salva com sucesso!");
		return new ModelAndView("redirect:new");
	}
	
	@PostMapping(value = "/new", params = "emmit")
	public ModelAndView emmit(Sale sale, BindingResult bindingResult, RedirectAttributes redirectAttributes, @AuthenticationPrincipal SystemUser systemUser) {
		validateSale(sale, bindingResult);
		if(bindingResult.hasErrors()) {
			return form(sale);
		}
		
		sale.setUser(systemUser.getUser());
		
		saleService.emmit(sale);
		redirectAttributes.addFlashAttribute("successMessage", "Venda emitida com sucesso!");
		return new ModelAndView("redirect:new");
	}
	
	@PostMapping(value = "/new", params = "sendEmail")
	public ModelAndView sendEmail(Sale sale, BindingResult bindingResult, RedirectAttributes redirectAttributes, @AuthenticationPrincipal SystemUser systemUser) {
		validateSale(sale, bindingResult);
		if(bindingResult.hasErrors()) {
			return form(sale);
		}
		
		sale.setUser(systemUser.getUser());
		
		saleService.save(sale);
		redirectAttributes.addFlashAttribute("successMessage", "Venda salva e email enviado com sucesso!");
		return new ModelAndView("redirect:new");
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
	
	@GetMapping
	public ModelAndView list(SaleFilter saleFilter, @PageableDefault(size = 3) Pageable pageable,
			HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("sale/list");
		mv.addObject("allStatus", SaleStatus.values());
		
		PageWrapper<Sale> pageWrapper = new PageWrapper<>(saleRepository.filter(saleFilter, pageable), httpServletRequest);
		mv.addObject("page", pageWrapper);
		
		return mv;
	}

	private ModelAndView mvSaleItemsTable(String uuid) {
		ModelAndView mv = new ModelAndView("sale/SaleItemsTable");
		mv.addObject("items", saleItemsTableSession.getItems(uuid));
		mv.addObject("totalValue", saleItemsTableSession.getTotalValue(uuid));
		
		return mv;
	}
	
	private void validateSale(Sale sale, BindingResult bindingResult) {
		sale.addItems(saleItemsTableSession.getItems(sale.getUuid()));
		sale.calculateTotalValue();
		
		saleValidator.validate(sale, bindingResult);
	}
}
