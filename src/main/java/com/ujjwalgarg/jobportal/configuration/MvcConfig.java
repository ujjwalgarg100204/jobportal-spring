package com.ujjwalgarg.jobportal.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * MVC configuration class for enabling CORS and serving static resources.
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

	/**
	 * Enables Cross-Origin Resource Sharing (CORS) for all API endpoints
	 * (`/api/**`) which is significant only during development but also
	 * applies in production
	 * This allows requests from different origins (domains, ports, or protocols) to
	 * access the API.
	 *
	 * @param registry The CORS registry to configure CORS mappings.
	 */
	@Override
	public void addCorsMappings(@SuppressWarnings("null") CorsRegistry registry) {
		registry.addMapping("/api/**")
				.allowedOriginPatterns("*");
	}

}
