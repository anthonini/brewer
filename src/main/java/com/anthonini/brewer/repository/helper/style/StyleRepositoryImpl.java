package com.anthonini.brewer.repository.helper.style;

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

import com.anthonini.brewer.model.Style;
import com.anthonini.brewer.repository.filter.StyleFilter;
import com.anthonini.brewer.repository.pagination.PaginationUtil;

public class StyleRepositoryImpl implements StyleRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private PaginationUtil<Style> paginationUtil;
	
	@Override
	public Page<Style> filter(StyleFilter filter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Style> criteriaQuery = builder.createQuery(Style.class);
		Root<Style> style = criteriaQuery.from(Style.class);
		
		criteriaQuery.where(getWhere(filter, builder, style));
		
		TypedQuery<Style> query = paginationUtil.prepare(builder, criteriaQuery, style, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(filter));
	}
	
	private Long total(StyleFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<Style> style = criteriaQuery.from(Style.class);
		
		criteriaQuery.select(builder.count(style)).where(getWhere(filter, builder, style));
		
		return manager.createQuery(criteriaQuery).getSingleResult();
	}

	private Predicate[] getWhere(StyleFilter filter, CriteriaBuilder builder, Root<Style> style) {
		List<Predicate> where = new ArrayList<>();
		
		if(filter != null) {			
			if (!StringUtils.isEmpty(filter.getName())) {
				where.add(builder.like(builder.upper(style.get("name")), "%" + filter.getName().toUpperCase() + "%"));
			}
		}
		
		return where.stream().toArray(Predicate[]::new);
	}
}
