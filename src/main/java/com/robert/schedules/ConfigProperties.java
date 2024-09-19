package com.robert.schedules;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "teste")
@Getter
@Setter
public class ConfigProperties {
    private String url;
    private String username;
    private String password;


}
