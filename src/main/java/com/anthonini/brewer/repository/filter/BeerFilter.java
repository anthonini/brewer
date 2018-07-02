package com.anthonini.brewer.repository.filter;

import java.math.BigDecimal;

import com.anthonini.brewer.model.Flavor;
import com.anthonini.brewer.model.Origin;
import com.anthonini.brewer.model.Style;

public class BeerFilter {

	private String sku;
	private String name;	
	private Origin origin;	
	private Flavor flavor;	
	private Style style;	
	private BigDecimal fromValue;	
	private BigDecimal toValue;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Origin getOrigin() {
		return origin;
	}

	public void setOrigin(Origin origin) {
		this.origin = origin;
	}

	public Flavor getFlavor() {
		return flavor;
	}

	public void setFlavor(Flavor flavor) {
		this.flavor = flavor;
	}

	public Style getStyle() {
		return style;
	}

	public void setStyle(Style style) {
		this.style = style;
	}

	public BigDecimal getFromValue() {
		return fromValue;
	}

	public void setFromValue(BigDecimal fromValue) {
		this.fromValue = fromValue;
	}

	public BigDecimal getToValue() {
		return toValue;
	}

	public void setToValue(BigDecimal toValue) {
		this.toValue = toValue;
	}
}
