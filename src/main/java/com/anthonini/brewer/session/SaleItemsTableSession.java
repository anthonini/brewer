package com.anthonini.brewer.session;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.anthonini.brewer.model.Beer;
import com.anthonini.brewer.model.SaleItem;

@Component
@SessionScope
public class SaleItemsTableSession {

	private Set<SaleItemsTable> tables = new HashSet<>();

	public void addItem(String uuid, Beer beer, int quantity) {
		SaleItemsTable table = getSaleItemsTableByUuid(uuid);
		table.addItem(beer, quantity);
		tables.add(table);
	}

	public void changeQuantity(String uuid, Beer beer, Integer quantity) {
		SaleItemsTable table = getSaleItemsTableByUuid(uuid);
		table.changeQuantity(beer, quantity);
	}

	public void deleteItem(String uuid, Beer beer) {
		SaleItemsTable table = getSaleItemsTableByUuid(uuid);
		table.deleteItem(beer);
	}

	public List<SaleItem> getItems(String uuid) {
		return getSaleItemsTableByUuid(uuid).getItems();
	}
	
	public Object getTotalValue(String uuid) {
		return getSaleItemsTableByUuid(uuid).getTotalValue();
	}
	
	private SaleItemsTable getSaleItemsTableByUuid(String uuid) {
		return tables.stream()
		.filter(t -> t.getUuid().equals(uuid))
		.findAny()
		.orElse(new SaleItemsTable(uuid));
	}
}
