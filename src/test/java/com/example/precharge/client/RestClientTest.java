package com.example.precharge.client;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.precharge.TestBase;
import com.example.precharge.client.model.ThirdPaymentRequest;
import com.example.precharge.client.model.ThirdPaymentResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

class RestClientTest extends TestBase {
  @Autowired private RestTemplate restTemplate;
  private RestClient restClient;
  @Autowired private WireMockServer wireMockServer;
  @Autowired ObjectMapper objectMapper;
  private String baseUrl;

  @BeforeEach
  void setUp() {
    configureFor("localhost", wireMockServer.port());
    baseUrl = String.format("http://localhost:%s", wireMockServer.port());
    restClient = new RestClient(restTemplate, baseUrl);
  }

  @Test
  @SneakyThrows
  public void shouldSucceedPostCallWhenEndpointAndClazzAreCorrect() {
    ThirdPaymentRequest request = new ThirdPaymentRequest("Siwo rental precharge", 300);
    String responseJson =
        """
        {
            "responseCode":200,
            "responseMsg":"payment succeed"
        }
       """;
    ThirdPaymentResponse expectedResponse =
        objectMapper.readValue(responseJson, ThirdPaymentResponse.class);
    stubFor(
        post(urlEqualTo("/test"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "Application/Json")
                    .withBody(responseJson)));

    ThirdPaymentResponse actualResponse =
        restClient.post("/test", request, ThirdPaymentResponse.class);

    assertThat(actualResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
  }
}
