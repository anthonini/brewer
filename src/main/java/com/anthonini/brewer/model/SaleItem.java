package com.anthonini.brewer.model;

import java.math.BigDecimal;

public class SaleItem {

	private Long id;
	
	private Beer beer;
	
	private BigDecimal unityValue;
	
	private Integer quantity;
	
	public BigDecimal getTotalValue() {
		return unityValue.multiply(new BigDecimal(quantity));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Beer getBeer() {
		return beer;
	}

	public void setBeer(Beer beer) {
		this.beer = beer;
	}

	public BigDecimal getUnityValue() {
		return unityValue;
	}

	public void setUnityValue(BigDecimal unityValue) {
		this.unityValue = unityValue;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		SaleItem other = (SaleItem) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
