package com.belieme.apiserver.config.jasypt;

import lombok.Getter;
import lombok.Setter;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jasypt.encryptor")
public class JasyptConfig {
  private String password;
  private String algorithm = "PBEWithMD5AndDES";
  private String keyObtentionIterations = "1000";
  private String poolSize = "1";
  private String providerName = "SunJCE";
  private String saltGeneratorClassName = "org.jasypt.salt.RandomSaltGenerator";
  private String ivGeneratorClassName = "org.jasypt.iv.RandomIvGenerator";
  private String stringOutputType = "base64";


  @Bean("jasyptStringEncryptor")
  public StringEncryptor stringEncryptor() {
    PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
    SimpleStringPBEConfig config = new SimpleStringPBEConfig();
    config.setPassword(password);
    config.setAlgorithm(algorithm);
    config.setKeyObtentionIterations(keyObtentionIterations);
    config.setPoolSize(poolSize);
    config.setProviderName(providerName);
    config.setSaltGeneratorClassName(saltGeneratorClassName);
    config.setIvGeneratorClassName(ivGeneratorClassName);
    config.setStringOutputType(stringOutputType);
    encryptor.setConfig(config);
    return encryptor;
  }
}
