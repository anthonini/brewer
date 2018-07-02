package com.anthonini.brewer.repository.helper.beer;

import java.util.List;

import com.anthonini.brewer.model.Beer;
import com.anthonini.brewer.repository.filter.BeerFilter;

public interface BeerRepositoryQueries {

	public List<Beer> filter(BeerFilter filter);
}
