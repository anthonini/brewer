package com.anthonini.brewer.repository.helper.sale;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import com.anthonini.brewer.dto.MonthSale;
import com.anthonini.brewer.dto.OriginSale;
import com.anthonini.brewer.model.PersonType;
import com.anthonini.brewer.model.Sale;
import com.anthonini.brewer.model.SaleStatus;
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
	
	@Override
	public Sale findWithItems(Long id) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Sale> criteriaQuery = builder.createQuery(Sale.class);
		Root<Sale> sale = criteriaQuery.from(Sale.class);
		sale.fetch("items", JoinType.LEFT);
		
		criteriaQuery.where(builder.equal(sale.get("id"), id));

		TypedQuery<Sale> query =  manager.createQuery(criteriaQuery);
		
		return query.getSingleResult();
	}
	
	@Override
	public BigDecimal yearTotalValue() {
		Optional<BigDecimal> optional = Optional.ofNullable(manager.createQuery("SELECT SUM(totalValue) From Sale WHERE YEAR(creationDate) = :year and status = :status", BigDecimal.class)
			.setParameter("year", Year.now().getValue())
			.setParameter("status", SaleStatus.EMITIDA)
			.getSingleResult());
		return optional.orElse(BigDecimal.ZERO);
	}
	
	@Override
	public BigDecimal monthTotalValue() {
		Optional<BigDecimal> optional = Optional.ofNullable(manager.createQuery("SELECT SUM(totalValue) From Sale WHERE MONTH(creationDate) = :month and status = :status", BigDecimal.class)
				.setParameter("month", MonthDay.now().getMonthValue())
				.setParameter("status", SaleStatus.EMITIDA)
				.getSingleResult());
			return optional.orElse(BigDecimal.ZERO);
	}
	
	@Override
	public BigDecimal avgTicket() {
		Optional<BigDecimal> optional = Optional.ofNullable(manager.createQuery("SELECT SUM(totalValue)/count(*) From Sale WHERE YEAR(creationDate) = :year and status = :status", BigDecimal.class)
				.setParameter("year", Year.now().getValue())
				.setParameter("status", SaleStatus.EMITIDA)
				.getSingleResult());
			return optional.orElse(BigDecimal.ZERO);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MonthSale> totalByMonth() {
		List<MonthSale> monthSales = manager.createNamedQuery("Sale.totalByMonth").getResultList();
		
		LocalDate today = LocalDate.now();
		for (int i = 1; i <= 6; i++) {
			String idealMonth = String.format("%d/%02d", today.getYear(), today.getMonthValue());
			
			boolean hasMonth = monthSales.stream().filter(v -> v.getMonth().equals(idealMonth)).findAny().isPresent();
			if (!hasMonth) {
				monthSales.add(i - 1, new MonthSale(idealMonth, 0));
			}
			
			today = today.minusMonths(1);
		}
		
		return monthSales;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<OriginSale> byOrigin() {
		List<OriginSale> originSales = manager.createNamedQuery("Sale.byOrigin").getResultList();
		
		LocalDate now = LocalDate.now();
		for (int i = 1; i <= 6; i++) {
			String idealMonth = String.format("%d/%02d", now.getYear(), now.getMonth().getValue());
			
			boolean possuiMes = originSales.stream().filter(s -> s.getMonth().equals(idealMonth)).findAny().isPresent();
			if (!possuiMes) {
				originSales.add(i - 1, new OriginSale(idealMonth, 0, 0));
			}
			
			now = now.minusMonths(1);
		}
		
		return originSales;
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
