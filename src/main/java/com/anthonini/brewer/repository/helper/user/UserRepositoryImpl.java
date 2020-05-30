package com.anthonini.brewer.repository.helper.user;

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

import com.anthonini.brewer.model.User;
import com.anthonini.brewer.model.UserGroup;
import com.anthonini.brewer.repository.filter.UserFilter;
import com.anthonini.brewer.repository.pagination.PaginationUtil;

public class UserRepositoryImpl implements UserRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private PaginationUtil<User> paginationUtil;
	
	@Override
	public Optional<User> byEmailActive(String email) {
		return manager
				.createQuery("from User where lower(email) = lower(:email) and active = true", User.class)
				.setParameter("email", email).getResultList().stream().findFirst();
	}

	@Override
	public List<String> permissions(User user) {
		return manager.createQuery(
				"select p.name from User u inner join u.groups g inner join g.permissions p where u = :user", String.class)
				.setParameter("user", user)
				.getResultList();
	}
	
	@Override
	public Page<User> filter(UserFilter filter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
		Root<User> user = criteriaQuery.from(User.class);
		user.fetch("groups", JoinType.LEFT);
		
		criteriaQuery.where(getWhere(filter, builder, user));
		
		TypedQuery<User> query = paginationUtil.prepare(builder, criteriaQuery, user, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(filter));
	}
	
	@Override
	public User findWithGroups(Long id) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
		Root<User> user = criteriaQuery.from(User.class);
		user.fetch("groups", JoinType.LEFT);
		
		criteriaQuery.where(builder.equal(user.get("id"), id));

		TypedQuery<User> query =  manager.createQuery(criteriaQuery);
		
		return query.getSingleResult();
	}
	
	private Long total(UserFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<User> user = criteriaQuery.from(User.class);
		
		criteriaQuery.select(builder.count(user)).where(getWhere(filter, builder, user));
		
		return manager.createQuery(criteriaQuery).getSingleResult();
	}

	private Predicate[] getWhere(UserFilter filter, CriteriaBuilder builder, Root<User> user) {
		List<Predicate> where = new ArrayList<>();
		
		if(filter != null) {			
			if (!StringUtils.isEmpty(filter.getName())) {
				where.add(builder.like(builder.upper(user.get("name")), "%" + filter.getName().toUpperCase() + "%"));
			}
			
			if (!StringUtils.isEmpty(filter.getEmail())) {
				where.add(builder.like(builder.upper(user.get("email")), "%" + filter.getEmail().toUpperCase() + "%"));
			}
			
			if (filter.getGroups() != null && !filter.getGroups().isEmpty()) {
				for(UserGroup userGroup : filter.getGroups()) {
					where.add(user.join("groups").in(userGroup));
				}
			}
		}
		
		return where.stream().toArray(Predicate[]::new);
	}
}
