package com.anthonini.brewer.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.AccessDeniedException;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anthonini.brewer.controller.page.PageWrapper;
import com.anthonini.brewer.controller.validator.SaleValidator;
import com.anthonini.brewer.dto.MonthSale;
import com.anthonini.brewer.mail.Mailer;
import com.anthonini.brewer.model.Beer;
import com.anthonini.brewer.model.Sale;
import com.anthonini.brewer.model.SaleItem;
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
	
	@Autowired
	private Mailer mailer;
	
	@InitBinder("sale")
	public void iniializateValidator(WebDataBinder binder) {
		binder.setValidator(saleValidator);
	}

	@GetMapping("/new")
	public ModelAndView form(Sale sale) {
		ModelAndView mv = new ModelAndView("sale/form");
		
		setUuid(sale);
		
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
		
		sale = saleService.save(sale);
		mailer.send(sale);
		
		redirectAttributes.addFlashAttribute("successMessage", String.format("Venda nº %d salva com sucesso e e-mail enviado!", sale.getId()));
		return new ModelAndView("redirect:new");
	}
	
	@PostMapping(value = "/new", params = "cancel")
	public ModelAndView cancel(Sale sale, BindingResult bindingResult, RedirectAttributes redirectAttributes, @AuthenticationPrincipal SystemUser systemUser) {
		try {
			saleService.cancel(sale);
		} catch (AccessDeniedException e) {
			return new ModelAndView("/403");
		}
		
		redirectAttributes.addFlashAttribute("successMessage", "Venda cancelada com sucesso");
		return new ModelAndView("redirect:/sale/"+sale.getId());
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
	public ModelAndView list(SaleFilter saleFilter, @PageableDefault(size = 10) Pageable pageable,
			HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("sale/list");
		mv.addObject("allStatus", SaleStatus.values());
		
		PageWrapper<Sale> pageWrapper = new PageWrapper<>(saleRepository.filter(saleFilter, pageable), httpServletRequest);
		mv.addObject("page", pageWrapper);
		
		return mv;
	}
	
	@GetMapping("/{id}")
	public ModelAndView update(@PathVariable Long id) {
		Sale sale = saleRepository.findWithItems(id);
		
		setUuid(sale);
		for(SaleItem saleItem : sale.getItems()) {
			saleItemsTableSession.addItem(sale.getUuid(), saleItem.getBeer(), saleItem.getQuantity());
		}
		
		ModelAndView mv = form(sale);
		mv.addObject(sale);
		
		return mv;
	}
	
	@GetMapping("/totalByMonth")
	public @ResponseBody List<MonthSale> listTotalByMonth() {
		return saleRepository.totalByMonth();
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
	
	private void setUuid(Sale sale) {
		if(StringUtils.isEmpty(sale.getUuid())) {
			sale.setUuid(UUID.randomUUID().toString());
		}
	}
}
