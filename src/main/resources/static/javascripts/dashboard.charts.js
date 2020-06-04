var Brewer = Brewer || {};

Brewer.MonthSaleChart = (function() {
	
	function MonthSaleChart() {
		this.ctx = $('#monthSaleChart')[0].getContext('2d');
	}
	
	MonthSaleChart.prototype.iniciar = function() {
		$.ajax({
			url: 'sale/totalByMonth',
			method: 'GET',
			success: onReceivedData.bind(this)
		});
	}
	
	function onReceivedData(monthSales) {
		var months = [];
		var values = [];
		monthSales.forEach(function(monthSale){
			months.unshift(monthSale.month);
			values.unshift(monthSale.total);
		});
		
		
		var monthSaleChart = new Chart(this.ctx, {
		    type: 'line',
		    data: {
		    	labels: months,
		    	datasets: [{
		    		label: 'Vendas por mês',
		    		backgroundColor: "rgba(26,179,148,0.5)",
	                pointBorderColor: "rgba(26,179,148,1)",
	                pointBackgroundColor: "#fff",
	                data: values
		    	}]
		    },
		});
	}
	
	return MonthSaleChart;
	
}());

Brewer.SaleByOriginChart = (function() {
	
	function SaleByOriginChart() {
		this.ctx = $('#saleByOriginChart')[0].getContext('2d');
	}
	
	SaleByOriginChart.prototype.iniciar = function() {
		$.ajax({
			url: 'sale/byOrigin',
			method: 'GET', 
			success: onReceivedData.bind(this)
		});
	}
	
	function onReceivedData(originSales) {
		var months = [];
		var nationalBeers = [];
		var internationalBeers = [];
		
		originSales.forEach(function(originSale) {
			months.unshift(originSale.month);
			nationalBeers.unshift(originSale.totalNational);
			internationalBeers.unshift(originSale.totalInternational)
		});
		
		var saleByOriginChart = new Chart(this.ctx, {
		    type: 'bar',
		    data: {
		    	labels: months,
		    	datasets: [{
		    		label: 'Nacional',
		    		backgroundColor: "rgba(220,220,220,0.5)",
	                data: nationalBeers
		    	},
		    	{
		    		label: 'Internacional',
		    		backgroundColor: "rgba(26,179,148,0.5)",
	                data: internationalBeers
		    	}]
		    },
		});
	}
	
	return SaleByOriginChart;
	
}());

$(function() {
	var monthSaleChart = new Brewer.MonthSaleChart();
	monthSaleChart.iniciar();
	
	var saleByOriginChart = new Brewer.SaleByOriginChart();
	saleByOriginChart.iniciar();
});