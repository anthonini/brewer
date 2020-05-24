package com.anthonini.brewer.repository.helper.sale;

import java.time.LocalDateTime;
import java.time.LocalTime;
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

import com.anthonini.brewer.model.PersonType;
import com.anthonini.brewer.model.Sale;
import com.anthonini.brewer.repository.filter.SaleFilter;
import com.anthonini.brewer.repository.pagination.PaginationUtil;

public class SaleRepositoryImpl implements SaleRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private PaginationUtil<Sale> paginationUtil;
	
	@Override
	public Page<Sale> filter(SaleFilter filter, Pageable pageable) {	
		CriteriaBuilder builder = manager.getCriteriaBuilder(); 
		CriteriaQuery<Sale> criteriaQuery = builder.createQuery(Sale.class); 
		Root<Sale> sale = criteriaQuery.from(Sale.class); 
		sale.fetch("client");
		sale.fetch("user");
		
		criteriaQuery.where(getWhere(filter, builder, sale));
		  
		TypedQuery<Sale> query = paginationUtil.prepare(builder, criteriaQuery, sale, pageable);
			 
		return new PageImpl<>(query.getResultList(), pageable, total(filter));
	}
	
	private Long total(SaleFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<Sale> sale = criteriaQuery.from(Sale.class);
		
		criteriaQuery.select(builder.count(sale)).where(getWhere(filter, builder, sale));
		
		return manager.createQuery(criteriaQuery).getSingleResult();
	}

	private Predicate[] getWhere(SaleFilter filter, CriteriaBuilder builder, Root<Sale> sale) {
		List<Predicate> where = new ArrayList<>();
		
		if(filter != null) {
			if (!StringUtils.isEmpty(filter.getId())) {
				where.add(builder.equal(sale.get("id"), filter.getId()));
			}

			if (filter.getStatus() != null) {
				where.add(builder.equal(sale.get("status"), filter.getStatus()));
			}
			
			if (filter.getSinceDate() != null) {
				LocalDateTime since = LocalDateTime.of(filter.getSinceDate(), LocalTime.of(0, 0));
				where.add(builder.greaterThanOrEqualTo(sale.get("creationDate"), since));
			}
			
			if (filter.getToDate() != null) {
				LocalDateTime toDate = LocalDateTime.of(filter.getToDate(), LocalTime.of(23, 59));
				where.add(builder.lessThanOrEqualTo(sale.get("creationDate"), toDate));
			}
			
			if(filter.getMinimumValue() != null) {
				where.add(builder.ge(sale.get("totalValue"), filter.getMinimumValue()));
			}
			
			if(filter.getMaximumValue() != null) {
				where.add(builder.le(sale.get("totalValue"), filter.getMaximumValue()));
			}
			
			if(!StringUtils.isEmpty(filter.getClientName())) {
				where.add(builder.like(builder.upper(sale.get("client").get("name")), "%" + filter.getClientName().toUpperCase() + "%"));
			}
			
			if(!StringUtils.isEmpty(filter.getClientCpfOrCnpj())) {
				where.add(builder.equal(sale.get("client").get("cpfCnpj"), PersonType.removeFormatting(filter.getClientCpfOrCnpj())));
			}
		}
		
		return where.stream().toArray(Predicate[]::new);
	}

}
