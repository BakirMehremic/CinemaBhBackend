package com.atlantbh.cinemabh.config.properties;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "application.rest-client")
public class RestClientProperties {

    @NotNull(message = "Connection timeout is required")
    private Duration connectionTimeout;

    @NotNull(message = "Read timeout is required")
    private Duration readTimeout;
}