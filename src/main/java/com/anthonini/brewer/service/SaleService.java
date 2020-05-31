package com.anthonini.brewer.service;

import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.anthonini.brewer.model.Sale;
import com.anthonini.brewer.model.SaleStatus;
import com.anthonini.brewer.repository.SaleRepository;
import com.anthonini.brewer.service.event.sale.SaleEvent;

@Service
public class SaleService {

	@Autowired
	private SaleRepository saleRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;	
	
	@Transactional
	public Sale save(Sale sale) {
		if(sale.isSaveForbidden()) {
			throw new RuntimeException("Usuário tentando salvar uma venda proibida!");
		}
		
		if(sale.isNew()) {
			sale.setCreationDate(LocalDateTime.now());
		} else {
			Sale saleDB = saleRepository.findOne(sale.getId());
			sale.setCreationDate(saleDB.getCreationDate());
		}
		
		if (sale.getDeliveryDate() != null) {
			sale.setDeliveryDateTime(LocalDateTime.of(sale.getDeliveryDate(), sale.getDeliveryTime() != null ? sale.getDeliveryTime() : LocalTime.NOON));
		}
		
		return saleRepository.saveAndFlush(sale);
	}

	@Transactional
	public void emmit(Sale sale) {
		sale.setStatus(SaleStatus.EMITIDA);
		save(sale);
		
		publisher.publishEvent(new SaleEvent(sale));
	}

	@PreAuthorize("#sale.user == principal.user or hasRole('SALE_CANCEL')")
	@Transactional
	public void cancel(Sale sale) {
		sale = saleRepository.findOne(sale.getId());
		
		sale.setStatus(SaleStatus.CANCELADA);
		save(sale);
	}
}
