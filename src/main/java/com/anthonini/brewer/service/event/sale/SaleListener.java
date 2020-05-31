package com.anthonini.brewer.service.event.sale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.anthonini.brewer.model.Beer;
import com.anthonini.brewer.model.SaleItem;
import com.anthonini.brewer.repository.BeerRepository;

@Component
public class SaleListener {

	@Autowired
	private BeerRepository beerRepository;
	
	@EventListener
	public void emmitedSale(SaleEvent saleEvent) {
		for (SaleItem item : saleEvent.getSale().getItems()) {
			Beer beer = beerRepository.findOne(item.getBeer().getId());
			beer.setStockQuantity(beer.getStockQuantity() - item.getQuantity());
			beerRepository.save(beer);
		}
	}
}
