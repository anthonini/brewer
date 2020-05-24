package com.anthonini.brewer.session;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.anthonini.brewer.model.Beer;
import com.anthonini.brewer.model.SaleItem;

@Component
@SessionScope
public class SaleItemsTable {

	private List<SaleItem> items = new ArrayList<>();

	public BigDecimal getTotalValue() {
		return items.stream()
				.map(SaleItem::getTotalValue)
				.reduce(BigDecimal::add)
				.orElse(BigDecimal.ZERO);
	}
	
	public void addItem(Beer beer, Integer quantity) {
		Optional<SaleItem> saleItemOptional = findItemByBeer(beer);
		
		SaleItem saleItem = saleItemOptional.orElse(new SaleItem()); 
		if(saleItemOptional.isPresent()) {
			saleItem.setQuantity(saleItem.getQuantity() + quantity);
		} else {
			saleItem.setBeer(beer);
			saleItem.setQuantity(quantity);
			saleItem.setUnityValue(beer.getValue());
			items.add(0, saleItem);
		}
	}

	private Optional<SaleItem> findItemByBeer(Beer beer) {
		return items.stream()
			.filter(i -> i.getBeer().equals(beer))
			.findAny();
	}
	
	public void changeQuantity(Beer beer, Integer quantity) {
		SaleItem saleItem = findItemByBeer(beer).get();
		saleItem.setQuantity(quantity);
	}
	
	public void deleteItem(Beer beer) {
		int index = IntStream.range(0, items.size())
				.filter(i -> items.get(i).getBeer().equals(beer))
				.findAny().getAsInt();
		
		items.remove(index);
	}

	public int total() {
		return items.size();
	}

	public List<SaleItem> getItems() {
		return items;
	}
}
