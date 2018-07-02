package com.anthonini.brewer.repository.helper.beer;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.anthonini.brewer.model.Beer;
import com.anthonini.brewer.repository.filter.BeerFilter;

public class BeerRepositoryImpl implements BeerRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public List<Beer> filter(BeerFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Beer> criteriaQuery = builder.createQuery(Beer.class);
		Root<Beer> beer = criteriaQuery.from(Beer.class);
		
		List<Predicate> where = new ArrayList<>();
		
		if(filter != null) {			
			if (!StringUtils.isEmpty(filter.getSku())) {
				where.add(builder.equal(beer.get("sku"), filter.getSku().toUpperCase()));
			}
			
			if (!StringUtils.isEmpty(filter.getName())) {
				where.add(builder.like(builder.upper(beer.get("name")), "%" + filter.getName().toUpperCase() + "%"));
			}

			if (hasStyle(filter)) {
				where.add(builder.equal(beer.get("style"), filter.getStyle()));
			}

			if (filter.getFlavor() != null) {
				where.add(builder.equal(beer.get("flavor"), filter.getFlavor()));
			}

			if (filter.getOrigin() != null) {
				where.add(builder.equal(beer.get("origin"), filter.getOrigin()));
			}

			if (filter.getFromValue() != null) {
				where.add(builder.ge(beer.get("value"), filter.getFromValue()));
			}

			if (filter.getToValue() != null) {
				where.add(builder.le(beer.get("value"), filter.getToValue()));
			}
		}
		
		return manager.createQuery(criteriaQuery.where(where.stream().toArray(Predicate[]::new))).getResultList();
	}

	private boolean hasStyle(BeerFilter filter) {
		return filter.getStyle() != null && filter.getStyle().getId()!= null;
	}

}
