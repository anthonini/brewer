package com.anthonini.brewer.dto;

public class MonthSale {

	private String month;
	private Integer total;
	
	public MonthSale() {
	}
	
	public MonthSale(String month, Integer total) {
		this.month = month;
		this.total = total;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}
}
