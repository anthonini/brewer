package com.anthonini.brewer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anthonini.brewer.model.Style;

@Repository
public interface StyleRepository extends JpaRepository<Style, Long> {

}
