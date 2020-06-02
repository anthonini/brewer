package com.anthonini.brewer.controller;

import java.sql.SQLException;

import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.anthonini.brewer.dto.ReportPeriod;
import com.anthonini.brewer.service.ReportService;

import net.sf.jasperreports.engine.JRException;

@Controller
@RequestMapping("/report")
public class ReportController {
	
	@Autowired
	private ReportService reportService;

	@GetMapping("/emittedSales")
	public ModelAndView emittedSalesReport() {
		ModelAndView mv = new ModelAndView("report/reportEmittedSales");
		mv.addObject(new ReportPeriod());
		
		return mv;
	}
	
	@PostMapping("/emittedSales")
	public ResponseEntity<byte[]> generateEmittedSalesReport(ReportPeriod reportPeriod) throws JRException, SQLException {
		byte[] report = reportService.generateEmittedSalesReport(reportPeriod);
		
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.body(report);
	}
}
