package com.anthonini.brewer.service;

import java.util.Optional;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anthonini.brewer.model.Style;
import com.anthonini.brewer.repository.StyleRepository;
import com.anthonini.brewer.service.exception.NotPossibleDeleteEntityException;
import com.anthonini.brewer.service.exception.StyleNameAlreadyRegisteredException;

@Service
public class StyleService {

	@Autowired
	private StyleRepository styleRepository;
	
	@Transactional
	public void save(Style style) {
		Optional<Style> styleOptional = styleRepository.findByNameIgnoreCase(style.getName());
		if(styleOptional.isPresent()) {
			throw new StyleNameAlreadyRegisteredException("Nome do estilo já cadastrado");
		}
		
		styleRepository.save(style);
	}

	@Transactional
	public void delete(Style style) {
		try {
			styleRepository.delete(style);
			styleRepository.flush();
		} catch (PersistenceException e) {
			throw new NotPossibleDeleteEntityException("Não é possivel apagar o estilo. O estilo já foi utilizado em alguma cerveja.");
		}
	}
}
