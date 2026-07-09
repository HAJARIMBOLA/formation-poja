package com.example.demo.conf;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.example.demo.PojaGenerated;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@PojaGenerated
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class FacadeIT {

  private static final Logger log = LoggerFactory.getLogger(FacadeIT.class);

  @SneakyThrows
  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {

    new BucketConf().configureProperties(registry);
    new EmailConf().configureProperties(registry);
    new EventConf().configureProperties(registry);
    new DbConf().configureProperties(registry);

    try {
      Class<?> envConfClazz = Class.forName("com.example.demo.conf.EnvConf");
      var configureMethod =
          envConfClazz.getDeclaredMethod("configureProperties", DynamicPropertyRegistry.class);

      Object envConf = envConfClazz.getConstructor().newInstance();
      configureMethod.invoke(envConf, registry);

    } catch (ClassNotFoundException e) {
      log.warn("EnvConf missing: no project-specific test env vars will be set");
    }
  }
}
