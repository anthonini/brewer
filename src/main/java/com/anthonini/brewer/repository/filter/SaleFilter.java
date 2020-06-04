package com.anthonini.brewer.repository.filter;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.anthonini.brewer.model.SaleStatus;

public class SaleFilter {
	private Long id;
	private SaleStatus status;

	private LocalDate sinceDate;
	private LocalDate toDate;
	private BigDecimal minimumValue;
	private BigDecimal maximumValue;

	private String clientName;
	private String clientCpfOrCnpj;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public SaleStatus getStatus() {
		return status;
	}
	
	public void setStatus(SaleStatus status) {
		this.status = status;
	}
	
	public LocalDate getSinceDate() {
		return sinceDate;
	}

	public void setSinceDate(LocalDate sinceDate) {
		this.sinceDate = sinceDate;
	}

	public LocalDate getToDate() {
		return toDate;
	}

	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}

	public BigDecimal getMinimumValue() {
		return minimumValue;
	}

	public void setMinimumValue(BigDecimal minimumValue) {
		this.minimumValue = minimumValue;
	}

	public BigDecimal getMaximumValue() {
		return maximumValue;
	}
	
	public void setMaximumValue(BigDecimal maximumValue) {
		this.maximumValue = maximumValue;
	}
	
	public String getClientName() {
		return clientName;
	}
	
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	
	public String getClientCpfOrCnpj() {
		return clientCpfOrCnpj;
	}
	
	public void setClientCpfOrCnpj(String clientCpfOrCnpj) {
		this.clientCpfOrCnpj = clientCpfOrCnpj;
	}
}
