package com.anthonini.brewer.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.anthonini.brewer.service.BeerService;
import com.anthonini.brewer.storage.PhotoStorage;

@Configuration
@ComponentScan(basePackageClasses = {BeerService.class, PhotoStorage.class})
public class ServiceConfig {

}
