var Brewer = Brewer || {};

Brewer.MaskMoney = (function(){
	function MaskMoney() {
		this.decimal = $('.js-decimal');		
		this.numero = $('.js-numero');
	}
	
	MaskMoney.prototype.enable = function() {
		this.decimal.maskNumber({ decimal: ',', thousands: '.' })
		this.numero.maskNumber({ integer: true, thousands: '.' });
	}
	
	return MaskMoney;
})();

Brewer.MaskPhoneNumber = (function() {
	
	function MaskPhoneNumber() {
		this.inputPhoneNumber = $('.js-phone-number');
	}
	
	MaskPhoneNumber.prototype.enable = function() {
		var maskBehavior = function (val) {
		  return val.replace(/\D/g, '').length === 11 ? '(00) 00000-0000' : '(00) 0000-00009';
		};
		
		var options = {
		  onKeyPress: function(val, e, field, options) {
		      field.mask(maskBehavior.apply({}, arguments), options);
		    }
		};
		
		this.inputPhoneNumber.mask(maskBehavior, options);
	}
	
	return MaskPhoneNumber;
	
}());

Brewer.MaskCep = (function() {
	
	function MaskCep() {
		this.inputCep = $('.js-cep');
	}
	
	MaskCep.prototype.enable = function() {
		this.inputCep.mask('00000-000');
	}
	
	return MaskCep;
	
}());

Brewer.MaskDate = (function() {
	
	function MaskDate() {
		this.inputDate = $('.js-date');
	}
	
	MaskDate.prototype.enable = function() {
		this.inputDate.mask('00/00/0000');
		this.inputDate.datepicker({
			orientation: 'bottom',
			language: 'pt-BR',
			autoclose: true
		});
	}
	
	return MaskDate;
	
}());

Brewer.MaskHour = (function() {
	
	function MaskHour() {
		this.inputHour = $('.js-hour');
	}
	
	MaskHour.prototype.enable = function() {
		this.inputHour.mask('00:00');
	}
	
	return MaskHour;
	
}());

Brewer.Security = (function() {
	
	function Security() {
		this.token = $('input[name=_csrf').val();
		this.header = $('input[name=_csrf_header]').val();
	}
	
	Security.prototype.enable = function() {
		$(document).ajaxSend(function(event, jqxhr, settings){
			jqxhr.setRequestHeader(this.header, this.token);
		}.bind(this));
	}
	
	return Security;
}());

numeral.language('pt-br');

Brewer.formatCurrency = function(value) {	
	return numeral(value).format('0,0.00');
}

Brewer.unFormatCurrency = function(formattedValue) {
	return numeral().unformat(formattedValue);
}

$(function(){	
	var maskNumber = new Brewer.MaskMoney();
	maskNumber.enable();
	
	var maskPhoneNumber = new Brewer.MaskPhoneNumber();
	maskPhoneNumber.enable();
	
	var maskCep = new Brewer.MaskCep();
	maskCep.enable();
	
	var maskDate = new Brewer.MaskDate();
	maskDate.enable();
	
	var maskHour = new Brewer.MaskHour();
	maskHour.enable();
	
	var security = new Brewer.Security();
	security.enable();
});