package com.anthonini.brewer.service;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anthonini.brewer.dto.ReportPeriod;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class ReportService {

	@Autowired
	private DataSource dataSource;
	
	public byte[] generateEmittedSalesReport(ReportPeriod reportPeriod) throws JRException, SQLException {
		
		Date initalDate = Date.from(LocalDateTime.of(reportPeriod.getInitialDate(), LocalTime.of(0,0,0))
				.atZone(ZoneId.systemDefault()).toInstant());
		Date finalDate = Date.from(LocalDateTime.of(reportPeriod.getFinalDate(), LocalTime.of(0,0,0))
				.atZone(ZoneId.systemDefault()).toInstant());
		
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("format", "pdf");
		parameters.put("initial_date", initalDate);
		parameters.put("final_date", finalDate);
		
		InputStream inputStream = this.getClass().getResourceAsStream("/reports/report_emitted_sales.jasper");
		
		try(Connection con = dataSource.getConnection()) {
			JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parameters, con);
			return JasperExportManager.exportReportToPdf(jasperPrint);
		}
	} 
}
