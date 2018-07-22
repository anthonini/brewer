package com.anthonini.brewer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anthonini.brewer.model.Client;
import com.anthonini.brewer.repository.helper.client.ClientRepositoryQueries;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>, ClientRepositoryQueries {

	public Optional<Client> findByCpfCnpj(String cpfCnpj);
}
