var Brewer = Brewer || {};

Brewer.CpfAndCnpjMask = (function() {

	function CpfAndCnpjMask() {
		this.inputCpfCnpj = $('#cpfCnpj');
	}
	
	CpfAndCnpjMask.prototype.start = function() {
		var cpfCnpjMask = function (val) {
		   return val.replace(/\D/g, '').length > 11 ? '00.000.000/0000-00' : '000.000.000-009';
		},
		cpfCnpjOptions = {
		   onKeyPress: function(val, e, field, options) {
		      field.mask(cpfCnpjMask.apply({}, arguments), options);
		   }
		};
		this.inputCpfCnpj.mask(cpfCnpjMask, cpfCnpjOptions);
	}
		
	return CpfAndCnpjMask;	
}());

$(function () {
	var cpfAndCnpjMask = new Brewer.CpfAndCnpjMask();
	cpfAndCnpjMask.start();
});