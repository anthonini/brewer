var Brewer = Brewer || {};

Brewer.PhotoUpload = (function() {
	
	function PhotoUpload() {
		this.inputPhotoName = $('input[name=photo]');
		this.inputContentType = $('input[name=contentType]');
		
		this.htmlBeerPhotoTemplate = $('#beer-photo').html();
		this.template = Handlebars.compile(this.htmlBeerPhotoTemplate);
		
		this.uploadDrop = $('#upload-drop');
		this.containerBeerPhoto = $('.js-beer-photo-container');
	}
	
	PhotoUpload.prototype.initiate = function() {
		var settings = {
				type: 'json',
				filelimit: 1,
				allow: '*.(jpg|jpeg|png)',
				action: this.containerBeerPhoto.data('url-photo'),
				complete: onUploadComplete.bind(this),
				beforeSend: addCsrfToken
		}
		
		UIkit.uploadSelect($('#upload-select'),settings);
		UIkit.uploadDrop($('#upload-drop'),settings);
		
		if(this.inputPhotoName.val()) {
			onUploadComplete.call(this, {name: this.inputPhotoName.val(), contentType: this.inputContentType.val() });
		}
	}
	
	function onUploadComplete(answer) {
		this.inputPhotoName.val(answer.name);
		this.inputContentType.val(answer.contentType);
		
		this.uploadDrop.addClass('hidden');
		var htmlBeerPhoto = this.template({photoName: answer.name});
		this.containerBeerPhoto.append(htmlBeerPhoto);
		
		$('.js-photo-remove').on('click', onPhotoRemove.bind(this));
	}
	
	function onPhotoRemove() {
		$('.js-beer-photo').remove();
		this.uploadDrop.removeClass('hidden');
		this.inputPhotoName.val('');
		this.inputContentType.val('');
	}
	
	function addCsrfToken(xhr) {
		var token = $('input[name=_csrf').val();
		var header = $('input[name=_csrf_header]').val();
		xhr.setRequestHeader(header, token);
	}
	
	return PhotoUpload;
	
}());

$(function() {
	var photoUpload = new Brewer.PhotoUpload();
	photoUpload.initiate();
})