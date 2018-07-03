package com.anthonini.brewer.thymeleaf;

import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;

import com.anthonini.brewer.thymeleaf.processor.ClassForErrorAttributeTagProcessor;
import com.anthonini.brewer.thymeleaf.processor.MessageElementTagProcessessor;

public class BrewerDialect extends AbstractProcessorDialect {

	public BrewerDialect() {
		super("Anthonini Brewer", "brewer", StandardDialect.PROCESSOR_PRECEDENCE);
	}

	@Override
	public Set<IProcessor> getProcessors(String dialectPrefix) {
		final Set<IProcessor> processadores = new HashSet<>();
		processadores.add(new ClassForErrorAttributeTagProcessor(dialectPrefix));
		processadores.add(new MessageElementTagProcessessor(dialectPrefix));
		
		return processadores;
	}

}