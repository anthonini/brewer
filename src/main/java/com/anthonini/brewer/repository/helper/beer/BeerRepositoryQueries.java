package com.anthonini.brewer.repository.helper.beer;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.anthonini.brewer.dto.BeerDTO;
import com.anthonini.brewer.model.Beer;
import com.anthonini.brewer.repository.filter.BeerFilter;

public interface BeerRepositoryQueries {

	public Page<Beer> filter(BeerFilter filter, Pageable pageable);
	
	public List<BeerDTO> findBySkuOrName(String skuOrName);
}
