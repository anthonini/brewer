package com.anthonini.brewer.dto;

import java.math.BigDecimal;

public class StockItemsValue {

	private BigDecimal value;
	private Long itemsTotal;
	
	public StockItemsValue() {
	}
	
	public StockItemsValue(BigDecimal value, Long itemsTotal) {
		this.value = value;
		this.itemsTotal = itemsTotal;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Long getItemsTotal() {
		return itemsTotal;
	}

	public void setItemsTotal(Long itemsTotal) {
		this.itemsTotal = itemsTotal;
	}
}
