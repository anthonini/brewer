package com.anthonini.brewer.dto;

public class OriginSale {

	private String month;
	private Integer totalNational;
	private Integer totalInternational;
	
	public OriginSale() {
	}

	public OriginSale(String month, Integer totalNational, Integer totalInternational) {
		this.month = month;
		this.totalNational = totalNational;
		this.totalInternational = totalInternational;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Integer getTotalNational() {
		return totalNational;
	}

	public void setTotalNational(Integer totalNational) {
		this.totalNational = totalNational;
	}

	public Integer getTotalInternational() {
		return totalInternational;
	}

	public void setTotalInternational(Integer totalInternational) {
		this.totalInternational = totalInternational;
	}	
}
