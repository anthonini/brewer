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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import com.anthonini.brewer.dto.BeerDTO;
import com.anthonini.brewer.dto.StockItemsValue;
import com.anthonini.brewer.model.Beer;
import com.anthonini.brewer.repository.filter.BeerFilter;
import com.anthonini.brewer.repository.pagination.PaginationUtil;

public class BeerRepositoryImpl implements BeerRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private PaginationUtil<Beer> paginationUtil;
	
	@Override
	public Page<Beer> filter(BeerFilter filter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Beer> criteriaQuery = builder.createQuery(Beer.class);
		Root<Beer> beer = criteriaQuery.from(Beer.class);		
		beer.fetch("style");
		
		criteriaQuery.where(getWhere(filter, builder, beer));
		
		TypedQuery<Beer> query = paginationUtil.prepare(builder, criteriaQuery, beer, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(filter));
	}
	
	@Override
	public StockItemsValue stockItemsValue() {
		String query = "select new com.anthonini.brewer.dto.StockItemsValue(sum(value * stockQuantity), sum(stockQuantity)) from Beer";
		return manager.createQuery(query, StockItemsValue.class).getSingleResult();
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
	
	@Override
	public List<BeerDTO> findBySkuOrName(String skuOrName) {
		String jpql = "select new com.anthonini.brewer.dto.BeerDTO(id, sku, name, origin, value, photo) " +
					  "from Beer "+
					  "where lower(sku) like lower(:skuOrName) or lower(name) like lower(:skuOrName)";
		
		return manager.createQuery(jpql, BeerDTO.class)
					  .setParameter("skuOrName", skuOrName + "%")
					  .getResultList();		
	}
}
