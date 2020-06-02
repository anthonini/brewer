package com.anthonini.brewer.config.init;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.filter.FormContentFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.anthonini.brewer.config.JPAConfig;
import com.anthonini.brewer.config.MailConfig;
import com.anthonini.brewer.config.S3Config;
import com.anthonini.brewer.config.SecurityConfig;
import com.anthonini.brewer.config.ServiceConfig;
import com.anthonini.brewer.config.WebConfig;

/**
 * Spring Dispatcher Servlet Class Configuration
 * 
 **/
public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
	
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { JPAConfig.class, ServiceConfig.class, SecurityConfig.class, S3Config.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { WebConfig.class, MailConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] {"/"};
	}
	
	@Override
	protected Filter[] getServletFilters() {
		FormContentFilter formContentFilter = new FormContentFilter();
		return new Filter[] { formContentFilter }; 
	}
	
	@Override
	protected void customizeRegistration(Dynamic registration) {
		registration.setMultipartConfig(new MultipartConfigElement(""));
	}
	
	@Override
		public void onStartup(ServletContext servletContext) throws ServletException {
			super.onStartup(servletContext);
			servletContext.setInitParameter("spring.profiles.default", "local");
		}

}
