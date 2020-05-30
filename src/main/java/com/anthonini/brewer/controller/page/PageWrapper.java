package com.anthonini.brewer.controller.page;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.web.util.UriComponentsBuilder;

public class PageWrapper<T> {

	private Page<T> page;
	private UriComponentsBuilder uriBuilder;
	
	public PageWrapper(Page<T> page, HttpServletRequest httpServletRequest) {
		this.page = page;
		String httpUrl = httpServletRequest.getRequestURL().append(
				httpServletRequest.getQueryString() != null ? "?" + httpServletRequest.getQueryString() : "")
				.toString().replaceAll("\\+", "%20").replaceAll("excluded", "");
		this.uriBuilder = UriComponentsBuilder.fromHttpUrl(httpUrl);
	}
	
	public List<T> getContent() {
		return page.getContent();
	}
	
	public boolean isEmpty() {
		return page.getContent().isEmpty();
	}
	
	public boolean isFirst() {
		return page.isFirst();
	}
	
	public boolean isLast() {
		return page.isLast();
	}
	
	public int getActual() {
		return page.getNumber();
	}
	
	public int getTotal() {
		return page.getTotalPages();
	}
	
	public String pageUrl(int pageNumber) {
		return uriBuilder.replaceQueryParam("page", pageNumber).build(true).encode().toUriString();
	}
	
	public String orderedUrl(String property) {
		UriComponentsBuilder uriBuilderOrder = UriComponentsBuilder
				.fromUriString(uriBuilder.build(true).encode().toUriString());
		
		String sort = String.format("%s,%s", property, invertDirection(property));
		
		return uriBuilderOrder.replaceQueryParam("sort", sort).build(true).encode().toUriString();
	}

	public boolean descending(String property) {
		Order order = getPropertyOrder(property);
		return order != null && order.getDirection().equals(Sort.Direction.ASC);
	}
	
	public boolean ordered(String property) {
		return getPropertyOrder(property) != null;
	}
	
	private Order getPropertyOrder(String property) {
		return page.getSort() != null ? page.getSort().getOrderFor(property) : null; 
	}

	private Object invertDirection(String property) {
		String direcao = "asc";
		
		Order order = getPropertyOrder(property);
		if (order != null) {
			direcao = Sort.Direction.ASC.equals(order.getDirection()) ? "desc" : "asc";
		}
		
		return direcao;
	}
}
