package com.anthonini.brewer.repository.helper.sale;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.anthonini.brewer.model.Sale;
import com.anthonini.brewer.repository.filter.SaleFilter;

public interface SaleRepositoryQueries {

	public Page<Sale> filter(SaleFilter filter, Pageable pageable);
	
	public Sale findWithItems(Long id);
}
