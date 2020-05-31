package com.anthonini.brewer.service.event.sale;

import com.anthonini.brewer.model.Sale;

public class SaleEvent {

	private Sale sale;

	public SaleEvent(Sale sale) {
		this.sale = sale;
	}

	public Sale getSale() {
		return sale;
	}
}
