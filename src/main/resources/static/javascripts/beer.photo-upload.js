var Brewer = Brewer || {};

Brewer.PhotoUpload = (function() {
	
	function PhotoUpload() {
		this.inputPhotoName = $('input[name=photo]');
		this.inputContentType = $('input[name=contentType]');
		this.inputUrlPhoto = $('input[name=urlPhoto');
		this.newPhoto = $('input[name=newPhoto');
		
		this.htmlBeerPhotoTemplate = $('#beer-photo').html();
		this.template = Handlebars.compile(this.htmlBeerPhotoTemplate);
		
		this.uploadDrop = $('#upload-drop');
		this.containerBeerPhoto = $('.js-beer-photo-container');
		this.imgLoading = $('.js-img-loading');
	}
	
	PhotoUpload.prototype.initiate = function() {
		var settings = {
				type: 'json',
				filelimit: 1,
				allow: '*.(jpg|jpeg|png)',
				action: this.containerBeerPhoto.data('url-photo'),
				complete: onUploadComplete.bind(this),
				beforeSend: addCsrfToken,
				loadstart: onLoadStart.bind(this)
		}
		
		UIkit.uploadSelect($('#upload-select'),settings);
		UIkit.uploadDrop($('#upload-drop'),settings);
		
		if(this.inputPhotoName.val()) {
			renderPhoto.call(this, {
				name: this.inputPhotoName.val(), 
				contentType: this.inputContentType.val(), 
				url: this.inputUrlPhoto.val()
				}
			);
		}
	}
	
	function onLoadStart() {
		this.imgLoading.removeClass('hidden');
	}
	
	function onUploadComplete(answer) {
		this.newPhoto.val('true');
		this.inputUrlPhoto.val(answer.url);
		this.imgLoading.addClass('hidden');
		renderPhoto.call(this, answer);
	}
	
	function renderPhoto(answer) {
		this.inputPhotoName.val(answer.name);
		this.inputContentType.val(answer.contentType);
		
		this.uploadDrop.addClass('hidden');
		
		var htmlBeerPhoto = this.template({url: answer.url});
		this.containerBeerPhoto.append(htmlBeerPhoto);
		
		$('.js-photo-remove').on('click', onPhotoRemove.bind(this));
	}
	
	function onPhotoRemove() {
		$('.js-beer-photo').remove();
		this.uploadDrop.removeClass('hidden');
		this.inputPhotoName.val('');
		this.inputContentType.val('');
		this.newPhoto.val('false');
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