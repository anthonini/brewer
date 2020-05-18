package com.anthonini.brewer.sale;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.anthonini.brewer.model.Beer;

public class SaleItemsTableTest {
	
	private SaleItemsTable saleItemsTable;
	
	@Before
	public void setUp() {
		saleItemsTable = new SaleItemsTable();
	}

	@Test
	public void testCalcTotalValueWithoutItems() {
		assertEquals(BigDecimal.ZERO, saleItemsTable.getTotalValue());
	}
	
	@Test
	public void testCalcTotalValueWithOneItem() {
		BigDecimal value = new BigDecimal("8.99");
		Beer beer = new Beer();
		beer.setValue(value);		
		saleItemsTable.addItem(beer, 1);
		
		assertEquals(new BigDecimal("8.99"), saleItemsTable.getTotalValue());
	}
	
	@Test
	public void testCalcTotalValueWithManyItems() {
		BigDecimal v1 = new BigDecimal("8.99");
		Beer b1 = new Beer();
		b1.setValue(v1);
		
		BigDecimal v2 = new BigDecimal("4.99");
		Beer b2 = new Beer();
		b2.setValue(v2);
		
		saleItemsTable.addItem(b1, 1);
		saleItemsTable.addItem(b2, 2);		
		
		assertEquals(new BigDecimal("18.97"), saleItemsTable.getTotalValue());
	}
}
