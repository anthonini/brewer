package com.anthonini.brewer.controller.validator;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.anthonini.brewer.model.Sale;

@Component
public class SaleValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Sale.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "client.id", "", "Selecione um cliente na pesquisa rápida");
		
		Sale sale = (Sale)target;
		validateInformedTimeWithoutDate(errors, sale);
		validateItemsInserted(errors, sale);
		validateNegativeTotalValue(errors, sale);
	}

	private void validateNegativeTotalValue(Errors errors, Sale sale) {
		if(sale.getTotalValue().compareTo(BigDecimal.ZERO) < 0) {
			errors.reject("", "Valor total não pode ser negativo");
		}
	}

	private void validateItemsInserted(Errors errors, Sale sale) {
		if(sale.getItems().isEmpty()) {
			errors.reject("", "Adicione pelo menos uma cerveja");
		}
	}

	private void validateInformedTimeWithoutDate(Errors errors, Sale sale) {
		if(sale.getDeliveryTime() != null && sale.getDeliveryDate() == null) {
			errors.rejectValue("deliveryDate", "", "Informe a data da entrega");
		}
	}

}
