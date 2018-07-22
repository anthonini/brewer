package com.anthonini.brewer.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

import org.apache.commons.beanutils.BeanUtils;

import com.anthonini.brewer.validation.ConfirmationAttribute;

public class ConfirmationAttributeValidator implements ConstraintValidator<ConfirmationAttribute, Object> {

	private String attribute;
	private String confirmationAttribute;
	
	@Override
	public void initialize(ConfirmationAttribute constraintAnnotation) {
		this.attribute = constraintAnnotation.attribute();
		this.confirmationAttribute = constraintAnnotation.confirmationAttribute();
	}
	
	@Override
	public boolean isValid(Object object, ConstraintValidatorContext context) {
		boolean valid = false;
		try {
			Object attributeValue = BeanUtils.getProperty(object, this.attribute);
			Object confirmatioAttributeValue = BeanUtils.getProperty(object, this.confirmationAttribute);
			
			valid = bothNull(attributeValue, confirmatioAttributeValue) || bothEquals(attributeValue, confirmatioAttributeValue);
		}catch (Exception e) {
			throw new RuntimeException("Error recovering attributes", e);
		}
		
		if(!valid) {
			context.disableDefaultConstraintViolation();
			String message = context.getDefaultConstraintMessageTemplate();
			ConstraintViolationBuilder constraintViolationBuilder = context.buildConstraintViolationWithTemplate(message);
			constraintViolationBuilder.addPropertyNode(confirmationAttribute).addConstraintViolation();
		}
		
		return valid;
	}

	private boolean bothNull(Object attributeValue, Object confirmatioAttributeValue) {
		return attributeValue == null && confirmatioAttributeValue == null;
	}

	private boolean bothEquals(Object attributeValue, Object confirmatioAttributeValue) {
		return attributeValue != null && attributeValue.equals(confirmatioAttributeValue);
	}
}
