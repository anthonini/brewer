package com.anthonini.brewer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anthonini.brewer.model.Style;
import com.anthonini.brewer.repository.helper.style.StyleRepositoryQueries;

@Repository
public interface StyleRepository extends JpaRepository<Style, Long>, StyleRepositoryQueries {

	public Optional<Style> findByNameIgnoreCase(String name);
}
