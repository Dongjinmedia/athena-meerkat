package com.athena.meerkat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.athena.meerkat.controller.web.common.converter.JsonHttpMessageConverter;
import com.athena.meerkat.controller.web.common.converter.StringToNumberConverterFactory;

/**
 * Spring MVC Configuration
 * @author BongJin Kwon
 * 
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	
	@Autowired
	private JsonHttpMessageConverter jsonCustomConverter;

	@Override
	public void addFormatters(FormatterRegistry registry) {
		
		// 'yyyy-MM-dd' format string to Date converting.
		registry.addFormatter(new DateFormatter("yyyy-MM-dd"));
		
		registry.removeConvertible(String.class, Number.class);// remove default converter.
		registry.addConverterFactory(new StringToNumberConverterFactory());
	}


	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		
		converters.add(jsonCustomConverter);
	}
	
	
	
	


}