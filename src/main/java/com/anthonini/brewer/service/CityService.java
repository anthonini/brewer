package com.anthonini.brewer.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anthonini.brewer.model.City;
import com.anthonini.brewer.repository.CityRepository;
import com.anthonini.brewer.service.exception.CityAlreadyRegisteredException;

@Service
public class CityService {

	@Autowired
	private CityRepository cityRepository;
	
	@Transactional
	public void save(City city) throws CityAlreadyRegisteredException {
		Optional<City> cityByNameAndState = cityRepository.findByNameAndState(city.getName(), city.getState());
		
		if(cityByNameAndState.isPresent() && !cityByNameAndState.get().equals(city)) {
			throw new CityAlreadyRegisteredException("Cidade já cadastrada");
		}
		
		cityRepository.save(city);
	}
}
