package com.anthonini.brewer.repository.helper.client;

import java.util.ArrayList;
import java.util.List;

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

import com.anthonini.brewer.model.Client;
import com.anthonini.brewer.model.PersonType;
import com.anthonini.brewer.repository.filter.ClientFilter;
import com.anthonini.brewer.repository.pagination.PaginationUtil;

public class ClientRepositoryImpl implements ClientRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private PaginationUtil<Client> paginationUtil;
	
	@Override
	public Page<Client> filter(ClientFilter filter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Client> criteriaQuery = builder.createQuery(Client.class);
		Root<Client> client = criteriaQuery.from(Client.class);
		client.fetch("address").fetch("city", JoinType.LEFT).fetch("state", JoinType.LEFT);
		
		criteriaQuery.where(getWhere(filter, builder, client));
		
		TypedQuery<Client> query = paginationUtil.prepare(builder, criteriaQuery, client, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(filter));
	}
	
	private Long total(ClientFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<Client> client = criteriaQuery.from(Client.class);
		
		criteriaQuery.select(builder.count(client)).where(getWhere(filter, builder, client));
		
		return manager.createQuery(criteriaQuery).getSingleResult();
	}

	private Predicate[] getWhere(ClientFilter filter, CriteriaBuilder builder, Root<Client> client) {
		List<Predicate> where = new ArrayList<>();
		
		if(filter != null) {			
			if (!StringUtils.isEmpty(filter.getName())) {
				where.add(builder.like(builder.upper(client.get("name")), "%" + filter.getName().toUpperCase() + "%"));
			}
			
			if (!StringUtils.isEmpty(filter.getCpfCnpj())) {
				where.add(builder.equal(client.get("cpfCnpj"), PersonType.removeFormatting(filter.getCpfCnpj())));
			}
		}
		
		return where.stream().toArray(Predicate[]::new);
	}
}
