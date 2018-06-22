package com.anthonini.brewer.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anthonini.brewer.model.Style;
import com.anthonini.brewer.repository.StyleRepository;
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
}
