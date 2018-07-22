package com.anthonini.brewer.repository.helper.city;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import com.anthonini.brewer.model.City;
import com.anthonini.brewer.repository.filter.CityFilter;
import com.anthonini.brewer.repository.pagination.PaginationUtil;

public class CityRepositoryImpl implements CityRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private PaginationUtil<City> paginationUtil;
	
	@Override
	public Page<City> filter(CityFilter filter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<City> criteriaQuery = builder.createQuery(City.class);
		Root<City> city = criteriaQuery.from(City.class);
		city.fetch("state");
		
		criteriaQuery.where(getWhere(filter, builder, city));
		
		TypedQuery<City> query = paginationUtil.prepare(builder, criteriaQuery, city, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(filter));
	}
	
	private Long total(CityFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<City> city = criteriaQuery.from(City.class);
		
		criteriaQuery.select(builder.count(city)).where(getWhere(filter, builder, city));
		
		return manager.createQuery(criteriaQuery).getSingleResult();
	}

	private Predicate[] getWhere(CityFilter filter, CriteriaBuilder builder, Root<City> city) {
		List<Predicate> where = new ArrayList<>();
		
		if(filter != null) {			
			if (!StringUtils.isEmpty(filter.getName())) {
				where.add(builder.like(builder.upper(city.get("name")), "%" + filter.getName().toUpperCase() + "%"));
			}
			
			if (hasState(filter)) {
				where.add(builder.equal(city.get("state"), filter.getState()));
			}
		}
		
		return where.stream().toArray(Predicate[]::new);
	}

	private boolean hasState(CityFilter filter) {
		return filter.getState() != null && filter.getState().getId() != null;
	}

}
