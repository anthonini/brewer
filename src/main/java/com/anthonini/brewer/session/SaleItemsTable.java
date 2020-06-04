package com.anthonini.brewer.session;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import com.anthonini.brewer.model.Beer;
import com.anthonini.brewer.model.SaleItem;

class SaleItemsTable {

	private String uuid;
	private List<SaleItem> items = new ArrayList<>();
	
	public SaleItemsTable(String uuid) {
		this.uuid = uuid;
	}

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

	public String getUuid() {
		return uuid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SaleItemsTable other = (SaleItemsTable) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} 
		
		return uuid.equals(other.uuid);
	}
	
	
}
