package com.anthonini.brewer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.anthonini.brewer.service.BeerService;
import com.anthonini.brewer.storage.PhotoStorage;
import com.anthonini.brewer.storage.local.PhotoStorageLocal;

@Configuration
@ComponentScan(basePackageClasses = BeerService.class)
public class ServiceConfig {

	@Bean
	public PhotoStorage photoStorage() {
		return new PhotoStorageLocal();
	}
}
