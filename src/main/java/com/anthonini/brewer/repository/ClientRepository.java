package com.anthonini.brewer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anthonini.brewer.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

}
