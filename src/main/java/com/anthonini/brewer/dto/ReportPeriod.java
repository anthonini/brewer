package com.anthonini.brewer.dto;

import java.time.LocalDate;

public class ReportPeriod {

	private LocalDate initialDate;
	private LocalDate finalDate;
	
	public LocalDate getInitialDate() {
		return initialDate;
	}
	public void setInitialDate(LocalDate initialDate) {
		this.initialDate = initialDate;
	}
	public LocalDate getFinalDate() {
		return finalDate;
	}
	public void setFinalDate(LocalDate finalDate) {
		this.finalDate = finalDate;
	}
}
