package com.anthonini.brewer.repository.helper.city;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.anthonini.brewer.model.City;
import com.anthonini.brewer.repository.filter.CityFilter;

public interface CityRepositoryQueries {

	public Page<City> filter(CityFilter filter, Pageable pageable);
}
