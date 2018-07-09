package com.anthonini.brewer.repository.pagination;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PaginationUtil<T> {
	
	@PersistenceContext
	private EntityManager manager;

	public TypedQuery<T> prepare(CriteriaBuilder builder, CriteriaQuery<T> criteriaQuery, Root<T> root, Pageable pageable) {
		Sort sort = pageable.getSort();
		if (sort != null) {
			Sort.Order order = sort.iterator().next();
			String property = order.getProperty();
			criteriaQuery.orderBy(order.isAscending() ? builder.asc(root.get(property)) : builder.desc(root.get(property)));
		}
		
		TypedQuery<T> query =  manager.createQuery(criteriaQuery);
		query.setFirstResult(pageable.getPageNumber()*pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());
		
		return query;
	}
}
