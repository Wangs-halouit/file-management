package com.halouit.filemanagement.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Setter
@Getter
@ConfigurationProperties(prefix = "file.upload")
public class FileConfig {
    private String path;
    private Duration expiration;
}
