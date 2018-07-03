package com.anthonini.brewer.repository.helper.beer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.anthonini.brewer.model.Beer;
import com.anthonini.brewer.repository.filter.BeerFilter;

public interface BeerRepositoryQueries {

	public Page<Beer> filter(BeerFilter filter, Pageable pageable);
}
