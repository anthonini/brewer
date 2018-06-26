package com.anthonini.brewer.thymeleaf.processor;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

public class MessageElementTagProcessessor extends AbstractElementTagProcessor {

	private static final String TAG_NAME = "message";
	private static final int PRECEDENCE = 1000;
	
	public MessageElementTagProcessessor(String dialectPrefix) {
		super(TemplateMode.HTML, dialectPrefix, TAG_NAME, true, null, false, PRECEDENCE);
	}
	
	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag,
			IElementTagStructureHandler structureHandler) {
		
		IModelFactory factory = context.getModelFactory();
		IModel model = factory.createModel();
		model.add(factory.createStandaloneElementTag("th:block", "th:replace", "fragments/SuccessMessage::message"));
		model.add(factory.createStandaloneElementTag("th:block", "th:replace", "fragments/ValidationErrorMessages::message"));
		
		structureHandler.replaceWith(model, true);
	}

}
