package com.anthonini.brewer.controller;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.anthonini.brewer.dto.ReportPeriod;

@Controller
@RequestMapping("/report")
public class ReportController {

	@GetMapping("/emittedSales")
	public ModelAndView emittedSalesReport() {
		ModelAndView mv = new ModelAndView("report/reportEmittedSales");
		mv.addObject(new ReportPeriod());
		
		return mv;
	}
	
	@PostMapping("/emittedSales")
	public ModelAndView gerarRelatoriosVendasEmitidas(ReportPeriod reportPeriod) {
		Map<String, Object> parameters = new HashMap<>();
		
		Date initalDate = Date.from(LocalDateTime.of(reportPeriod.getInitialDate(), LocalTime.of(0,0,0))
				.atZone(ZoneId.systemDefault()).toInstant());
		Date finalDate = Date.from(LocalDateTime.of(reportPeriod.getFinalDate(), LocalTime.of(0,0,0))
				.atZone(ZoneId.systemDefault()).toInstant());
		
		parameters.put("format", "pdf");
		parameters.put("initial_date", initalDate);
		parameters.put("final_date", finalDate);
		
		return new ModelAndView("report_emitted_sales", parameters);
	}
}
