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

$(function() {
	var monthSaleChart = new Brewer.MonthSaleChart();
	monthSaleChart.iniciar();
});