var Brewer = Brewer || {};

Brewer.CpfCnpjMask = (function() {

	function CpfCnpjMask() {
		this.personTypeRadio = $('.js-person-type-radio');
		this.labelCpfCnpj = $('[for=cpfCnpj]');
		this.inputCpfCnpj = $('#cpfCnpj');
	}
	
	CpfCnpjMask.prototype.start = function() {
		this.personTypeRadio.on('change', onPersonTypeRadioChanged.bind(this));
		
		var selectedPersonType = this.personTypeRadio.filter(':checked')[0];
		if( selectedPersonType ) {
			applyMask.call(this, $(selectedPersonType));
		}
	}
	
	function onPersonTypeRadioChanged(event) {
		var selectedPersonType = $(event.currentTarget);
		applyCpfCnpjMask.call(this, selectedPersonType);
		this.inputCpfCnpj.val('');
	}
	
	function applyCpfCnpjMask(selectedPersonType){
		this.labelCpfCnpj.text(selectedPersonType.data('document'));
		this.inputCpfCnpj.mask(selectedPersonType.data('mascara'));
		this.inputCpfCnpj.removeAttr('disabled');
	}
		
	return CpfCnpjMask;	
}());

$(function () {
	var cpfCnpjMask = new Brewer.CpfCnpjMask();
	cpfCnpjMask.start();
});