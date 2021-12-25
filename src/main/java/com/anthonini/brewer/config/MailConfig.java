package com.anthonini.brewer.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@PropertySource(value = { "file:///${user.home}/.brewer-mail.properties" }, ignoreResourceNotFound = true)
public class MailConfig {
	
	@Value("${external.brewer.mail.host:${brewer.mail.host}}")
	private String host;
	
	@Value("${external.brewer.mail.port:${brewer.mail.port}}")
	private int port;
	
	@Value("${external.brewer.mail.username:${brewer.mail.username}}")
	private String username;
	
	@Value("${external.brewer.mail.password:${brewer.mail.password}}")
	private String password;
	
	@Value("${external.brewer.mail.from:${brewer.mail.from}}")
	private String from;

	@Bean
	public JavaMailSender mailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(host);
		mailSender.setPort(port);
		mailSender.setUsername(username);
		mailSender.setPassword(password);
		
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.starttls.enable", true);
		props.put("mail.debug", false);
		props.put("mail.smtp.connectiontimeout", 10000); // miliseconds

		mailSender.setJavaMailProperties(props);
		
		return mailSender;
	}
	
	public String getFrom() {
		return from;
	}
}
