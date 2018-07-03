package com.anthonini.brewer.repository.helper.beer;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import com.anthonini.brewer.model.Beer;
import com.anthonini.brewer.repository.filter.BeerFilter;

public class BeerRepositoryImpl implements BeerRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public Page<Beer> filter(BeerFilter filter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Beer> criteriaQuery = builder.createQuery(Beer.class);
		Root<Beer> beer = criteriaQuery.from(Beer.class);
		
		TypedQuery<Beer> query =  manager.createQuery(criteriaQuery.where(getWhere(filter, builder, beer)));
		query.setFirstResult(pageable.getPageNumber()*pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());
		
		return new PageImpl<>(query.getResultList(), pageable, total(filter));
	}
	
	private Long total(BeerFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<Beer> beer = criteriaQuery.from(Beer.class);
		
		criteriaQuery.select(builder.count(beer)).where(getWhere(filter, builder, beer));
		
		return manager.createQuery(criteriaQuery).getSingleResult();
	}

	private Predicate[] getWhere(BeerFilter filter, CriteriaBuilder builder, Root<Beer> beer) {
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
		
		return where.stream().toArray(Predicate[]::new);
	}

	private boolean hasStyle(BeerFilter filter) {
		return filter.getStyle() != null && filter.getStyle().getId()!= null;
	}

}
