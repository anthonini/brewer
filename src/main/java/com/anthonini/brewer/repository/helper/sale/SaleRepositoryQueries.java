package com.anthonini.brewer.repository.helper.sale;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.anthonini.brewer.dto.MonthSale;
import com.anthonini.brewer.model.Sale;
import com.anthonini.brewer.repository.filter.SaleFilter;

public interface SaleRepositoryQueries {

	public Page<Sale> filter(SaleFilter filter, Pageable pageable);
	
	public Sale findWithItems(Long id);
	
	public BigDecimal yearTotalValue();
	
	public BigDecimal monthTotalValue();
	
	public BigDecimal avgTicket();
	
	public List<MonthSale> totalByMonth();
}
