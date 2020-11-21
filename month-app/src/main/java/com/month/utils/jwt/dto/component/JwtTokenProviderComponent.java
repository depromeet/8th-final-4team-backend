package com.month.utils.jwt.dto.component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@ConfigurationProperties(prefix = "jwt")
@NoArgsConstructor
@Component
public class JwtTokenProviderComponent {

	private String secretKey;

	private String issuer;

}
