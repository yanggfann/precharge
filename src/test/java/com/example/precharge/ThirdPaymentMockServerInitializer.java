package com.example.precharge;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;

public class ThirdPaymentMockServerInitializer
    implements ApplicationContextInitializer<ConfigurableApplicationContext> {

  @Override
  public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
    WireMockServer thirdPaymentMockServer =
        new WireMockServer(new WireMockConfiguration().dynamicPort());
    thirdPaymentMockServer.start();

    configurableApplicationContext
        .getBeanFactory()
        .registerSingleton("thirdPaymentMockServer", thirdPaymentMockServer);

    configurableApplicationContext.addApplicationListener(
        applicationEvent -> {
          if (applicationEvent instanceof ContextClosedEvent) {
            thirdPaymentMockServer.stop();
          }
        });

    TestPropertyValues.of(
            "third.payment.base.url=http://localhost:" + thirdPaymentMockServer.port())
        .applyTo(configurableApplicationContext);
  }
}
