package com.anthonini.brewer.model;

public enum Flavor {

	SWEET("Adocicada"),
	BITTER("Amarga"),
	STRONG("Forte"),
	FRUITY("Frutada"),
	SOFT("Suave");	
	
	private String description;
	
	Flavor(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}