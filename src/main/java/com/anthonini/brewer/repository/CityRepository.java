package com.anthonini.brewer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anthonini.brewer.model.City;

public interface CityRepository extends JpaRepository<City, Long>{

	public List<City> findByStateIdOrderByName(Long stateId);
}
