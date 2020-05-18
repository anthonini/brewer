package com.anthonini.brewer.sale;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.anthonini.brewer.model.Beer;
import com.anthonini.brewer.model.SaleItem;

public class SaleItemsTable {

	private List<SaleItem> items = new ArrayList<>();

	public BigDecimal getTotalValue() {
		return items.stream()
				.map(SaleItem::getTotalValue)
				.reduce(BigDecimal::add)
				.orElse(BigDecimal.ZERO);
	}
	
	public void addItem(Beer beer, Integer amount) {
		SaleItem saleItem = new SaleItem();
		saleItem.setBeer(beer);
		saleItem.setAmount(amount);
		saleItem.setUnityValue(beer.getValue());
		
		items.add(saleItem);
	}
}
