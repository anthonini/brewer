$(function(){
	var modal = $('#modalFastStyleRegister');
	var saveButton = modal.find('.js-style-fast-register-save-button');
	var form = modal.find('form');
	var url = form.attr('action');
	var inputStyle = $('#styleName');
	var errorMessageContainer = modal.find('.js-style-fast-register-error-message');
	
	modal.on('hide.bs.modal', onModalClose);
	form.on('submit', function(event) { event.preventDefault(); onFormSubmit(); })
	saveButton.on('click', onFormSubmit);
	
	function onModalClose() {
		inputStyle.val('');
		errorMessageContainer.addClass('hidden');
		form.find('.form-group').removeClass('has-error');
	}
	
	function onFormSubmit() {
		var styleName = inputStyle.val().trim();
		$.ajax({
			url: url,
			method: 'POST',
			contentType: 'application/json',
			data: JSON.stringify({ name: styleName }),
			error: onRegisterStyleError,
			success: onStyleRegister
		})
	}
	
	function onRegisterStyleError(obj) {
		errorMessageContainer.html('<div><i class="fa  fa-exclamation-circle"></i> ' + obj.responseText + '.</div>');
		errorMessageContainer.removeClass('hidden');
		form.find('.form-group').addClass('has-error');
	}
	
	function onStyleRegister(style) {
		var styleCombo = $('#style');
		styleCombo.append('<option value="' + style.id + '">' + style.name + '</option>');
		styleCombo.val(style.id);
		modal.modal('hide');
	}
})