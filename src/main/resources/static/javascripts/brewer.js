var Brewer = Brewer || {};

Brewer.MaskMoney = (function(){
	function MaskMoney() {
		this.decimal = $('.js-decimal');		
		this.numero = $('.js-numero');
	}
	
	MaskMoney.prototype.enable = function() {
		this.decimal.maskMoney({ decimal: ',', thousands: '.' })
		this.numero.maskMoney({ precision: 0, thousands: '.' });
	}
	
	return MaskMoney;
})();

$(function(){	
	var maskNumber = new Brewer.MaskMoney();
	maskNumber.enable();
});