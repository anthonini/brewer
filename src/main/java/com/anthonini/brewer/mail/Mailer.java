package com.anthonini.brewer.mail;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.anthonini.brewer.model.Beer;
import com.anthonini.brewer.model.Sale;
import com.anthonini.brewer.model.SaleItem;
import com.anthonini.brewer.storage.PhotoStorage;

@Component
public class Mailer {
	
	private static Logger logger = LoggerFactory.getLogger(Mailer.class);

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private TemplateEngine thymeleaf;
	
	@Autowired
	private PhotoStorage photoStorage;
	
	@Async
	public void send(Sale sale) {
		Context context = new Context(new Locale("pt", "BR"));
		context.setVariable("sale", sale);
		context.setVariable("logo", "logo");
		
		Map<String, String> photos = new HashMap<>();
		boolean addBeerMock = false;
		for (SaleItem item : sale.getItems()) {
			Beer beer = item.getBeer();
			if (beer.hasPhoto()) {
				String bid = "photo-" + beer.getId();
				context.setVariable(bid, bid);
				
				photos.put(bid, beer.getPhoto() + "|" + beer.getContentType());
			} else {
				addBeerMock = true;
				context.setVariable("beerMock", "beerMock");
			}
		}
		
		try {
			String email = thymeleaf.process("mail/saleSummary", context);
			
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			helper.setFrom("anthonnini@gmail.com");
			helper.setTo(sale.getClient().getEmail());
			helper.setSubject(String.format("Brewer - Venda nº %d", sale.getId()));
			helper.setText(email, true);
			
			helper.addInline("logo", new ClassPathResource("static/images/logo-gray.png"));
			
			if (addBeerMock) {
				helper.addInline("beerMock", new ClassPathResource("static/images/beer-mock.png"));
			}
			
			for (String cid : photos.keySet()) {
				String[] photoContentType = photos.get(cid).split("\\|");
				String photo = photoContentType[0];
				String contentType = photoContentType[1];
				byte[] photoArray = photoStorage.recoverThumbnail(photo);
				helper.addInline(cid, new ByteArrayResource(photoArray), contentType);
			}
		
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			logger.error("Erro ao enviar e-mail", e);
		}
	}
}
