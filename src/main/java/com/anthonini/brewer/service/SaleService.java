package com.anthonini.brewer.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anthonini.brewer.model.Sale;
import com.anthonini.brewer.model.SaleItem;
import com.anthonini.brewer.repository.SaleRepository;

@Service
public class SaleService {

	@Autowired
	private SaleRepository saleRepository;
	
	@Transactional
	public void save(Sale sale) {
		if(sale.isNew()) {
			sale.setCreationDate(LocalDateTime.now());
		}
		
		BigDecimal itemsTotalValue = sale.getItems().stream()
				.map(SaleItem::getTotalValue)
				.reduce(BigDecimal::add)
				.get();
		BigDecimal totalValue = calculateTotalValue(itemsTotalValue, sale.getShippingValue(), sale.getDiscountValue());
		sale.setTotalValue(totalValue);
		
		if (sale.getDeliveryDate() != null) {
			sale.setDeliveryDateTime(LocalDateTime.of(sale.getDeliveryDate(), sale.getDeliveryTime()));
		}
		
		saleRepository.save(sale);
	}
	
	private BigDecimal calculateTotalValue(BigDecimal ItemsTotalValue, BigDecimal shippingValue, BigDecimal discountValue) {
		BigDecimal valorTotal = ItemsTotalValue
				.add(Optional.ofNullable(shippingValue).orElse(BigDecimal.ZERO))
				.subtract(Optional.ofNullable(discountValue).orElse(BigDecimal.ZERO));
		return valorTotal;
	}
}
