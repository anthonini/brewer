package com.anthonini.brewer.model;

public enum Origin {

	NATIONAL("Nacional"),
	INTERNATIONAL("Internacional");
	
	private String description;
	
	Origin(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
	
}
