var Brewer = Brewer || {};

Brewer.FastStyleRegister = (function(){
	function FastStyleRegister() {
		this.modal = $('#modalFastStyleRegister');
		this.saveButton = this.modal.find('.js-style-fast-register-save-button');
		this.form = this.modal.find('form');
		this.url = this.form.attr('action');
		this.inputStyle = $('#styleName');
		this.errorMessageContainer = this.modal.find('.js-style-fast-register-error-message');
	}
	
	FastStyleRegister.prototype.start = function() {
		this.modal.on('hide.bs.modal', onModalClose.bind(this));
		this.form.on('submit', onFormSubmit.bind(this) );
		this.saveButton.on('click', registerStyle.bind(this));
	}
	
	function onModalClose() {
		this.inputStyle.val('');
		this.errorMessageContainer.addClass('hidden');
		this.form.find('.form-group').removeClass('has-error');
	}
	
	function onFormSubmit(event) {
		event.preventDefault();		
		registerStyle.call(this);
	}
	
	function registerStyle() {
		var styleName = this.inputStyle.val().trim();
		$.ajax({
			url: this.url,
			method: 'POST',
			contentType: 'application/json',
			data: JSON.stringify({ name: styleName }),
			error: onRegisterStyleError.bind(this),
			success: onStyleRegister.bind(this)
		})
	}
	
	function onRegisterStyleError(obj) {
		this.errorMessageContainer.html('<div><i class="fa  fa-exclamation-circle"></i> ' + obj.responseText + '.</div>');
		this.errorMessageContainer.removeClass('hidden');
		this.form.find('.form-group').addClass('has-error');
	}
	
	function onStyleRegister(style) {
		var styleCombo = $('#style');
		styleCombo.append('<option value="' + style.id + '">' + style.name + '</option>');
		styleCombo.val(style.id);
		this.modal.modal('hide');
	}
	
	return FastStyleRegister;
}());

$(function(){
	var fastStyleRegister = new Brewer.FastStyleRegister();
	fastStyleRegister.start();
})