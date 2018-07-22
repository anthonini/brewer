package com.anthonini.brewer.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import com.anthonini.brewer.model.City;

public class CityConverter implements Converter<String, City> {

	@Override
	public City convert(String id) {
		if (!StringUtils.isEmpty(id)) {
			City city = new City();
			city.setId(Long.valueOf(id));
			
			return city;
		}
		
		return null;
	}
}
