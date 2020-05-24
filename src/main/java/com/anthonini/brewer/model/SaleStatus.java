package com.anthonini.brewer.model;

public enum SaleStatus {

	ORCAMENTO("Orçamento"), 
	EMITIDA("Emitida"), 
	CANCELADA("Cancelada");

	private String description;

	SaleStatus(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
