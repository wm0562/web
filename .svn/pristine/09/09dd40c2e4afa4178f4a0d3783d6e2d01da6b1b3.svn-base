package com.vortex.cloud.ums.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Configuration
@RefreshScope
public class ManagerConfig {
	@Value("${URL_GATEWAY}")
	private String URL_GATEWAY;

	public String getURL_GATEWAY() {
		return URL_GATEWAY;
	}

	public void setURL_GATEWAY(String uRL_GATEWAY) {
		URL_GATEWAY = uRL_GATEWAY;
	}
}
