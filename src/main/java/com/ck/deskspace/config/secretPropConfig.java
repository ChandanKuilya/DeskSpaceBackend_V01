package com.ck.deskspace.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "secret")
@Getter
@Setter
public class secretPropConfig {
  private String secret;
}
