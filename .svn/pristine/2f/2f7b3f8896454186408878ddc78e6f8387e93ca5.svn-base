package com.vortex.cloud.ums.ui.config;

import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.vortex.cloud.ums.ui.support.UmsFeignConstants;

import feign.Request;

@Configuration
@EnableFeignClients(basePackages = { "com.vortex.cloud.ums.ui.service" })
public class UmsFeignConfig {

	@Bean
	public Request.Options feginOption() {
		Request.Options option = new Request.Options(UmsFeignConstants.connectTimeoutMillis, UmsFeignConstants.readTimeoutMillis);
		return option;
	}
}
