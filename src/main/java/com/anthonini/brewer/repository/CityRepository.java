package com.anthonini.brewer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anthonini.brewer.model.City;
import com.anthonini.brewer.model.State;

public interface CityRepository extends JpaRepository<City, Long>{

	public List<City> findByStateIdOrderByName(Long stateId);
	
	public Optional<City> findByNameAndState(String name, State state);
}
